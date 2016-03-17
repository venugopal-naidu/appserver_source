package com.vellkare.core

/**
 * Created by roopesh on 15/03/16.
 */
class DoctorHospitalMini {
  Long id
  String name
  String description
  Gender gender
  String website
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
  String photoUrl
  Set<HospitalAvailabilityMini> hospitals
  Set <String> specialities

}
