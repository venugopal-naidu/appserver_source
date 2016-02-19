package com.vellkare.social

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONElement

class FacebookService {
  final static def GRAPH_URL = "https://graph.facebook.com/v2.3/"
  final static def PROFILE_URL = "http://facebook.com/profile.php"

  def securityService
  def grailsApplication

  def getProfile(String accessToken) {
    try {
      if (accessToken) {
        def urlString = GRAPH_URL + "me/?access_token=" + accessToken
        URL url = new URL(urlString)
        return JSON.parse(url.getText())
      }
    } catch (Exception e) {
      println "Invalid access_token : " + accessToken
    }
    return null
  }

  def getProfileImageUrl(String profileId) {
    if (profileId) {
      def urlString = GRAPH_URL + profileId + "/picture?redirect=false&type=large"
      URL url = new URL(urlString)
      JSONElement json = JSON.parse(url.getText())
      json.data.url
    }
  }

  /*
   def url = "https://www.facebook.com/v2.3/dialog/oauth" +
              "?client_id=955065507860235" +
              "&redirect_uri=http://localhost:8080/vellkares/api/v0/auth/social/facebook/authorize" +
              "&scope=email"
   */

  def getAuthorizeURL() {
    def m = securityService.currentMember
    def socialConfig = grailsApplication.config.vellkare.auth.social.facebook
    def url = "https://www.facebook.com/v2.3/dialog/oauth" +
      "?client_id=" + socialConfig.clientId +
      "&redirect_uri=" + socialConfig.redirectURI + (m ? '/' + m.id : '') +
      "&scope=" + socialConfig.scope
    url
  }

  def getAccessToken(String code) {
    try {
      if (code) {
        def m = securityService.currentMember
        def socialConfig = grailsApplication.config.vellkare.auth.social.facebook
        def url = new URL("https://graph.facebook.com/v2.3/oauth/access_token?"
          + "client_id=" + socialConfig.clientId +
          "&client_secret=" + socialConfig.clientSecret +
          "&redirect_uri=" + socialConfig.redirectURI +
          "&code=" + code)
        HttpURLConnection con = url.openConnection()
        con.setRequestMethod("POST")
        con.setRequestProperty("Accept", "application/json")
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
      log.error("something went wrong ", e)
      return null
    }
  }
}
