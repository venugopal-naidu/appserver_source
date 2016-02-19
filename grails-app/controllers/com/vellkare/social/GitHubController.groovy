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
class GitHubController extends SocialController {

  def gitHubService
  def securityService
  def memberService

  @GET
  @Path("/gitHub/login")
  @ApiOperation(value = "User Social Login", notes = "This is used get the login url for a social user.", tags = ["/v0/auth/social", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "social url successfully", response = SocialConnectUrlResponse.class),
    @ApiResponse(code = 404, message = "Invalid login details supplied.", response = SocialConnectErrorResponse.class), //https://github.com/swagger-api/swagger-ui/issues/1055
    @ApiResponse(code = 500, message = "Internal Error", response = InternalErrorResponse.class)])
  def login(String socialId) {
    if (socialId in grailsApplication.config.vellkare.auth.social.enabled) {
      def url = gitHubService.getAuthorizeURL()
      respond new SocialConnectUrlResponse(url: url)
    } else {
      respond new SocialConnectErrorResponse("Social type is not supported.", [])
    }
  }

  /*@GET
  @Path("/gitHub/authorize")
  @ApiOperation(value = "User Social authorize", notes = "This is used to authorize a social user. This is invoked by the social connection application.", tags=["/v0/auth/social","all.public"])
  @ApiResponses(value = [
          @ApiResponse(code = 200, message = "social authorization", response=SocialConnectUrlResponse.class),
          @ApiResponse(code = 404, message = "Invalid login details supplied.", response=SocialConnectErrorResponse.class), //https://github.com/swagger-api/swagger-ui/issues/1055
          @ApiResponse(code = 500, message = "Internal Error", response=InternalErrorResponse.class) ])
  @ApiImplicitParams (  [
          @ApiImplicitParam(dataType = "string", paramType="query", name="state",value="state Id", required = true) ])*/

  def authorize(String code, String state) {
    def resp = gitHubService.getAccessToken(code)
    if (resp && !resp.error) {
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
    def profile = gitHubService.getProfile(cmd.accessToken)
    if (!profile) {
      respond new SocialConnectErrorResponse("invalid accessToken.", [])
      return
    }
    def gitHubUser = SocialConnection.findByProviderIdAndProviderUserId('GitHub', 'gh-' + profile.id)
    def login
    def ghUser

    if (loggedInMember && gitHubUser) {
      def alreadyExistsMsg = ""
      if (gitHubUser.uid == loggedInMember.login.id) {
        alreadyExistsMsg = "GitHub account of ${profile.login} already connected with your ${loggedInMember.firstName} Account."
      } else
        alreadyExistsMsg = "GitHub account of ${profile.login} already connected with another member."
      def map = [socialType: 'GitHub', email: profile.email]
      respond new SocialConnectErrorResponse(alreadyExistsMsg, [])
      return
    }
    if (gitHubUser) {
      gitHubUser.accessToken = cmd.accessToken
      gitHubUser.imageUrl = profile.avatar_url
      gitHubUser.save(failOnError: true)
      login = Login.get(gitHubUser.uid)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    def profileUrl = profile.html_url
    if (loggedInMember) {
      ghUser = new SocialConnection(uid: loggedInMember.login.id, accessToken: cmd.accessToken, providerId: "GitHub", providerUserId: "gh-" + profile.id,
        displayName: profile.login, imageUrl: profile.pictureUrl, profileUrl: profileUrl, login: loggedInMember.login, email: profile.email, expireTime: cmd.expiresIn)
      ghUser.save(failOnError: true)
      def map = [socialType: 'GitHub', email: profile.email]
      respond new SocialConnectResponse("Account linked to ${loggedInMember.firstName}", [])
      return
    }
    login = Login.findByUsername("gh-" + profile.id)
    if (login) {
      ghUser = new SocialConnection(uid: login.id, accessToken: cmd.accessToken, providerId: "GitHub", providerUserId: "gh-" + profile.id,
        displayName: profile.login, imageUrl: profile.pictureUrl, profileUrl: profileUrl, login: login, email: profile.email, expireTime: cmd.expiresIn)
      ghUser.save(failOnError: true)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    login = new Login(username: "gh-" + profile.id, enabled: true, password: 'CHANGE_ME')
    login.save(failOnError: true)

    ghUser = new SocialConnection(uid: login.id, accessToken: cmd.accessToken, providerId: "GitHub", providerUserId: "gh-" + profile.id,
      displayName: profile.login, imageUrl: profile.pictureUrl, profileUrl: profileUrl, login: login, email: profile.email, expireTime: cmd.expiresIn)
    ghUser.save(failOnError: true)
    Member m = new Member(login: login, email: profile.email, firstName: profile.login, lastName: null)
    m.save(failOnError: true)
    OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
    def tokenApiModel = new TokenApiModel(token)
    def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
    respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
    return
  }

}
