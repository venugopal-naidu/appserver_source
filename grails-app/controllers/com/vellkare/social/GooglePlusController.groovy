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
class GooglePlusController extends SocialController {

  def googlePlusService
  def securityService
  def memberService

  @GET
  @Path("/googlePlus/login")
  @ApiOperation(value = "User Social Login", notes = "This is used get the login url for a social user.", tags = ["/v0/auth/social", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "social url successfully", response = SocialConnectUrlResponse.class),
    @ApiResponse(code = 404, message = "Invalid login details supplied.", response = SocialConnectErrorResponse.class), //https://github.com/swagger-api/swagger-ui/issues/1055
    @ApiResponse(code = 500, message = "Internal Error", response = InternalErrorResponse.class)])
  def login(String socialId) {
    if (socialId in grailsApplication.config.vellkare.auth.social.enabled) {
      def url = googlePlusService.getAuthorizeURL()
      respond new SocialConnectUrlResponse(url: url)
    } else {
      respond new SocialConnectErrorResponse("Social type is not supported.", [])
    }
  }

  /*@GET
  @Path("/googlePlus/authorize")
  @ApiOperation(value = "User Social authorize", notes = "This is used to authorize a social user. This is invoked by the social connection application.", tags=["/v0/auth/social","all.public"])
  @ApiResponses(value = [
          @ApiResponse(code = 200, message = "social authorization", response=SocialConnectUrlResponse.class),
          @ApiResponse(code = 404, message = "Invalid login details supplied.", response=SocialConnectErrorResponse.class), //https://github.com/swagger-api/swagger-ui/issues/1055
          @ApiResponse(code = 500, message = "Internal Error", response=InternalErrorResponse.class) ])
  @ApiImplicitParams (  [
          @ApiImplicitParam(dataType = "string", paramType="query", name="state",value="state Id", required = true) ])*/

  def authorize(String code, String state) {
    def resp = googlePlusService.getAccessToken(code)
    if (resp && !resp.error) {
      respond connect(state, new SocialCommand(clientId: "my-client", accessToken: resp.access_token, expiresIn: resp.expires_in))
      return
    }
    respond new InternalErrorResponse("Something went wrong.", [])
  }

  //state is not provided when using sdk
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
    def profile = googlePlusService.getProfile(cmd.accessToken)
    if (!profile) {
      respond new SocialConnectErrorResponse("invalid accessToken.", [])
      return
    }
    def googlePlusUser = SocialConnection.findByProviderIdAndProviderUserId('GooglePlus', 'gp-' + profile.id)
    def login
    def gpUser

    if (loggedInMember && googlePlusUser) {
      def alreadyExistsMsg = ""
      if (googlePlusUser.uid == loggedInMember.login.id) {
        alreadyExistsMsg = "GooglePlus user ${profile.name} or Email ${profile.email} already connected with your ${loggedInMember.firstName} Account."
      } else
        alreadyExistsMsg = "GooglePlus user ${profile.name} Email ${profile.email} already connected with another member."
      ArrayList results = new ArrayList()
      def map = [socialType: 'GooglePlus', email: profile.email]
      respond new SocialConnectErrorResponse(alreadyExistsMsg, [])
      return
    }
    if (googlePlusUser) {
      googlePlusUser.accessToken = cmd.accessToken
      googlePlusUser.imageUrl = profile.picture
      googlePlusUser.save(failOnError: true)
      login = Login.get(googlePlusUser.uid)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    def profileUrl = profile.link
    if (loggedInMember) {
      gpUser = new SocialConnection(uid: loggedInMember.login.id, accessToken: cmd.accessToken, providerId: "GooglePlus", providerUserId: "gp-" + profile.id,
        displayName: profile.name, imageUrl: profile.picture, profileUrl: profileUrl, login: loggedInMember.login, email: profile.email, expireTime: cmd.expiresIn)
      gpUser.save(failOnError: true)
      ArrayList results = new ArrayList()
      def map = [socialType: 'GooglePlus', email: profile.email]
      respond new SocialConnectResponse("Account linked to ${loggedInMember.firstName}", [])
      return
    }
    login = Login.findByUsername("gp-" + profile.id)
    if (login) {
      gpUser = new SocialConnection(uid: login.id, accessToken: cmd.accessToken, providerId: "GooglePlus", providerUserId: "gp-" + profile.id,
        displayName: profile.name, imageUrl: profile.picture, profileUrl: profileUrl, login: login, email: profile.email, expireTime: cmd.expiresIn)
      gpUser.save(failOnError: true)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    login = new Login(username: "gp-" + profile.id, enabled: true, password: 'CHANGE_ME')
    login.save(failOnError: true)

    gpUser = new SocialConnection(uid: login.id, accessToken: cmd.accessToken, providerId: "GooglePlus", providerUserId: "gp-" + profile.id,
      displayName: profile.name, imageUrl: profile.picture, profileUrl: profileUrl, login: login, email: profile.email, expireTime: cmd.expiresIn)
    gpUser.save(failOnError: true)
    Member m = new Member(login: login, email: profile.email, firstName: profile.given_name, lastName: profile.family_name)
    m.save(failOnError: true)
    OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
    def tokenApiModel = new TokenApiModel(token)
    def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
    respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
    return
  }

}
