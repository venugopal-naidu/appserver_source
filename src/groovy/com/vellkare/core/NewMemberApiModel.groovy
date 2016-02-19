package com.vellkare.core

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 */
@JsonIgnoreProperties(["properties"])
class NewMemberApiModel {
    MemberApiModel member
    TokenApiModel token
}
