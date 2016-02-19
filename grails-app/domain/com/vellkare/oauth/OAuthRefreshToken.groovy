package com.vellkare.oauth

class OAuthRefreshToken {

  byte[] authentication
  String value

  static constraints = {
    value nullable: false, blank: false, unique: true
    authentication nullable: false, maxSize: 1024 * 4, validator: { val, obj -> val.size() > 0 }
  }

  static mapping = {
    version false
  }
}
