package com.vellkare.oauth

import org.apache.commons.lang.RandomStringUtils

class OAuthClient {

  private static final String NO_CLIENT_SECRET = ''

  transient springSecurityService

  String namespace
  String clientId
  String clientSecret

  Integer accessTokenValiditySeconds
  Integer refreshTokenValiditySeconds

  Map<String, Object> additionalInformation

  static hasMany = [
    authorities         : String,
    authorizedGrantTypes: String,
    resourceIds         : String,
    scopes              : String,
    redirectUris        : String
  ]

  static transients = ['springSecurityService']

  static constraints = {
    clientId blank: false, unique: true
    clientSecret nullable: true
    namespace nullable: true

    accessTokenValiditySeconds nullable: true
    refreshTokenValiditySeconds nullable: true

    authorities nullable: true
    authorizedGrantTypes nullable: true

    resourceIds nullable: true
    scopes nullable: true

    redirectUris nullable: true
    additionalInformation nullable: true
  }

  def beforeInsert() {
    encodeClientSecret()
    setNamespace()
  }

  def beforeUpdate() {
    if (isDirty('clientSecret')) {
      encodeClientSecret()
    }
    setNamespace()
  }

  def setNamespace() {
    if (!namespace) {
      this.@namespace = clientId.substring(0, 2) + "-" + RandomStringUtils.randomAlphanumeric(4)
    }
  }

  protected void encodeClientSecret() {
    clientSecret = clientSecret ?: NO_CLIENT_SECRET
    clientSecret = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(clientSecret) : clientSecret
  }
}
