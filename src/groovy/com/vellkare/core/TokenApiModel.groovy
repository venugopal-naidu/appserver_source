package com.vellkare.core

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.security.oauth2.common.OAuth2AccessToken

/**
 */
@JsonIgnoreProperties(["properties","errors"])
class TokenApiModel {
    String scope
    Long expires_in
    String token_type
    String refresh_token
    String access_token

    public TokenApiModel(OAuth2AccessToken token) {
        this.scope = token.scope.join(",")
        this.expires_in = token.expiresIn
        this.token_type = token.tokenType
        this.refresh_token = token.refreshToken.value
        this.access_token = token.value
    }
}
