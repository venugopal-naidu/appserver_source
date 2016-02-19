package com.vellkare.social

import com.vellkare.api.*
import com.vellkare.core.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.apache.http.HttpStatus
import org.springframework.security.oauth2.common.OAuth2AccessToken

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/v0/auth/social")
@Api(value = "social", description = "Operations about Social Connections")
@Produces(["application/json", "application/xml"])
class LinkedInController extends SocialController {

  def linkedInService
  def securityService
  def memberService

  @GET
  @Path("/linkedIn/login")
  @ApiOperation(value = "User Social Login", notes = "This is used get the login url for a social user.", tags = ["/v0/auth/social", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "social url successfully", response = SocialConnectUrlResponse.class),
    @ApiResponse(code = 404, message = "Invalid login details supplied.", response = SocialConnectErrorResponse.class), //https://github.com/swagger-api/swagger-ui/issues/1055
    @ApiResponse(code = 500, message = "Internal Error", response = InternalErrorResponse.class)])
  def login(String socialId) {
    def m = securityService.currentMember
    if (socialId in grailsApplication.config.vellkare.auth.social.enabled) {
      def url = linkedInService.getAuthorizeURL()
      respond new SocialConnectUrlResponse(url: url)
    } else {
      respond new SocialConnectErrorResponse("Social type is not supported.", [])
    }
  }

  //this is invoked from SocialController
  /*@GET
  @Path("/linkedIn/authorize")
  @ApiOperation(value = "User Social authorize", notes = "This is used to authorize a social user. This is invoked by the social connection application.", tags=["/v0/auth/social","all.public"])
  @ApiResponses(value = [
          @ApiResponse(code = 200, message = "social authorization", response=SocialConnectUrlResponse.class),
          @ApiResponse(code = 404, message = "Invalid login details supplied.", response=SocialConnectErrorResponse.class), //https://github.com/swagger-api/swagger-ui/issues/1055
          @ApiResponse(code = 500, message = "Internal Error", response=InternalErrorResponse.class) ])
  @ApiImplicitParams (  [
          @ApiImplicitParam(dataType = "string", paramType="query", name="state",value="state Id", required = true) ])*/

  def authorize(String code, String state) {
    def resp = linkedInService.getAccessToken(code)
    if (resp && !resp.error) {
      respond connect(state, new SocialCommand(clientId: "my-client", accessToken: resp.access_token, expiresIn: resp.expires_in))
      return
    }
    respond new InternalErrorResponse("Something went wrong.", [])
  }

  def connect(String state, SocialCommand cmd) {
    if (!cmd.validate()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse(cmd.errors)
      log.debug "cmd errors : " + cmd.errors
      return
    }
    def loggedInMember = securityService.currentMember
    if (!loggedInMember) {
      loggedInMember = Member.get(state)
    }
    def profile = linkedInService.getProfile(cmd.accessToken)
    if (!profile) {
      respond new SocialConnectErrorResponse("invalid accessToken.", [])
      return
    }
    def linkedInUser = SocialConnection.findByProviderIdAndProviderUserId('LinkedIn', 'ln-' + profile.id)
    def login
    def lnUser
    if (loggedInMember && linkedInUser) {
      def alreadyExistsMsg = ""
      if (linkedInUser.uid == loggedInMember.login.id) {
        alreadyExistsMsg = "LinkedIn account of ${profile.firstName} already connected with ${loggedInMember.firstName} Account."
      } else
        alreadyExistsMsg = "LinkedIn account of ${profile.firstName} already connected with another member."
      def map = [socialType: 'LinkedIn', email: profile.emailAddress]
      respond new SocialConnectErrorResponse(alreadyExistsMsg, [])
      return
    }
    if (linkedInUser) {
      linkedInUser.accessToken = cmd.accessToken
      linkedInUser.imageUrl = profile.pictureUrl
      linkedInUser.save(failOnError: true)
      login = Login.get(linkedInUser.uid)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    def profileUrl = profile.publicProfileUrl
    if (loggedInMember) {
      lnUser = new SocialConnection(uid: loggedInMember.login.id, accessToken: cmd.accessToken, providerId: "LinkedIn", providerUserId: "ln-" + profile.id,
        displayName: profile.firstName, imageUrl: profile.pictureUrl, profileUrl: profileUrl, login: loggedInMember.login, email: profile.emailAddress, expireTime: cmd.expiresIn)
      lnUser.save(failOnError: true)
      def map = [socialType: 'LinkedIn', email: profile.emailAddress]
      respond new SocialConnectResponse("Account linked to ${loggedInMember.firstName}", [])
      return
    }
    login = Login.findByUsername("ln-" + profile.id)
    if (login) {
      lnUser = new SocialConnection(uid: login.id, accessToken: cmd.accessToken, providerId: "LinkedIn", providerUserId: "ln-" + profile.id,
        displayName: profile.firstName, imageUrl: profile.pictureUrl, profileUrl: profileUrl, login: login, email: profile.emailAddress, expireTime: cmd.expiresIn)
      lnUser.save(failOnError: true)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    login = new Login(username: "ln-" + profile.id, enabled: true, password: 'CHANGE_ME')
    login.save(failOnError: true)

    lnUser = new SocialConnection(uid: login.id, accessToken: cmd.accessToken, providerId: "LinkedIn", providerUserId: "ln-" + profile.id,
      displayName: profile.firstName, imageUrl: profile.pictureUrl, profileUrl: profileUrl, login: login, email: profile.emailAddress, expireTime: cmd.expiresIn)
    lnUser.save(failOnError: true)
    Member m = new Member(login: login, email: profile.emailAddress, firstName: profile.firstName, lastName: profile.lastName)
    m.save(failOnError: true)
    OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
    def tokenApiModel = new TokenApiModel(token)
    def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
    respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
    return
  }

}
