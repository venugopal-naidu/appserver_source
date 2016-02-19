package com.vellkare.core

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vellkare.api.FieldErrorApiModel
import com.vellkare.api.InternalErrorResponse
import com.vellkare.api.SuccessResponse
import com.vellkare.api.ValidationErrorResponse
import com.vellkare.mail.PendingEmailConfirmation
import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import grails.validation.Validateable
import io.swagger.annotations.*
import org.apache.http.HttpStatus
import org.springframework.security.oauth2.common.OAuth2AccessToken

import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/v0/auth")
@Api(value = "auth", description = "Operations about Authentication")
@Produces(["application/json", "application/xml"])
class AuthController extends RestfulController {
  static responseFormats = ['json', 'xml']
  static allowedMethods = [login: 'POST', changePassword: 'POST', resendPassword: 'POST', resetPassword: 'POST', logout: 'GET']

  static namespace = 'v0'

  def securityService
  def passwordEncoder
  def springSecurityService
  def memberService
  def mailService
  def mailConfirmationService
  def transactionService
  def tokenStore

  @POST
  @Path("/login")
  @ApiOperation(value = "User Login", notes = "This is used to login a user.", tags = ["/v0/auth", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "User loggedin successfully", response = NewMemberApiModel.class),
    @ApiResponse(code = 404, message = "Invalid login details supplied.", response = ValidationErrorResponse.class, responseContainer = "List"), //https://github.com/swagger-api/swagger-ui/issues/1055
    @ApiResponse(code = 500, message = "Internal Error", response = InternalErrorResponse.class)])
  @ApiImplicitParams([
    @ApiImplicitParam(dataType = "com.vellkare.core.LoginCommand", paramType = "body", name = "LoginCommand")])
  @Transactional
  // TODO: OAuthTokenService.hasScope(token,".api.user:write")
  def login() {

    String authHeader = request.getHeader("Authorization");
    if (authHeader != null) {
      String tokenValue = authHeader.replace("Bearer", "").trim();
      OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
      tokenStore.removeAccessToken(accessToken);
    }
    LoginCommand cmd = new LoginCommand()
    bindData(cmd, request.getJSON())
    if (!cmd.validate()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse(cmd.errors)
      return
    }
    def className = grailsApplication.config.grails.plugin.springsecurity.oauthProvider.accessTokenLookup.className
    def aClass = grailsApplication.getDomainClass(className).clazz
    def obj = aClass.findByUsername(cmd.username)
    if (obj) {
      obj.delete(failOnError: true, flush: true)
    }

    try {
      if (passwordEncoder.isPasswordValid(Login.findByUsername(cmd.username).password, cmd.password, null)) {
        OAuth2AccessToken token = securityService.getToken(cmd.clientId, "password", cmd.username, cmd.password)
        def tokenApiModel = new TokenApiModel(token)
        def memberApiModel = memberService.findByUsername(cmd.username) as MemberApiModel
        respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)
      } else {
        response.setStatus(HttpStatus.SC_BAD_REQUEST)
        respond new ValidationErrorResponse([new FieldErrorApiModel('password', 'password.wrong', [])])
      }
      return
    } catch (e) {
      transactionService.rollback(transactionStatus)
      response.setStatus(500)
      log.error("unable to login:", e)
      e.printStackTrace()
      respond new InternalErrorResponse('internal.error', [])
    }

  }

  //forgot password
  @POST
  @Path("/forgotPassword")
  @ApiOperation(value = "forgot password", notes = "This is used for user when user forgets password", tags = ["/v0/auth", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "Password reset link sent successfully", response = SuccessResponse.class),
    @ApiResponse(code = 404, message = "Invalid details supplied", response = ValidationErrorResponse.class)])
  @ApiImplicitParams([
    @ApiImplicitParam(dataType = "com.vellkare.core.EmailCommand", paramType = "body", name = "EmailCommand")])
  @Transactional
  // TODO: OAuthTokenService.hasScope(token,".api.user:write")
  def resendPassword() {
    EmailCommand cmd = new EmailCommand()
    bindData(cmd, request.getJSON())
    if (cmd.email) {
      def member = Member.findByEmail(cmd.email)
      if (member) {
        def token = mailConfirmationService.sendConfirmation(
          [to     : member.email,
           event  : 'resetPassword',
           subject: 'Reset your password',
           view   : '/user/resetPasswordEmail',
           link   : "${grailsApplication.config.grails.serverURL}"])
        respond new SuccessResponse('email.password.reset.sent', [])
      } else {
        response.setStatus(HttpStatus.SC_BAD_REQUEST)
        respond new ValidationErrorResponse([new FieldErrorApiModel('email', 'email.notFound', [])])
      }
    } else {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('email', 'email.empty', [])])
    }
  }

  @POST
  @Path("/resetPassword")
  @ApiOperation(value = "Reset password", notes = "This is used for user to reset password", tags = ["/v0/auth", "all.private"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "Password changed successfully", response = SuccessResponse.class),
    @ApiResponse(code = 404, message = "Invalid details supplied", response = ValidationErrorResponse.class)])
  @ApiImplicitParams([
    @ApiImplicitParam(dataType = "com.vellkare.core.ResetPasswordCommand", paramType = "body", name = "ResetPasswordCommand")])
  @Transactional
  // TODO: OAuthTokenService.hasScope(token,".api.user:write")
  def resetPassword() {
    ResetPasswordCommand cmd = new ResetPasswordCommand()
    bindData(cmd, request.getJSON())
    def login
    def token = PendingEmailConfirmation.findByConfirmationToken(cmd.token)
    if (!token) {
      token = cmd.token.replaceAll("%2B", "+")
      token = token.replaceAll("%3D", "=")
      token = PendingEmailConfirmation.findByConfirmationToken(token)
    }
    if (token)
      login = ((Member) memberService.findByEmail(token.emailAddress)).login
    if (!login) {
      //404
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('token', 'email.token.expired', [])])
      return
    }
    def result = mailConfirmationService.checkConfirmation(token.confirmationToken)
    if (result.valid) {
      login.password = cmd.newPassword
      login.save(failOnError: true, flush: true)
      respond new SuccessResponse('password.changed', [])
    } else {
      //404, already used token
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('token', 'email.token.used', [])])
      return
    }
  }

  //user change password
  @POST
  @Path("/changePassword")
  @ApiOperation(value = "Change password", notes = "This is used for loggedin user to change password", response = SuccessResponse.class, tags = ["/v0/auth", "all.private"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "Password changed successfully", response = SuccessResponse.class),
    @ApiResponse(code = 403, message = "UnAuthorized"),
    @ApiResponse(code = 404, message = "Invalid details supplied", response = ValidationErrorResponse.class)])
  @ApiImplicitParams([
    @ApiImplicitParam(dataType = "com.vellkare.core.ChangePasswordCommand", paramType = "body", name = "ChangePasswordCommand")])
  @Transactional
  @Secured("#oauth2.isUser()")
  public changePassword() {
    ChangePasswordCommand cmd = new ChangePasswordCommand()
    bindData(cmd, request.getJSON())
    def m = securityService.currentMember
    if (!cmd.validate()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse(cmd.errors)
      return
    }
    def login = m.login
    if (passwordEncoder.isPasswordValid(login.password, cmd.password, null)) {
      if (passwordEncoder.isPasswordValid(login.password, cmd.newPassword, null)) {
        response.setStatus(HttpStatus.SC_BAD_REQUEST)
        respond new ValidationErrorResponse([new FieldErrorApiModel('newPassword', 'password.old.new.same', [])])
        return
      } else {
        login.password = cmd.newPassword
        login.save(flush: true, failOnError: true)
        respond new SuccessResponse('password.changed', [])
        return
      }
    } else {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('password', 'password.old.incorrect', [])])
      return
    }
  }

  @GET
  @Path("/logout")
  @ApiOperation(value = "User Logout", notes = "This is used for logout user", response = SuccessResponse.class, tags = ["/v0/auth", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "Successfully logged out", response = SuccessResponse.class),
    @ApiResponse(code = 403, message = "UnAuthorized")])
  @Transactional
  @Secured("#oauth2.isUser()")
  // TODO: OAuthTokenService.hasScope(token,".api.user:write")
  def logout() {
    def m = securityService.currentMember
    def className = grailsApplication.config.grails.plugin.springsecurity.oauthProvider.accessTokenLookup.className
    def aClass = grailsApplication.getDomainClass(className).clazz
    def obj = aClass.findByUsername(m.login.username)
    if (obj) {
      obj.delete(failOnError: true, flush: true)
    }
    respond new SuccessResponse('member.logout.success', [])
    return
  }
}

@Validateable
@JsonIgnoreProperties(["properties", "errors"])
class LoginCommand {
  String clientId
  String username
  String password

  static constraints = {
    clientId nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("clientId", "clientId.empty")
      }
    }
    username nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("username", "username.empty")
      }
      if (val) {
        if (!Login.findByUsername(val)) {
          errors.rejectValue("username", "username.doesnotexists")
        }
      }
    }
    password nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("password", "password.empty")
      }

    }
  }
}

@Validateable
@JsonIgnoreProperties(["properties", "errors"])
class ChangePasswordCommand {
  String password
  String newPassword

  static constraints = {
    password nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("password", "password.empty")
      }
    }
    newPassword nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("newPassword", "newPassword.empty")
      }
    }
  }
}

@Validateable
@JsonIgnoreProperties(["properties", "errors"])
class ResetPasswordCommand {
  String newPassword
  String token

  static constraints = {
    newPassword nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("newPassword", "newPassword.empty")
      }
    }
    token nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("token", "token.empty")
      }
    }
  }
}

@Validateable
@JsonIgnoreProperties(["properties", "errors"])
class EmailCommand {
  String email

  static constraints = {
    email nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("email", "email.empty")
      }
    }
  }
}