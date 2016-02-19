package com.vellkare.social

import grails.converters.JSON

class TwitterService {

  final static def TWITTER_URL = ""

  def getProfile(String accessToken) {
    try {
      if (accessToken) {
        def urlString = TWITTER_URL + "" + accessToken
        URL url = new URL(urlString)
        return JSON.parse(url.getText())
      }
    } catch (Exception e) {
      println "Invalid access_token : " + accessToken
    }
    return null
  }
}
