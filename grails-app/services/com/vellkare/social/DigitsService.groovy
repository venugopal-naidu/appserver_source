package com.vellkare.social

import grails.converters.JSON

class DigitsService {

  def grailsApplication

  def getProfile(String authorization, String provider) {
    try {
      if (authorization && provider) {
        def url = new URL(provider)
        HttpURLConnection con = url.openConnection()
        con.setRequestMethod("GET")
        con.setRequestProperty("Authorization", authorization)
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
      println "Invalid authorization or provider"
    }
    return null
  }

}
