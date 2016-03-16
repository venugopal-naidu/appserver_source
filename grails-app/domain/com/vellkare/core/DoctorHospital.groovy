package com.vellkare.core

import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * Created by roopesh on 07/03/16.
 */
class DoctorHospital implements Serializable {
  Doctor doctor
  Hospital hospital
  boolean velkareVerified = false
  String serviceHoursFrom
  String serviceHoursTo
  Long serviceProviderId

  static constraints = {
    serviceHoursFrom nullable: true
    serviceHoursTo nullable: true
    serviceProviderId nullable: true
  }

  static belongsTo = [
    doctor  : Doctor,
    hospital: Hospital
  ]

  static mapping = {
    id composite: ['doctor', 'hospital']
  }

  boolean equals(other) {
    if (!(other instanceof DoctorSpeciality)) {
      return false
    }

    other.doctor?.id == doctor?.id &&
      other.hospital?.id == hospital?.id
  }

  int hashCode() {
    def builder = new HashCodeBuilder()
    if (doctor) builder.append(doctor.id)
    if (hospital) builder.append('.').append(hospital.id)
    builder.toHashCode()
  }

}
