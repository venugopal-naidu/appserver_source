package com.vellkare.core

import org.apache.commons.lang.builder.HashCodeBuilder

class DoctorSpeciality implements Serializable{

  Doctor doctor
  Speciality speciality
  boolean velkareVerified = false

  static belongsTo = [doctor: Doctor, speciality: Speciality]

  static constraints = {
    doctor nullable: false
    speciality nullable: false
  }

  static mapping = {
    id composite: ['doctor', 'speciality']
    cache true
    version false
  }

  DoctorSpeciality(Speciality speciality){
    this.speciality = speciality
  }

  DoctorSpeciality (Doctor doctor, Speciality speciality){
    this.doctor = doctor
    this.speciality = speciality
  }

  boolean equals(other) {
    if (!(other instanceof DoctorSpeciality)) {
      return false
    }

    other.doctor?.id == doctor?.id &&
      other.speciality?.id == speciality?.id
  }

  int hashCode() {
    def builder = new HashCodeBuilder()
    if (doctor) builder.append(doctor.id)
    if (speciality) builder.append('.').append(speciality.id)
    builder.toHashCode()
  }

}
