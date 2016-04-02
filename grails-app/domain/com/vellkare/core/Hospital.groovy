package com.vellkare.core

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

class Hospital implements Serializable{
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
  Boolean velkareVerified

  static mapping = {
    cache true
  }

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

  def printableAddress(){
    def sb = new StringBuilder()
    if(address1)sb.append(address1)
    if(address2) sb.append("\n").append(address2)
    if(address3) sb.append("\n").append(address3)
    if(address4) sb.append("\n").append(address4)
    if(city) sb.append("\n").append(city)
    if(state)sb.append("\n").append(state)
    if(postalCode)sb.append("\n").append(postalCode)
    if(country)sb.append("\n").append(country)
  }
}
