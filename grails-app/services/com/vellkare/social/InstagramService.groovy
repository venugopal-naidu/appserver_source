package com.vellkare.social

import grails.converters.JSON

class InstagramService {

  final static def INSTAGRAM_API_URL = "https://api.instagram.com"
  final static def INSTAGRAM_ACCESSTOKEN_URL = "https://api.instagram.com/oauth/access_token"

  def securityService
  def grailsApplication

  def getProfile(String accessToken) {
    try {
      if (accessToken) {
        def urlString = INSTAGRAM_API_URL + "/v1/users/self/?access_token=" + accessToken
        URL url = new URL(urlString)
        return JSON.parse(url.getText())
        /*
        sample success response
       {
            "meta": {
                "code": 200
            },
            "data": {
                "username": "vijaykumar.siripuram",
                "bio": "",
                "website": "",
                "profile_picture": "https://igcdn-photos-f-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-19/11357483_1047303611948309_32911545_a.jpg",
                "full_name": "",
                "counts": {
                    "media": 0,
                    "followed_by": 0,
                    "follows": 0
                },
                "id": "2125818477"
            }
        }

        sample failure response

        {
            "meta": {
                "error_type": "OAuthAccessTokenException",
                "code": 400,
                "error_message": "The access_token provided is invalid."
            }
        }
         */
      }
    } catch (Exception e) {
      log.error("Invalid access_token : " + accessToken, e)
    }
    return null
  }

  def getAuthorizeURL() {
    def m = securityService.currentMember
    def socialConfig = grailsApplication.config.vellkare.auth.social.instagram
    def url = "https://api.instagram.com/oauth/authorize/" +
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
        def socialConfig = grailsApplication.config.vellkare.auth.social.instagram
        def url = new URL(INSTAGRAM_ACCESSTOKEN_URL)
        String params = "client_id=" + socialConfig.clientId +
          "&client_secret=" + socialConfig.clientSecret +
          "&grant_type=authorization_code" +
          "&redirect_uri=" + socialConfig.redirectURI +
          "&code=" + code
        HttpURLConnection con = url.openConnection()
        con.setDoOutput(true);
        con.setRequestMethod("POST")
        con.setRequestProperty("Accept", "application/json")
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
        outputStreamWriter.write(params.toString());
        outputStreamWriter.flush();
        int responseCode = con.getResponseCode()
        BufferedReader inp = new BufferedReader(new InputStreamReader(con.getInputStream()))
        String inputLine
        StringBuffer res = new StringBuffer()
        while ((inputLine = inp.readLine()) != null) {
          res.append(inputLine)
        }
        inp.close()
        con.disconnect()
        return JSON.parse(res.toString())

        /*
        success response
        {
            "access_token": "2125818477.894e1e2.8acac2924ac64c71a16c86dc0fdd0bdb",
            "user": {
                "username": "vijaykumar.siripuram",
                "bio": "",
                "website": "",
                "profile_picture": "https:\/\/igcdn-photos-f-a.akamaihd.net\/hphotos-ak-xfa1\/t51.2885-19\/11357483_1047303611948309_32911545_a.jpg",
                "full_name": "",
                "id": "2125818477"
            }
        }

        failure response

        {
            "code": 400,
            "error_type": "OAuthException",
            "error_message": "You must provide a client_secret"
        }
         */

      }
    } catch (Exception e) {
      log.error("something went wrong " + e)
      return null
    }
  }
}
