package com.vellkare.core

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 */
@JsonIgnoreProperties(["properties","errors"])
class MediaApiModel {
    String extension
    String supportedsizes
    List<MediaFileDetails> mediaFileDetailsList
}
