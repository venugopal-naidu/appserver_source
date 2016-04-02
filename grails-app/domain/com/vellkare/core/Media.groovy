package com.vellkare.core

class Media {
  String fileName
  boolean publicMedia = false
  String contentType
  Integer size = 0
  MediaData mediaData

  static hasOne = [mediaData: MediaData]

  static mapping = {
    mediaData cascade: "save-update,delete", lazy: true
  }

  static constraints = {
    fileName nullable: true
    size nullable: true
    contentType nullable: true
    mediaData nullable: true
  }

}
