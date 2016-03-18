package com.vellkare.core

class Appointment{

  Member member
  Doctor doctor
  Hospital hospital
  Lab lab
  AppointmentStatus status = AppointmentStatus.NEW
  AppointmentType type
  Date bookingDate = new Date()
  Date fromTime
  Date toTime
  Date cancelledDate

  String notes
  String cancelReason
  Long confirmedByUser
  Long cancelledByUser

  Boolean sendReminderEmail = false


  static enum AppointmentType { DOCTOR_APPOINTMENT, LAB_DIAGNOSIS }
  static enum AppointmentStatus { NEW, CONFIRMED, CANCELLED, NO_SHOW, COMPLETE }

  static contraints = {
    doctor nullable:true
    hospital nullable:true
    lab nullable: true
    cancelledDate nullable: true
    notes nullable: true
    cancelReason nullable:true
    confirmedByUser nullable:true
    cancelledByUser nullable:true

  }

}
