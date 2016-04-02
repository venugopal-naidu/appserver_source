package com.vellkare.core

class Role implements Serializable {

  Authority authority
  boolean isActive = true

  static enum Authority {
    ROLE_ADMIN, ROLE_USER, ROLE_CSR
  }

  static mapping = {
    cache true
  }

  // auto timestamp fields
  Date dateCreated
  Date lastUpdated

  static constraints = {
    authority unique: true
  }

  def boolean equals(Object aRole) {
    if (!(aRole instanceof Role)) {
      return false
    }
    return this.authority.equals(aRole.authority)
  }

  def int hashCode() {
    return this.authority.hashCode()
  }

  def String toString() {
    return this.authority.name()
  }
}
