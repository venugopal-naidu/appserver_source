package com.vellkare.core

import com.vellkare.api.FieldErrorApiModel
import com.vellkare.api.ValidationErrorResponse
import org.apache.http.HttpStatus
import org.springframework.security.oauth2.common.OAuth2AccessToken

class RegistrationController {

  static responseFormats = ['json', 'xml']
  static namespace = 'v0'

  def registrationService
  def grailsApplication
  def memberService
  def securityService


  def register(RegisterCommand cmd) {
    if (!cmd.validate()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse(cmd.errors)
      log.debug "cmd errors : " + cmd.errors
      return
    }

    if (!cmd.tncChecked) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('tncChecked', 'tncChecked.notAccepted', [])])
      return
    }
    def existingRegistration = Registration.findByEmail(cmd.email)
    if (existingRegistration) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('email', 'email.registered', [])])
      return
    }

    Registration registration = registrationService.registerMember(cmd.firstName, cmd.lastName, cmd.email,
      cmd.phoneNumber, cmd.tncChecked)
    String successMessage = "User Registered Successfully. Please verify your phone number and email by entering OTP number."
    boolean emailVerificationEnabled = grailsApplication.config.registration.verification.email
    boolean phoneNumberVerificationEnabled = grailsApplication.config.registration.verification.phone
    respond(registration: "success", message: successMessage,
      emailVerificationEnabled: emailVerificationEnabled, phoneNumberVerificationEnabled: phoneNumberVerificationEnabled,
      phoneNumber: cmd.phoneNumber, email: cmd.email, firstName: cmd.firstName, lastName: cmd.lastName,
      uid: registration.uuid
    )
  }

  def verifyUid(VerifyRegisterCommand cmd) {
    if (!cmd.validate()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse(cmd.errors)
      log.debug "cmd errors : " + cmd.errors
      return
    }

    def existingRegistration = Registration.findByUuid(cmd.uid)
    if (existingRegistration) {
      respond(isValidUid: true, isUserRegistered: existingRegistration.member ? true : false)
    } else {
      respond(isValidUid: false, isUserRegistered: false)
    }
    return
  }

  def confirmRegistration(ConfirmRegistrationCommand  cmd) {
    if (!cmd.validate()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse(cmd.errors)
      log.debug "cmd errors : " + cmd.errors
      return
    }

    Registration registration = Registration.findByUuid(cmd.uid);
    if(!registration || !registration.verificationCode.equals(cmd.otp)){
      if(registration){
        registration.invalidUuidVerification()
      }
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('uid', 'uid.invalid', [])])
      return
    }

    if(registration.member){
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('uid', 'member.already.registered', [])])
      return
    }

    Member m = memberService.createMember(registration, cmd.password)
    registration.verificationSuccessful(true,true) // setting both verification as true as the otp is save in both the cases
    m.addToRoles(MemberRole.create(m, Role.findByAuthority(Role.Authority.ROLE_USER)))

    OAuth2AccessToken token = securityService.getToken(grailsApplication.config.grails.client.config.clientId,
      "password", m.login.username, cmd.password)
    def tokenApiModel = new TokenApiModel(token)
    def memberApiModel = memberService.findByUsername(m.login.username) as MemberApiModel

    respond new NewMemberApiModel(token: tokenApiModel, member: memberApiModel)

  }

}

class ConfirmRegistrationCommand  {
  String uid
  String otp
  String password

  static constraints = {
    uid nullable: false
    otp nullable: false
    password nullable: false , minSize: 6
  }
}
class VerifyRegisterCommand {
  String uid
  static constraints = {
    uid nullable: false
  }
}
class RegisterCommand {
  String email
  String firstName
  String lastName
  String phoneNumber
  Boolean tncChecked

  static constraints = {
    email email: true, nullable: false
    phoneNumber nullable: false, minSize: 10
    lastName nullable: true
    firstName nullable: false, minSize: 3
  }

}
