package com.vellkare.core

import grails.transaction.Transactional
import org.apache.commons.math3.random.RandomDataGenerator

@Transactional
class RegistrationService {

  static transactional = false

  def mailService
  def grailsApplication
  def contentService


  @Transactional
  def registerMember(String firstName, String lastName, String email, String phoneNumber, Boolean tncChecked) {
    def registration = new Registration(firstName:firstName, lastName:lastName, email: email,
      phoneNumber: phoneNumber,tncChecked:tncChecked)
    registration.save(flush: true, failOnError: true)
    generateOTP(registration)
    sendVerificationEmail(registration)
    sendSMSVerification(registration)
    registration
  }

  @Transactional
  def sendVerificationEmail(Registration registration){
    def cfg = grailsApplication.config
    String verificationLink = "${cfg.grails.serverURL}${cfg.registration.verification.verifyController}?uuid=${registration.uuid}"
    if (grailsApplication.config.app.emails.otpVerification) {
      mailService.sendMail {
        to  cfg.grails?.email?.mock?.toAddress?:registration.email
        subject 'verification email'
        def bodyArgs = [view: '/emails/registration/verification',
                        model: [registration: registration, verificationLink: verificationLink]]
        def mailHtml = contentService.applyTemplate(bodyArgs.view, bodyArgs.model)
        html(mailHtml)
      }
      registration.sentEmailVerification()
    }
  }

  @Transactional
  def sendSMSVerification(Registration registration){

  }

  private void generateOTP(Registration registration){
    def otpSize = grailsApplication.config.verification.otp.size?:8
    RandomDataGenerator rdg = new RandomDataGenerator()
    registration.verificationCode = rdg.nextSecureHexString(otpSize)
  }

  @Transactional
  def resentVerificationCode(){

  }

  @Transactional
  def verifyVerificationCode(){

  }


}
