package com.vellkare.core

import grails.transaction.Transactional

class MemberService {

  static transactional = false
  def grailsApplication
  def mailService
  def contentService

  def generateUUID() {
    UUID.randomUUID().toString()
  }

  def findByLogin(Login login) {
    Member.createCriteria().get {
      eq('login', login)
    }
  }

  def findByUsername(String username) {
    Member.createCriteria().get {
      login {
        eq('username', username)
      }
    }
  }

  def findByEmail(String email) {
    Member.createCriteria().get {
      eq('email', email)
    }
  }

  @Transactional
  def createMember(Registration registration, String password) {
    Login login = new Login(username: registration.email, password: password, enabled: true)
    login.save(failOnError: true, flush: true)
    Member m = new Member(login: login, firstName: registration.firstName, lastName: registration.lastName,
      email: registration.email, primaryPhone: registration.phoneNumber)
    m.registration= registration
    m.save(failOnError: true, flush: true)

    if (grailsApplication.config.app?.emails?.welcome && m.email) {
      mailService.sendMail {
        to m.email
        subject "Welcome Member!"
        def bodyArgs = [view: '/emails/member/welcome', model: [member: m]]
        def mailHtml = contentService.applyTemplate(bodyArgs.view, bodyArgs.model)
        html(mailHtml)
      }
    }
  }

  @Transactional
  def updateMember(Member member, MemberCommand cmd) {
    member.firstName = cmd.firstName
    member.lastName = cmd.lastName
    member.primaryPhone = cmd.primaryPhone
    member.description = cmd.description

    member.save(failOnError: true, flush: true)
  }

  @Transactional
  def updateMember(Member member, MemberProfileCommand cmd) {
    member.firstName = cmd.firstName
    member.lastName = cmd.lastName
    member.primaryPhone = cmd.primaryPhone
    member.description = cmd.description
    member.email = cmd.email
    member.fax = cmd.fax
    member.gender=cmd.gender
    member.secondaryPhone = cmd.secondaryPhone
    member.title=cmd.title
    if(!cmd.currentAddress) {
      if(member.currentAddress){
        member.currentAddress.clear()
      }

    }else if(cmd.currentAddress){
      if (!member.currentAddress){
        member.currentAddress = new Address()
        member.currentAddress.save(flush:true, failOnEror:true)
      }
      member.currentAddress.addressLine1 = cmd.currentAddress.addressLine1
      member.currentAddress.addressLine2 = cmd.currentAddress.addressLine2
      member.currentAddress.addressLine3 = cmd.currentAddress.addressLine3
      member.currentAddress.addressLine4 = cmd.currentAddress.addressLine4
      member.currentAddress.city= cmd.currentAddress.city
      member.currentAddress.state= cmd.currentAddress.state
      member.currentAddress.country= cmd.currentAddress.country
      member.currentAddress.postalCode= cmd.currentAddress.postalCode
    }

    if(!cmd.permanentAddress) {
      if(member.permanentAddress){
        member.permanentAddress.clear()
      }

    }else if(cmd.permanentAddress){
      if (!member.permanentAddress){
        member.permanentAddress = new Address()
        member.permanentAddress.save(flush:true, failOnEror:true)
      }
      member.permanentAddress.addressLine1 = cmd.permanentAddress.addressLine1
      member.permanentAddress.addressLine2 = cmd.permanentAddress.addressLine2
      member.permanentAddress.addressLine3 = cmd.permanentAddress.addressLine3
      member.permanentAddress.addressLine4 = cmd.permanentAddress.addressLine4
      member.permanentAddress.city= cmd.permanentAddress.city
      member.permanentAddress.state= cmd.permanentAddress.state
      member.permanentAddress.country= cmd.permanentAddress.country
      member.permanentAddress.postalCode= cmd.permanentAddress.postalCode
    }

    member.save(failOnError: true, flush: true)
  }

}
