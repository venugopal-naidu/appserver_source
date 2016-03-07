package com.vellkare.core

import grails.transaction.Transactional

@Transactional
class DoctorService {

  def save(def doctor) {
    doctor.save(flush:true, failOnError:true)
    doctor
  }
}
