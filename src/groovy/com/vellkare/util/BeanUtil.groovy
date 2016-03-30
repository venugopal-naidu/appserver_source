package com.vellkare.util

import com.vellkare.core.AvailabilityMini
import com.vellkare.core.Doctor
import com.vellkare.core.DoctorHospital
import com.vellkare.core.DoctorHospitalMini
import com.vellkare.core.HospitalAvailabilityMini
import com.vellkare.core.Lab
import com.vellkare.core.LabMini
import grails.util.Holders

/**
 * Created by roopesh on 21/03/16.
 */
class BeanUtil {


  static def transformObject (def fromType , def toType, def fromObj){
    def cfg = Holders.getConfig()
    if(fromObj instanceof Doctor && toType == DoctorHospitalMini) {
      def doctor = fromObj
      DoctorHospitalMini d = new DoctorHospitalMini()
      d.id = doctor.id
      d.name = doctor.name
      d.description = doctor.description
      d.website = doctor.website
      d.velkareVerified = doctor.velkareVerified
      d.degree1 = doctor.degree1
      d.degree2 = doctor.degree2
      d.degree3 = doctor.degree3
      d.degree4 = doctor.degree4
      d.degree5 = doctor.degree5
      d.univ1 = doctor.univ1
      d.univ2 = doctor.univ2
      d.univ3 = doctor.univ3
      d.univ4 = doctor.univ4
      d.univ5= doctor.univ5
      d.experience = doctor.experience
      d.awards = doctor.awards
      d.language = doctor.language

      if (doctor.hospitals) {
        d.hospitals = doctor.hospitals.collect { DoctorHospital dh ->
          HospitalAvailabilityMini mini = new HospitalAvailabilityMini()
          mini.id = dh.hospital.id
          mini.name = dh.hospital.name
          mini.address1 = dh.hospital.address1
          mini.address2 = dh.hospital.address2
          mini.address3 = dh.hospital.address3
          mini.address4 = dh.hospital.address4
          ["MONDAY", 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY','SATURDAY','SUNDAY'].each {
            mini.availability.put ( it,[new AvailabilityMini(from: dh.serviceHoursFrom, to: dh.serviceHoursTo)] )
          }
          mini
        }
      }
      if(doctor.specialities){
        d.specialties = doctor.specialities.collect { it.speciality.name.toLowerCase().capitalize() }.sort().unique()
      }
      def photoUrl = cfg.images.doctors.path?:"/images/doctor/"
      photoUrl= "${photoUrl}${doctor.id}.jpeg"
      d.photoUrl=photoUrl
      return d
    }
    if(fromObj instanceof Lab && toType == LabMini) {
      def lab = fromObj
      LabMini d = new LabMini()
      ['id', 'name', 'address1', 'address2',
       'address3', 'address4', 'state', 'city', 'country', 'postalCode',
       'website','velkareVerified'].each { name ->
        def val = lab."${name}"
        d."${name}" = val
      }
      def photoUrl = cfg.images.labs.path?:"/images/lab/"
      photoUrl= "${photoUrl}${lab.id}.jpeg"
      d.labImageUrl = photoUrl
      return d
    }
    return null
  }
}
