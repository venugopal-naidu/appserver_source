package com.vellkare.core

/**
 * Created by roopesh on 25/03/16.
 */
class MediaData {
  Media media
  byte[] fileData
  static constraints = {
    fileData nullable: false, minSize: 1
  }
}
