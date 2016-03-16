package com.vellkare.core

class Registration {
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


  def memberService
  static belongsTo = [member: Member]

  static constraints = {
    email unique:true , nullable: false
    lastName nullable: true
    verificationCodeExpieryDate nullable: true
    lastEmailVerificationSentDate nullable: true
    lastPhoneVerificationSentDate nullable: true
    emailVerifiedDate nullable: true
    phoneNumberVerifiedDate nullable: true
    member nullable: true
    verificationCode nullable: true
    uuid(nullable: true)
  }
  static enum RegistrationStatus {NEW, CANCELED, BLOCKED, VERIFIED, VERIFICATION_DETAILS_SENT}

  def beforeInsert() {
    uuid = memberService.generateUUID()
  }

  def sentEmailVerification(){
    lastEmailVerificationSentDate = new Date()
    emailVerificationSentCount++
    verificationStatus = RegistrationStatus.VERIFICATION_DETAILS_SENT
  }

}
