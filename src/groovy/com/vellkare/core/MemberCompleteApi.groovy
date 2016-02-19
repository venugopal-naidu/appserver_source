package com.vellkare.core
/**
 * Created by roopesh on 14/02/16.
 */
class MemberCompleteApi {
  Login login
  String uuid
  String firstName
  String lastName
  String description
  String title
  Gender gender
  String primaryPhone
  String secondayPhone
  String fax
  String email
  Member.MemberType memberType
  Member.AccountStatus accountStatus

  // auto timestamp fields
  Date dateCreated
  Date lastUpdated

  Address permanentAddress
  Address currentAddress
  Set<MemberRole> roles
}
