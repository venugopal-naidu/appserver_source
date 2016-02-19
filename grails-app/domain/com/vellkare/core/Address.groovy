package com.vellkare.core

class Address {

  String addressLine1
  String addressLine2
  String addressLine3
  String addressLine4
  String city
  String state
  String country
  String postalCode

  static constraints = {
    addressLine1(nullable: true)
    addressLine2(nullable: true)
    addressLine3(nullable: true)
    addressLine4(nullable: true)
    postalCode(nullable: true)
    city nullable: true
    state nullable: true
    country nullable: true
    postalCode nullable: true
  }

  def clear(){
    addressLine1 = addressLine2 =addressLine3 =addressLine4 = city = state= country = postalCode = null
  }


  def propertyValueMap() {
    [
      id: id, addressLine1: addressLine1, addressLine2: addressLine2, addressLine3: addressLine3,
      addressLine4: addressLine4, city: city, state: state, country: country, postalCode: postalCode
    ]
  }
}
