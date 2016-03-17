package com.vellkare.core

import com.vellkare.api.FieldErrorApiModel
import com.vellkare.api.ValidationErrorResponse
import com.vellkare.social.SocialConnection
import org.apache.http.HttpStatus

class RegistrationController {

  static responseFormats = ['json', 'xml']
  static namespace = 'v0'

  def registrationService
  def grailsApplication


  def register(RegisterCommand cmd) {
    if (!cmd.validate()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse(cmd.errors)
      log.debug "cmd errors : " + cmd.errors
      return
    }

    def existingRegistration = Registration.findByEmail(cmd.email)
    if (existingRegistration) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('email', 'email.registered', [])])
      return
    }

    Registration registration =  registrationService.registerMember(cmd.firstName, cmd.lastName, cmd.email, cmd.phoneNumber)
    String successMessage = "User Registered Successfully. Please verify your phone number and email by entering OTP number."
    boolean emailVerificationEnabled = grailsApplication.config.registration.verification.email
    boolean phoneNumberVerificationEnabled = grailsApplication.config.registration.verification.phone
    respond(registration: "success", message: successMessage,
      emailVerificationEnabled: emailVerificationEnabled, phoneNumberVerificationEnabled: phoneNumberVerificationEnabled,
      phoneNumber: cmd.phoneNumber, email: cmd.email, firstName: cmd.firstName, lastName: cmd.lastName,
      uid:registration.uuid
    )
  }

  def verifyUid(String uid){
    def existingRegistration = Registration.findByUuid(uid)
    if (existingRegistration ){
      respond( isValidUid:true, isUserRegistered:existingRegistration.member?true:false )
    }else{
      respond( isValidUid:false, isUserRegistered:false )
    }
    return
  }

}

class RegisterCommand {
  String email
  String firstName
  String lastName
  String phoneNumber

  static constraints = {
    email email: true, nullable: false
    phoneNumber nullable: false, minSize: 10
    lastName nullable: true
    firstName nullable: false, minSize: 3
  }

}
