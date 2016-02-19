package com.vellkare.core

class Education {

  String qualification
  String university

  Doctor doctor

  static belongsTo = [doctor : Doctor]

  static constraints = {
  }

}
