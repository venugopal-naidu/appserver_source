package com.vellkare.oauth

import groovy.util.logging.Log4j
import org.springframework.security.oauth2.common.util.SerializationUtils
import org.springframework.security.oauth2.provider.OAuth2Authentication

@Log4j
class WLOAuth2AuthenticationSerializer {

    byte[] serialize(OAuth2Authentication authentication) {
        log.info("Serializing: ${authentication}")
        SerializationUtils.serialize(authentication)
    }

    OAuth2Authentication deserialize(byte[] authentication) {
        log.info("De-Serializing: ${authentication}")
        new ByteArrayInputStream(authentication).withObjectInputStream(getClass().classLoader) { ois ->
            ois.readObject() as OAuth2Authentication
        }
    }
}
