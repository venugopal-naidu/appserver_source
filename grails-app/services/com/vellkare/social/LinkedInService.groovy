package com.vellkare.social

import grails.converters.JSON

class LinkedInService {

  final static def LINKEDIN_API_URL = "https://api.linkedin.com/v1/"
  final static def LINKEDIN_ACCESSTOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken"

  def securityService
  def grailsApplication

  def getProfile(String accessToken) {
    try {
      if (accessToken) {
        def urlString = LINKEDIN_API_URL + "people/~:(id,num-connections,picture-url,firstName,lastName,public-profile-url,email-address)?format=json&oauth2_access_token=" + accessToken
        URL url = new URL(urlString)
        return JSON.parse(url.getText())
      }
    } catch (Exception e) {
      log.error("Invalid access_token : " + accessToken, e)
    }
    return null
  }

  def getAuthorizeURL() {
    def m = securityService.currentMember
    def socialConfig = grailsApplication.config.vellkare.auth.social.linkedIn
    def url = "https://www.linkedin.com/uas/oauth2/authorization" +
      "?client_id=" + socialConfig.clientId +
      "&response_type=code" +
      "&redirect_uri=" + socialConfig.redirectURI +
      "&state=" + (m ? m.id : socialConfig.state) +
      "&scope=" + socialConfig.scope
    url
  }

  def getAccessToken(String code) {
    try {
      if (code) {
        def socialConfig = grailsApplication.config.vellkare.auth.social.linkedIn
        def url = new URL(LINKEDIN_ACCESSTOKEN_URL +
          "?grant_type=authorization_code" +
          "&client_id=" + socialConfig.clientId +
          "&client_secret=" + socialConfig.clientSecret +
          "&redirect_uri=" + socialConfig.redirectURI +
          "&code=" + code)
        HttpURLConnection con = url.openConnection()
        con.setRequestMethod("POST")
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        con.setDoOutput(true)
        int responseCode = con.getResponseCode()
        BufferedReader inp = new BufferedReader(new InputStreamReader(con.getInputStream()))
        String inputLine
        StringBuffer res = new StringBuffer()
        while ((inputLine = inp.readLine()) != null) {
          res.append(inputLine)
        }
        inp.close()
        return JSON.parse(res.toString())
      }
    } catch (Exception e) {
      log.error("something went wrong " + e)
      return null
    }
  }
}
