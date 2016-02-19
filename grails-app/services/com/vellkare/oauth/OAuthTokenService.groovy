package com.vellkare.oauth

import grails.transaction.Transactional
import org.springframework.security.oauth2.common.OAuth2AccessToken

@Transactional
class OAuthTokenService {

  def authorityService
  def tokenServices

  def hasScope(String token, String authorityName) {
    def authority = authorityService.findByUName(authorityName)
    OAuth2AccessToken accessToken // = tokenServices.find( .. )
    return accessToken?.getScope().contains(authorityName)
  }
}
