package com.vellkare.core

import com.fasterxml.jackson.annotation.JsonIgnore

class Doctor {

  String name
  String description
  Gender gender
  String website
  String phone
  String fax
  String email
  Boolean velkareVerified = false
  String degree1
  String univ1
  String degree2
  String univ2
  String degree3
  String univ3
  String degree4
  String univ4
  String degree5
  String univ5
  int experience = 0
  String awards
  String language

  @JsonIgnore
  Set<DoctorSpeciality> specialities = new HashSet<>()

  @JsonIgnore
  Set<DoctorHospital> hospitals = new HashSet<>()

  static hasMany = [ specialities: DoctorSpeciality, hospitals : DoctorHospital]

  static constraints = {
    description  nullable: true
    phone nullable: true
    fax nullable: true
    email nullable: true
    specialities nullable:true
    website nullable:true
    gender nullable: true
    degree1 nullable: true
    univ1 nullable: true
    degree2 nullable: true
    univ2 nullable: true
    degree3 nullable: true
    univ3 nullable: true
    degree4 nullable: true
    univ4 nullable: true
    degree5 nullable: true
    univ5 nullable: true
    experience nullable:true
    awards nullable: true
  }
}
