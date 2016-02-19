package com.vellkare.core

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vellkare.api.InternalErrorResponse
import com.vellkare.api.NotFoundResponse
import com.vellkare.api.ValidationErrorResponse
import com.vellkare.social.SocialConnection
import grails.converters.JSON
import grails.transaction.Transactional
import grails.validation.Validateable
import io.swagger.annotations.*
import org.apache.http.HttpStatus
import org.springframework.security.oauth2.common.OAuth2AccessToken

import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/v0/users")
@Api(value = "users", description = "Operations about users")
@Produces(["application/json", "application/xml"])
class UserController {
  static responseFormats = ['json', 'xml']
  static allowedMethods = [profile: 'GET', update: 'PUT', create: 'POST']


  static namespace = 'v0'

  def transactionService
  def memberService
  def mailService
  def securityService
  def grailsApplication

  UserController() {
    //super(Member)
  }

  def profile() {
    try {
      def member = securityService.getCurrentMember()
      if (!member) {
        response.setStatus(HttpStatus.SC_NOT_FOUND)
        respond new NotFoundResponse('member.notfound', [id])
        return
      }
      render (member.propertyValueMap() as JSON)
      return
    }
    catch (e) {
      log.error("get profile error : ", e)
      response.setStatus(500)
      respond new InternalErrorResponse('internal.error', [])
    }
  }

  @PUT
  @Path("/update")
  @ApiOperation(value = "Update user", notes = "This can only be done by the logged in user.", response = MemberApiModel.class, tags = ["/v0/users", "all.private"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "Member updated", response = MemberApiModel.class),
    @ApiResponse(code = 400, message = "Invalid username supplied"),
    @ApiResponse(code = 404, message = "User not found", response = NotFoundResponse.class),
    @ApiResponse(code = 500, message = "Internal Error", response = InternalErrorResponse.class)])
  @ApiImplicitParams([
    @ApiImplicitParam(dataType = "long", paramType = "path", name = "userId", value = "Member Id"),
    @ApiImplicitParam(dataType = "com.vellkare.core.MemberProfileCommand", paramType = "body", name = "MemberCommand")])
  @Transactional
  // TODO: OAuthTokenService.hasScope(token,".api.user:write")
  def update(MemberProfileCommand cmd) {
    try {
      if (!cmd.validate()) {
        response.setStatus(HttpStatus.SC_BAD_REQUEST)
        respond new ValidationErrorResponse(cmd.errors)
        log.debug "cmd errors : " + cmd.errors
        return
      }

      def member = securityService.getCurrentMember()
      if (!member) {
        response.setStatus(HttpStatus.SC_NOT_FOUND)
        respond new NotFoundResponse('member.notfound', [])
        return
      }
      memberService.updateMember(member, cmd)
      render (member.propertyValueMap() as JSON)
      //respond member as MemberApiModel
      return
    } catch (e) {
      log.error("update profile error : ${e.getMessage()}", e)
      transactionService.rollback(transactionStatus)
      response.setStatus(500)
      respond new InternalErrorResponse('internal.error', [])
    }
  }

  @POST
  @Path("/signup")
  @ApiOperation(value = "User Signup", notes = "This is used to register.", response = NewMemberApiModel.class, tags = ["/v0/users", "all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "User registered", response = NewMemberApiModel.class),
    @ApiResponse(code = 404, message = "Invalid details supplied.", response = ValidationErrorResponse.class, responseContainer = "List"), //https://github.com/swagger-api/swagger-ui/issues/1055
    @ApiResponse(code = 500, message = "Internal Error", response = InternalErrorResponse.class)])
  @ApiImplicitParams([
    @ApiImplicitParam(dataType = "com.vellkare.core.MemberRegistrationCommand", paramType = "body", name = "MemberRegistrationCommand")])
  @Transactional
  def create(MemberRegistrationCommand cmd) {
    try {
      if (!cmd.validate()) {
        response.setStatus(HttpStatus.SC_BAD_REQUEST)
        respond new ValidationErrorResponse(cmd.errors)
        return
      }
      Member m = cmd.createMember()
      m.addToRoles(MemberRole.create(m, Role.findByAuthority(Role.Authority.ROLE_USER)))
      if (grailsApplication.config.grails?.email?.rule?.welcomeEmail && m.email) {
        mailService.sendMail {
          to m.email
          from grailsApplication.config.grails.mail.default.from
          subject message(code: 'email.member.welcome', args: [])
          def bodyArgs = [view: '/emails/member/welcome', model: [member: m]]
          body(bodyArgs)
        }
      }
      OAuth2AccessToken token = securityService.getToken(cmd.clientId, "password", cmd.username, cmd.password)
      def tokenApiModel = new TokenApiModel(token)
      def memberApiModel = memberService.findByUsername(cmd.username) as MemberApiModel
      respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)

    } catch (e) {
      transactionService.rollback(transactionStatus)
      response.setStatus(500)
      log.error("Signup Internal Error : ${e.getMessage()}", e)
      respond new InternalErrorResponse('internal.error', [])
    }
  }
}

@Validateable
class AddressCommand{
  String addressLine1
  String addressLine2
  String addressLine3
  String addressLine4
  String city
  String state
  String country
  String postalCode

  static constraints = {
    addressLine1 nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("addressLine1", "addressLine1.empty", "empty address line 1")
      }
    }

    addressLine2 nullable: true
    addressLine3 nullable: true
    addressLine4 nullable: true
    postalCode nullable: true

    city nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (!val) {
        errors.rejectValue("city", "city.empty", args, null)
      }
    }

    state nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (!val) {
        errors.rejectValue("state", "state.empty", args, null)
      }
    }

    country nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (!val) {
        errors.rejectValue("country", "country.empty", args, null)
      }
    }



  }
}
@Validateable
class MemberProfileCommand {

  def securityService

  String firstName
  String lastName
  String description
  String title
  Gender gender

  String primaryPhone
  String secondaryPhone
  String fax
  String email

  AddressCommand permanentAddress
  AddressCommand currentAddress

  static constraints = {
    lastName nullable: true
    description nullable: true
    title nullable: true
    gender nullable: true
    secondaryPhone nullable: true
    fax nullable: true
    permanentAddress nullable: false
    currentAddress nullable: true

    firstName nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("firstName", "firstName.empty", "empty firstName")
      }
    }


    primaryPhone nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (!val) {
        errors.rejectValue("primaryPhone", "primaryPhone.null", args, null)
      }
    }
    email nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (val) {
        if (!(val.indexOf('@') >= 0) || !(val.indexOf(".") >= 0)) {
          errors.rejectValue("email", "user.email.wrongformat", args, null)
        } else {
          def member = Member.findByEmail(val)
          def currentMember = cmd.securityService.getCurrentMember()
          if (member && member != currentMember) {
            errors.rejectValue("email", "user.email.exists", args, null)
          }
        }
      }
    }
  }
}

@Validateable
@JsonIgnoreProperties(["properties", "errors"])
class MemberCommand {
  Long id
  String firstName
  String lastName
  Long primaryPhone
  String description


  static constraints = {
    // without nullable:true, the generic validation is going to reject without entering the validator
    firstName nullable: true, validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("firstName", "firstName.empty", "empty firstName")
      }
    }
    lastName nullable: true
    primaryPhone nullable: true
    description nullable: true
  }
}

@Validateable
@JsonIgnoreProperties(["properties", "errors"])
class MemberRegistrationCommand {
  String clientId
  String username
  String email
  String password
  String firstName
  String lastName
  String phone
  static constraints = {
    clientId nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (!val) {
        errors.rejectValue("clientId", "clientId.empty", args, null)
      }
    }
    username nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (!val) {
        errors.rejectValue("username", "user.username.null", args, null)
      } else if (Login.findByUsername(val)) {
        errors.rejectValue("username", "user.username.exists", args, null)
      }
    }
    email nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (val) {
        if (!(val.indexOf('@') >= 0) || !(val.indexOf(".") >= 0)) {
          errors.rejectValue("email", "user.email.wrongformat", args, null)
        } else {
          def member = Member.findByEmail(val)
          if (member) {
            errors.rejectValue("email", "user.email.exists", args, null)
          } else if (SocialConnection.findByEmail(val)) {
            errors.rejectValue("email", "user.email.exists", args, null)
          }
        }
      }
    }
    password nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (!val) {
        errors.rejectValue("password", "user.password.null", args, null)
      }
    }
    firstName nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (!val) {
        errors.rejectValue("firstName", "user.firstName.null", args, null)
      }
    }
    lastName nullable: true
    phone nullable: true, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (!val) {
        errors.rejectValue("phone", "user.phone.null", args, null)
      }
    }
  }

  def createMember() {
    Login login = new Login(username: username, password: password, enabled: true)
    login.save(failOnError: true, flush: true)
    Member m = new Member(login: login, firstName: firstName, lastName: lastName, email: email, primaryPhone: phone)
    m.save(failOnError: true, flush: true)
  }
}