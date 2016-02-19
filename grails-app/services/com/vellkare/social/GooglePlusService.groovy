package com.vellkare.social

import grails.converters.JSON

class GooglePlusService {
  final static def USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo"
  final static def GOOGLE_PLUS_URL = "https://plus.google.com"
  final static def GOOGLEPLUS_ACCESSTOKEN_URL = "https://www.googleapis.com/oauth2/v3/token"

  def securityService
  def grailsApplication

  def getProfile(String accessToken) {
    try {
      if (accessToken) {
        def urlString = USER_INFO_URL + "?access_token=" + accessToken
        URL url = new URL(urlString)
        return JSON.parse(url.getText())
      }
    } catch (Exception e) {
      println "Invalid access_token : " + accessToken
    }
    return null
  }

  def getAuthorizeURL() {
    def m = securityService.currentMember
    def socialConfig = grailsApplication.config.vellkare.auth.social.googlePlus
    def url = "https://accounts.google.com/o/oauth2/auth" +
      "?client_id=" + socialConfig.clientId +
      "&redirect_uri=" + socialConfig.redirectURI +
      "&response_type=code" +
      "&scope=" + socialConfig.scope +
      "&state=" + (m ? m.id : socialConfig.state)    //state is optional and used for CSRF issues
    url
  }

  def getAccessToken(String code) {
    try {
      if (code) {
        def socialConfig = grailsApplication.config.vellkare.auth.social.googlePlus
        def url = new URL(GOOGLEPLUS_ACCESSTOKEN_URL)
        String params = "grant_type=authorization_code" +
          "&client_id=" + socialConfig.clientId +
          "&client_secret=" + socialConfig.clientSecret +
          "&redirect_uri=" + socialConfig.redirectURI +
          "&code=" + code
        HttpURLConnection con = url.openConnection()
        con.setDoOutput(true)
        con.setRequestMethod("POST")
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
        outputStreamWriter.write(params.toString());
        outputStreamWriter.flush()
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
