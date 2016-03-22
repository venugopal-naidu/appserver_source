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
      ['id','name', 'description', 'website', 'velkareVerified',
       'degree1', 'univ1', 'degree2', 'univ2', 'degree3', 'univ3',
       'degree4', 'univ4', 'degree5', 'univ5', 'experience', 'awards', 'language'].each { name ->
        def val = doctor."${name}"
        d."${name}" = val
      }

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
        d.specialties = doctor.specialities.collect { it.speciality.name.toLowerCase().capitalize() }
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
