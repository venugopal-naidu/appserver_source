package com.vellkare.social

import com.vellkare.api.SocialConnectErrorResponse
import grails.transaction.Transactional

/**
 */
class SocialController {

  static responseFormats = ['json', 'xml']

  static namespace = 'v0'

  def unsupported() {
    respond new SocialConnectErrorResponse("Social type is not supported.", [])
  }

  @Transactional
  def login(String socialId) {
    def controllerName = grailsApplication.config.vellkare.auth.social."${socialId}".controllerName
    if (!controllerName || !(controllerName in grailsApplication.config.vellkare.auth.social.enabled)) {
      respond new SocialConnectErrorResponse("Social type is not supported.", [])
      return
    } else {
      def c = grailsApplication.getArtefactByLogicalPropertyName("Controller", controllerName)
      def o = applicationContext.getBean(c.clazz.name)
      return o.login(socialId)
    }
  }

  @Transactional
  def authorize(String socialId, String code, String state) {
    def controllerName = grailsApplication.config.vellkare.auth.social."${socialId}".controllerName
    if (!controllerName || !(controllerName in grailsApplication.config.vellkare.auth.social.enabled)) {
      respond new SocialConnectErrorResponse("Social type is not supported.", [])
      return
    } else {
      def c = grailsApplication.getArtefactByLogicalPropertyName("Controller", controllerName)
      def o = applicationContext.getBean(c.clazz.name)
      return o.authorize(code, state)
    }
  }

  def socialConnect(String socialId, SocialCommand cmd) {
    def controllerName = grailsApplication.config.vellkare.auth.social."${socialId}".controllerName
    if (!controllerName || !(controllerName in grailsApplication.config.vellkare.auth.social.enabled)) {
      respond new SocialConnectErrorResponse("Social type is not supported.", [])
      return
    } else {
      def c = grailsApplication.getArtefactByLogicalPropertyName("Controller", controllerName)
      def o = applicationContext.getBean(c.clazz.name)
      return o.connect(null, cmd)
    }
  }
}

class SocialCommand {
  String clientId
  String accessToken
  Long expiresIn

  static constraints = {
    clientId validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("clientId", "clientId.empty")
      }
    }
    accessToken validator: { val, cmd, errors ->
      if (!val) {
        errors.rejectValue("accessToken", "accessToken.empty")
      }
    }
    expiresIn nullable: true
  }
}
