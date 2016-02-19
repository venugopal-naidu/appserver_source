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
class InstagramController extends SocialController {

  def instagramService
  def securityService
  def memberService

  @GET
  @Path("/instagram/login")
  @ApiOperation(value = "User Social Login", notes = "This is used get the login url for a social user.", tags = ["/v0/auth/social", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "social url successfully", response = SocialConnectUrlResponse.class),
    @ApiResponse(code = 404, message = "Invalid login details supplied.", response = SocialConnectErrorResponse.class), //https://github.com/swagger-api/swagger-ui/issues/1055
    @ApiResponse(code = 500, message = "Internal Error", response = InternalErrorResponse.class)])
  def login(String socialId) {
    if (socialId in grailsApplication.config.vellkare.auth.social.enabled) {
      def url = instagramService.getAuthorizeURL()
      respond new SocialConnectUrlResponse(url: url)
    } else {
      respond new SocialConnectErrorResponse("Social type is not supported.", [])
    }
  }

  /*@GET
  @Path("/instagram/authorize")
  @ApiOperation(value = "User Social authorize", notes = "This is used to authorize a social user. This is invoked by the social connection application.", tags=["/v0/auth/social","all.public"])
  @ApiResponses(value = [
          @ApiResponse(code = 200, message = "social authorization", response=SocialConnectUrlResponse.class),
          @ApiResponse(code = 404, message = "Invalid login details supplied.", response=SocialConnectErrorResponse.class), //https://github.com/swagger-api/swagger-ui/issues/1055
          @ApiResponse(code = 500, message = "Internal Error", response=InternalErrorResponse.class) ])
  @ApiImplicitParams (  [
          @ApiImplicitParam(dataType = "string", paramType="query", name="state",value="state Id", required = true) ])*/

  def authorize(String code, String state) {
    def resp = instagramService.getAccessToken(code)
    if (resp && !resp.error_type) {
      respond connect(state, new SocialCommand(clientId: "my-client", accessToken: resp.access_token))
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
    def profile = instagramService.getProfile(cmd.accessToken)
    if (!profile) {
      respond new SocialConnectErrorResponse("invalid accessToken.", [])
      return
    }
    profile = profile.data
    def instagramUser = SocialConnection.findByProviderIdAndProviderUserId('Instagram', 'in-' + profile.id)
    def login
    def inUser

    if (loggedInMember && instagramUser) {
      def alreadyExistsMsg = ""
      if (instagramUser.uid == loggedInMember.login.id) {
        alreadyExistsMsg = "Instagram account of ${profile.username} already connected with your ${loggedInMember.firstName} Account."
      } else
        alreadyExistsMsg = "Instagram account of ${profile.username} already connected with another member."
      def map = [socialType: 'Instagram', email: profile.email]
      respond new SocialConnectErrorResponse(alreadyExistsMsg, [])
      return
    }
    if (instagramUser) {
      instagramUser.accessToken = cmd.accessToken
      instagramUser.imageUrl = profile.profile_picture
      instagramUser.save(failOnError: true)
      login = Login.get(instagramUser.uid)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    def profileUrl = "https://instagram.com/" + profile.username
    //profile url and email are not provided by instagram
    if (loggedInMember) {
      inUser = new SocialConnection(uid: loggedInMember.login.id, accessToken: cmd.accessToken, providerId: "Instagram", providerUserId: "in-" + profile.id,
        displayName: profile.username, imageUrl: profile.profile_picture, profileUrl: profileUrl, login: loggedInMember.login, email: profile.email, expireTime: cmd.expiresIn)
      inUser.save(failOnError: true)
      def map = [socialType: 'Instagram', email: profile.email]
      respond new SocialConnectResponse("Account linked to ${loggedInMember.firstName}", [])
      return
    }
    login = Login.findByUsername("in-" + profile.id)
    if (login) {
      inUser = new SocialConnection(uid: login.id, accessToken: cmd.accessToken, providerId: "Instagram", providerUserId: "in-" + profile.id,
        displayName: profile.username, imageUrl: profile.profile_picture, profileUrl: profileUrl, login: login, email: profile.email, expireTime: cmd.expiresIn)
      inUser.save(failOnError: true)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    login = new Login(username: "in-" + profile.id, enabled: true, password: 'CHANGE_ME')
    login.save(failOnError: true)

    inUser = new SocialConnection(uid: login.id, accessToken: cmd.accessToken, providerId: "Instagram", providerUserId: "in-" + profile.id,
      displayName: profile.username, imageUrl: profile.profile_picture, profileUrl: profileUrl, login: login, email: profile.email, expireTime: cmd.expiresIn)
    inUser.save(failOnError: true)
    //mmeber.fullName is required, so uesrname is given (which will have value) and not full_name (which is optional in instagram)
    Member m = new Member(login: login, email: profile.email, firstName: profile.username, lastName: null)
    m.save(failOnError: true)
    OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
    def tokenApiModel = new TokenApiModel(token)
    def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
    respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
    return
  }
}
