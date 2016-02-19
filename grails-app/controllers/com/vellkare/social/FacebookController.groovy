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
class FacebookController extends SocialController {

  def facebookService
  def securityService
  def memberService

  @GET
  @Path("/facebook/login")
  @ApiOperation(value = "User Social Login", notes = "This is used get the login url for a social user.", tags = ["/v0/auth/social", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "social url successfully", response = SocialConnectUrlResponse.class),
    @ApiResponse(code = 404, message = "Invalid login details supplied.", response = SocialConnectErrorResponse.class), //https://github.com/swagger-api/swagger-ui/issues/1055
    @ApiResponse(code = 500, message = "Internal Error", response = InternalErrorResponse.class)])
  def login(String socialId) {
    def url = facebookService.getAuthorizeURL()
    respond new SocialConnectUrlResponse(url: url)
  }

  /*@GET
  @Path("/facebook/authorize")
  @ApiOperation(value = "User Social authorize", notes = "This is used to authorize a social user. This is invoked by the social connection application.", tags=["/v0/auth/social","all.public"])
  @ApiResponses(value = [
          @ApiResponse(code = 200, message = "social authorization", response=SocialConnectUrlResponse.class),
          @ApiResponse(code = 404, message = "Invalid login details supplied.", response=SocialConnectErrorResponse.class), //https://github.com/swagger-api/swagger-ui/issues/1055
          @ApiResponse(code = 500, message = "Internal Error", response=InternalErrorResponse.class) ])
  @ApiImplicitParams (  [
          @ApiImplicitParam(dataType = "string", paramType="query", name="state",value="state Id", required = true) ])*/

  def authorize(String code, String state) {
    def resp = facebookService.getAccessToken(code)
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
    def profile = facebookService.getProfile(cmd.accessToken)
    if (!profile) {
      respond new SocialConnectErrorResponse("invalid accessToken.", [])
      return
    }
    def facebookUser = SocialConnection.findByProviderIdAndProviderUserId('Facebook', 'fb-' + profile.id)
    def login
    def fbUser

    if (loggedInMember && facebookUser) {
      def alreadyExistsMsg = ""
      if (facebookUser.uid == loggedInMember.login.id) {
        alreadyExistsMsg = "Facebook account of ${profile.name} already connected with your ${loggedInMember.firstName} Account."
      } else
        alreadyExistsMsg = "Facebook account of ${profile.name} already connected with another member."
      def map = [socialType: 'Facebook', email: profile.email]
      respond new SocialConnectErrorResponse(alreadyExistsMsg, [])
      return
    }

    def profileUrl = facebookService.GRAPH_URL + "?id=" + profile.id
    def imageUrl = facebookService.getProfileImageUrl(profile.id)

    if (facebookUser) {
      facebookUser.accessToken = cmd.accessToken
      facebookUser.imageUrl = imageUrl
      facebookUser.save(failOnError: true)
      login = Login.get(facebookUser.uid)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    if (loggedInMember) {
      fbUser = new SocialConnection(uid: loggedInMember.login.id, accessToken: cmd.accessToken, providerId: "Facebook", providerUserId: "fb-" + profile.id,
        displayName: profile.name, imageUrl: imageUrl, profileUrl: profileUrl, login: loggedInMember.login, email: profile.email, expireTime: cmd.expiresIn)
      fbUser.save(failOnError: true)
      def map = [socialType: 'Facebook', email: profile.email]
      respond new SocialConnectResponse("Account linked to ${loggedInMember.firstName}", [])
      return
    }
    login = Login.findByUsername("fb-" + profile.id)
    if (login) {
      fbUser = new SocialConnection(uid: login.id, accessToken: cmd.accessToken, providerId: "Facebook", providerUserId: "fb-" + profile.id,
        displayName: profile.name, imageUrl: imageUrl, profileUrl: profileUrl, login: login, email: profile.email, expireTime: cmd.expiresIn)
      fbUser.save(failOnError: true)
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      return
    }
    login = new Login(username: "fb-" + profile.id, enabled: true, password: 'CHANGE_ME')
    login.save(failOnError: true)

    fbUser = new SocialConnection(uid: login.id, accessToken: cmd.accessToken, providerId: "Facebook", providerUserId: "fb-" + profile.id,
      displayName: profile.name, imageUrl: imageUrl, profileUrl: profileUrl, login: login, email: profile.email, expireTime: cmd.expiresIn)
    fbUser.save(failOnError: true)
    Member m = new Member(login: login, email: profile.email, firstName: profile.first_name ?: profile.name, lastName: profile.last_name)
    m.save()
    OAuth2AccessToken token = securityService.getToken(cmd.clientId, null, login.username, null)
    def tokenApiModel = new TokenApiModel(token)
    def memberApiModel = memberService.findByUsername(login.username) as MemberApiModel
    respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
    return
  }


}
