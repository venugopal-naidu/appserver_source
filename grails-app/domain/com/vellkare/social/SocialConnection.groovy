package com.vellkare.social

import com.vellkare.core.Login

class SocialConnection {

  long uid
  String accessToken
  String providerId
  String providerUserId
  String displayName
  String profileUrl
  String imageUrl
  String secret
  String refreshToken
  Long rank
  Long expireTime
  String email
  boolean deleted


  static belongsTo = [login: Login]

  static constraints = {
    //    uid unique: true          //multiple emails can connect to one login. eg: facebook yahoo email and google+ email can connect to one login
    email nullable: true
    expireTime nullable: true
    imageUrl nullable: true
    rank nullable: true
    refreshToken nullable: true
    secret nullable: true
    deleted defaultValue: false
    profileUrl nullable: true     //made nullable true
    accessToken size: 1..512
  }
}
