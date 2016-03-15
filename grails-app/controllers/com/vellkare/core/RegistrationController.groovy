package com.vellkare.core

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

    Registration registration =  registrationService.registerMember(cmd.firstName, cmd.lastName, cmd.email, cmd.phoneNumber)
    String successMessage = "User Registered Successfully. Please verify your phone number and email by entering OTP number."
    boolean emailVerificationEnabled = grailsApplication.config.registration.verification.email
    boolean phoneNumberVerificationEnabled = grailsApplication.config.registration.verification.phone
    respond(registration: "success", message: successMessage,
      emailVerificationEnabled: emailVerificationEnabled, phoneNumberVerificationEnabled: phoneNumberVerificationEnabled,
      phoneNumber: cmd.phoneNumber, email: cmd.email, firstName: cmd.firstName, lastName: cmd.lastName)

  }
}

class RegisterCommand {
  String email
  String firstName
  String lastName
  String phoneNumber

  static constraints = {
    email email: true, nullable: false, validator: { val, cmd, errors ->
      Object[] args = new Object[1]
      args[0] = val
      if (val && !errors) {
        def registration = Registration.findByEmail(val)
        if (registration) {
          errors.rejectValue("email", "user.email.exists", args, null)
        } else if (SocialConnection.findByEmail(val)) {
          errors.rejectValue("email", "user.email.exists", args, null)
        }
      }
    }
    phoneNumber nullable: false, minSize: 10
    lastName nullable: true
    firstName nullable: false, minSize: 3
  }

}
