package com.vellkare.social

import com.vellkare.api.SocialConnectErrorResponse
import com.vellkare.api.SocialConnectResponse
import com.vellkare.api.ValidationErrorResponse
import com.vellkare.core.*
import io.swagger.annotations.Api
import org.apache.http.HttpStatus
import org.springframework.security.oauth2.common.OAuth2AccessToken

import javax.ws.rs.Path
import javax.ws.rs.Produces

/**
 */
@Path("/v0/auth/social")
@Api(value = "social", description = "Operations about Social Connections")
@Produces(["application/json", "application/xml"])
class DigitsController extends SocialController {

  def digitsService
  def securityService
  def memberService

  def connect(DigitsCommand cmd) {
    if (!cmd.validate()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse(cmd.errors)
      log.debug "cmd errors : " + cmd.errors
      return
    }
    def loggedInMember = securityService.currentMember
    def profile = digitsService.getProfile(cmd.authorization, cmd.provider)
    if (!profile) {
      respond new SocialConnectErrorResponse("invalid accessToken.", [])
      return
    }
    def digitsUser = SocialConnection.findByProviderIdAndProviderUserId('Digits', 'dg-' + profile.id)
    def login
    def dgUser
    //if no loggedInMember and new user and signup is true, then cmd should have full user details
    if (!loggedInMember && !digitsUser && !cmd.firstName) {
      respond new SocialConnectErrorResponse("Please provide required details", [])
      return
    }
    if (loggedInMember && digitsUser) {
      def alreadyExistsMsg = ""
      if (digitsUser.uid == loggedInMember.login.id) {
        alreadyExistsMsg = "Digits phone number already connected with your ${loggedInMember.firstName} Account."
      } else
        alreadyExistsMsg = "Digits phone number already connected with another member."
      def map = [socialType: 'Digits']
      respond new SocialConnectErrorResponse(alreadyExistsMsg, [])
      return
    }
    if (digitsUser) {
      digitsUser.accessToken = profile.access_token.token
      digitsUser.secret = profile.access_token.secret
      digitsUser.save(failOnError: true)
      login = Login.get(digitsUser.uid)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    if (loggedInMember) {
      dgUser = new SocialConnection(uid: loggedInMember.login.id, accessToken: profile.access_token.token, providerId: "Digits", providerUserId: "dg-" + profile.id,
        displayName: loggedInMember.firstName, imageUrl: null, profileUrl: null, login: loggedInMember.login, email: loggedInMember.email, expireTime: cmd.expiresIn)
      dgUser.save(failOnError: true)
      def map = [socialType: 'Digits', email: cmd.email]
      respond new SocialConnectResponse("Account linked to ${loggedInMember.firstName}", [])
      return
    }
    login = Login.findByUsername("dg-" + profile.id)
    if (login) {
      dgUser = new SocialConnection(uid: login.id, accessToken: profile.access_token.token, providerId: "Digits", providerUserId: "dg-" + profile.id,
        displayName: cmd.firstName, imageUrl: null, profileUrl: null, login: login, email: cmd.email, expireTime: null)
      dgUser.save(failOnError: true)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    login = new Login(username: "dg-" + profile.id, enabled: true, password: 'CHANGE_ME')
    login.save(failOnError: true)

    dgUser = new SocialConnection(uid: login.id, accessToken: profile.access_token.token, providerId: "Digits", providerUserId: "dg-" + profile.id,
      displayName: cmd.firstName, imageUrl: null, profileUrl: null, login: login, email: cmd.email, expireTime: null)
    dgUser.save(failOnError: true)
    Member m = new Member(login: login, email: cmd.email, firstName: cmd.firstName, lastName: cmd.lastName, phone: profile.phone_number.replace("+", ""))
    m.save(failOnError: true)
    OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
    def tokenApiModel = new TokenApiModel(token)
    def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
    respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
    return
  }
}

class DigitsCommand {
  String clientId
  String authorization
  String provider
  String firstName
  String lastName
  String email

  static constraints = {
    clientId validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("clientId", "clientId.empty")
      }
    }
    authorization validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("authorization", "authorization.empty")
      }
    }
    provider validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("provider", "provider.empty")
      }
    }
    firstName nullable: true    //check manually
    lastName nullable: true
    email nullable: true
  }
}
