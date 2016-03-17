package com.vellkare.core

import grails.transaction.Transactional
import org.apache.commons.math3.random.RandomDataGenerator

@Transactional
class RegistrationService {

  static transactional = false

  def mailService
  def grailsApplication


  @Transactional
  def registerMember(String firstName, String lastName, String email, String phoneNumber) {
    def registration = new Registration(firstName:firstName, lastName:lastName, email: email, phoneNumber: phoneNumber)
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
    if (grailsApplication.config.registration.verification.email) {
      mailService.sendMail {
        to registration.email
        from grailsApplication.config.grailsApplication.config.registration.verification.from
        subject message(code: 'email.registration.verification', args: [])
        def bodyArgs = [view: '/emails/registration/verification',
                        model: [registration: registration, verificationLink: verificationLink]]
        body(bodyArgs)
      }
      registration.sentEmailVerification()
    }
  }

  @Transactional
  def sendSMSVerification(Registration registration){

  }

  private void generateOTP(Registration registration){
    def otpSize = grailsApplication.config.grailsApplication.config.registration.verification.otpSize?:8
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
