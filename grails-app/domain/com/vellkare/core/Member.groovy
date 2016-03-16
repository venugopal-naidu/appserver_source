package com.vellkare.core

import org.apache.commons.lang.builder.HashCodeBuilder

class Member {

  public enum MemberType {
    BASIC, PREMIUM, FREE_TRIAL_PREMIUM
  }

  public enum AccountStatus {
    PENDING, ACTIVE, CLOSED
  }

  Login login
  String uuid
  String firstName
  String lastName
  String description
  String title
  Gender gender

  String primaryPhone
  String secondaryPhone
  String fax
  String email
  MemberType memberType = MemberType.BASIC
  AccountStatus accountStatus = AccountStatus.ACTIVE

  // auto timestamp fields
  Date dateCreated
  Date lastUpdated

  Address permanentAddress
  Address currentAddress

  Registration registration


  def memberService

  static belongsTo = [login: Login, registration : Registration]

  static hasMany = [roles: MemberRole]

  static constraints = {
    email(nullable: true)
    lastName(nullable: true)
    primaryPhone(nullable: true)
    secondaryPhone(nullable: true)
    description(nullable: true)
    uuid(nullable: true)
    fax(nullable: true)
    gender(nullable: true)
    title(nullable: true)
    permanentAddress nullable: true
    currentAddress(nullable: true)
  }

  @Override
  boolean equals(other) {
    if (!(other instanceof Member)) return false;
    if (id && other.id && id == other.id) return true;
    if (email == other.email) return true;
    return false;
  }

  @Override
  int hashCode() {
    def builder = new HashCodeBuilder();
    if (id) {
      builder.append(id)
    } else {
      builder.append(email)
    }
    builder.toHashCode()
  }

  def beforeInsert() {
    uuid = memberService.generateUUID()
  }

  @Override
  def String toString() {
    "member: id:${id}, email:${email}, username:${username}"
  }

  def propertyValueMap() {
    [
      id              : id,
      uuid            : uuid,
      firstName       : firstName,
      lastName        : lastName,
      description     : description,
      title           : title,
      gender          : gender,
      primaryPhone    : primaryPhone,
      secondaryPhone   : secondaryPhone,
      fax             : fax,
      email           : email,
      memberType      : memberType,
      accountStatus   : accountStatus,
      dateCreated     : dateCreated,
      lastUpdated     : lastUpdated,

      permanentAddress: permanentAddress?permanentAddress.propertyValueMap():null,
      currentAddress  : currentAddress?currentAddress.propertyValueMap():null
    ]

  }
}
