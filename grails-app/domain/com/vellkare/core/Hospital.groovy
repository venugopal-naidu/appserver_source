package com.vellkare.core

class Hospital {
  String name;
  String hosGeocode
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
  String specialists
  String velkareVerified

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
    specialists nullable: true
    velkareVerified nullable: true
  }
}
