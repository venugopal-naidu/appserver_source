package com.vellkare.core

class Doctor {

  String name
  String description
  Gender gender

  String addressLine1
  String addressLine2
  String addressLine3
  String addressLine4
  String city
  String district
  String state
  String country
  String postalCode

  String phone
  String fax
  String email

  boolean verified

  Long createdBy  // ?
  Long lastUpdatedBy  // ?

  String website

  Set<Education> educationDetails

  // work experience

  // auto timestamp fields
  Date dateCreated
  Date lastUpdated

  static hasMany = [educationDetails: Education]


  static constraints = {
  }
}
