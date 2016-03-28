package com.vellkare.core

class MedicalRecordType implements Serializable {
  String name
  String description
  Boolean enabled = true

  static constraints = {
    name unique: true
    description nullable: true
    enabled nullable: false, defaultValue:true
  }

  static mapping = {
    version(false)
    cache true
  }
}
