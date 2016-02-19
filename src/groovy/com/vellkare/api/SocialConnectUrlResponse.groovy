package com.vellkare.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 */
@JsonIgnoreProperties(["properties","errors"])
class SocialConnectUrlResponse {
    String url
}
