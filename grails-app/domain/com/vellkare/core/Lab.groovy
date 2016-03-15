package com.vellkare.core

class Lab {
  String name
  String address1
  String address2
  String address3
  String address4
  String city
  String district
  String state
  String country
  String postalCode
  String website
  String phone
  String fax
  String email
  Boolean velkareVerified = false

  Set<LabPackageTest> packages

  static constraints = {
    address1 nullable: true
    address2 nullable: true
    address3 nullable: true
    address4 nullable: true
    city nullable: true
    district nullable: true
    state nullable: true
    country nullable: true
    postalCode nullable: true
    website nullable: true
    phone nullable: true
    fax nullable: true
    email nullable: true
  }

  static hasMany = [packages: LabPackageTest]
}
