package com.vellkare.core

import org.apache.commons.lang.builder.HashCodeBuilder

class MemberRole implements Serializable {

  Member member
  Role role

  static constraints = {
    member nullable: false
    role nullable: false
  }

  static belongsTo = [member: Member, role: Role]


  boolean equals(other) {
    if (!(other instanceof MemberRole)) {
      return false
    }

    other.member?.id == member?.id &&
      other.role?.id == role?.id
  }

  int hashCode() {
    def builder = new HashCodeBuilder()
    if (member) builder.append(member.id)
    if (role) builder.append('.').append(role.id)
    builder.toHashCode()
  }

  static MemberRole create(Member member, Role role, boolean flush = false) {
    new MemberRole(member: member, role: role).save(flush: flush, insert: true)
  }

  static mapping = {
    id composite: ['role', 'member']
    version false
  }
}
