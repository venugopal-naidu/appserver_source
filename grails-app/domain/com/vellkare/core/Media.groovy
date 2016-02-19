package com.vellkare.core

class Media {
  String location
  String extension
  String fileName
  boolean publicMedia
  String pictureTypes
  public enum Imgext_type {
    JPG, PNG, GIF, BMP, TIF
  }

  public enum MediaType {
    UploadLocalFile('Upload a file from your computer'), ExternalLink('Give an external link'), UploadYoutubeVideo('Upload a youtube video')
    final String value

    MediaType(String value) { this.value = value }

    String toString() { value }

    String getKey() { name() }

  }

  MediaType mediaType = MediaType.UploadLocalFile

  static constraints = {
    mediaType nullable: false
    extension nullable: true
    fileName nullable: true
  }

  def supportedTypes() {
    pictureTypes.split(",").collectEntries {
      def (mediatype, size) = it.split(":"); size = size.split("x"); [(mediatype): size]
    }
  }

  static def defaultSupportedTypes(def mediaConfig) {
    mediaConfig.mediaTypes.collectEntries { mediaType -> [(mediaType): mediaConfig."${mediaType}".size] }
  }

}
