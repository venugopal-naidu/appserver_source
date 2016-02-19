package com.vellkare.core

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 */
@JsonIgnoreProperties(["properties","errors"])
class MemberApiModel {
    Long id
    String email
    String firstName
    String lastName
    String primaryPhone
    String description

    List<SocialAccountApiModel> socialAccounts
}
