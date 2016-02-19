package com.vellkare.social

import grails.converters.JSON

class GitHubService {

  final static def GITHUB_API_URL = "https://api.github.com/"
  final static def GITHUB_ACCESSTOKEN_URL = "https://github.com/login/oauth/access_token"

  def securityService
  def grailsApplication

  def getProfile(String accessToken) {
    try {
      if (accessToken) {
        def urlString = GITHUB_API_URL + "user?access_token=" + accessToken
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
    def socialConfig = grailsApplication.config.vellkare.auth.social.gitHub
    def url = "https://github.com/login/oauth/authorize" +
      "?client_id=" + socialConfig.clientId +
      "&redirect_uri=" + socialConfig.redirectURI + (m ? "?state=" + m.id : '') +
      "&scope=" + socialConfig.scope
    url
  }

  def getAccessToken(String code) {
    try {
      if (code) {
        def socialConfig = grailsApplication.config.vellkare.auth.social.gitHub
        def url = new URL(GITHUB_ACCESSTOKEN_URL +
          "?client_id=" + socialConfig.clientId +
          "&client_secret=" + socialConfig.clientSecret +
          "&code=" + code +
          "&redirect_uri=" + socialConfig.redirectURI)
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

        /*
        sample success response

        {
            "access_token": "38eafabd300d05b2b8dd3fe6b97d221065bc0ca3",
            "token_type": "bearer",
            "scope": "delete_repo,notifications,repo,user"
        }

        failure response
        {
            "error": "bad_verification_code",
            "error_description": "The code passed is incorrect or expired.",
            "error_uri": "https://developer.github.com/v3/oauth/#bad-verification-code"
        }
         */
      }
    } catch (Exception e) {
      println "something went wrong " + e.getMessage()
      return null
    }
  }
}
