package com.vellkare.core

import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.OAuth2Request
import org.springframework.security.oauth2.provider.TokenRequest

class SecurityService {

  def springSecurityService
  def clientDetailsService
  def oauth2RequestFactory
  def tokenServices
  def tokenStore

  def isLoggedIn() {
    springSecurityService.isLoggedIn()
  }

  def Member getCurrentMember() {
    if (isLoggedIn()) {
      // log.info "principal: " + springSecurityService.getPrincipal()
      def auth = springSecurityService.authentication
      GrailsUser user = auth.principal
      Login login = Login.get(user.id)
      Member m = Member.findByLogin(login)
      //log.info "current member: ${m.id}, ${m.firstName}"
      m
    } else {
      return null
    }
  }

  @Transactional
  def getToken(String clientId, String grantType, String username, String password) {
    ClientDetails client = clientDetailsService.loadClientByClientId(clientId);
    def parameters = [grant_type: grantType, username: username, password: password, client_id: clientId, scope: "read+write"]
    springSecurityService.reauthenticate username

    TokenRequest tokenRequest = oauth2RequestFactory.createTokenRequest(parameters, client);
    OAuth2Request storedOAuth2Request = oauth2RequestFactory.createOAuth2Request(client, tokenRequest);
    OAuth2Authentication authentication = new OAuth2Authentication(storedOAuth2Request, SecurityContextHolder.context.authentication)

    OAuth2AccessToken token = tokenServices.createAccessToken(authentication)
    return token
  }

  @Transactional
  def invalidateToken(myToken) {
    OAuth2AccessToken aToken = tokenStore.readAccessToken(myToken)
    tokenStore.removeAccessToken(aToken?.value)
    tokenStore.removeRefreshToken(aToken?.refreshToken?.value)
  }
}
