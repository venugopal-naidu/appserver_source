package com.vellkare.core

class Registration {
  Boolean tncChecked = false
  String email
  String firstName
  String lastName
  String phoneNumber
  String uuid
  Date registrationDate = new Date()
  boolean emailVerified = false
  boolean phoneNumberVerified = false
  String verificationCode
  int emailVerificationSentCount = 0
  int phoneVerificationSentCount = 0
  Date lastEmailVerificationSentDate
  Date lastPhoneVerificationSentDate

  RegistrationStatus verificationStatus = RegistrationStatus.NEW

  Date verificationCodeExpieryDate
  Date emailVerifiedDate
  Date phoneNumberVerifiedDate
  Member member

  int verificationAttempts = 0
  Date lastFailedVerificationAttemptDate

  def memberService


  static constraints = {
    email unique:true , nullable: false
    lastName nullable: true
    verificationCodeExpieryDate nullable: true
    lastEmailVerificationSentDate nullable: true
    lastPhoneVerificationSentDate nullable: true
    emailVerifiedDate nullable: true
    lastFailedVerificationAttemptDate nullable: true
    phoneNumberVerifiedDate nullable: true
    member nullable: true
    verificationCode nullable: true
    uuid(nullable: true)
  }
  static enum RegistrationStatus {NEW, CANCELED, BLOCKED, VERIFIED, VERIFICATION_DETAILS_SENT}

  def beforeInsert() {
    uuid = memberService.generateUUID()
  }

  def verificationSuccessful(boolean emailVerification, boolean phoneVerification){

    if(lastEmailVerificationSentDate){
      emailVerifiedDate = new Date()
    }
    if(phoneVerification){
      phoneNumberVerifiedDate = new Date()
    }
    verificationStatus = RegistrationStatus.VERIFIED

  }

  def sentEmailVerification(){
    lastEmailVerificationSentDate = new Date()
    emailVerificationSentCount++
    verificationStatus = RegistrationStatus.VERIFICATION_DETAILS_SENT
  }

  def sentPhoneVerification(){
    lastPhoneVerificationSentDate = new Date()
    phoneVerificationSentCount++
    verificationStatus = RegistrationStatus.VERIFICATION_DETAILS_SENT
  }

  def invalidUuidVerification(){
    verificationAttempts++
    lastFailedVerificationAttemptDate = new Date()

  }

}
