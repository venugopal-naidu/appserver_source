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
  Date confirmedDate

  String notes
  String cancelReason
  Long confirmedByUserId
  Long cancelledByUserId

  Boolean sendReminderEmail = false

  static belongsTo = [member: Member, hospital : Hospital, lab : Lab, doctor: Doctor]


  static enum AppointmentType { DOCTOR_APPOINTMENT, LAB_DIAGNOSIS }
  static enum AppointmentStatus { NEW, CONFIRMED, CANCELLED, NO_SHOW, COMPLETE }

  static constraints = {
    doctor nullable:true
    hospital nullable:true
    lab nullable: true
    cancelledDate nullable: true
    confirmedDate nullable: true
    notes nullable: true
    cancelReason nullable:true
    confirmedByUserId nullable:true
    cancelledByUserId nullable:true

  }

  def isCancelled(){
    return status==AppointmentStatus.CANCELLED
  }

  def canCancel(){
    status in [AppointmentStatus.CONFIRMED, AppointmentStatus.NEW]
  }

  def canConfirm(){
    status == AppointmentStatus.NEW
  }

  def cancel(Long cancelledByUserId, String cancelReason){
    status = AppointmentStatus.CANCELLED
    cancelledDate = new Date()
    this.cancelledByUserId = cancelledByUserId
    this.cancelReason = cancelReason
  }

  def confirm(Long confirmingUserId){
    status = AppointmentStatus.CONFIRMED
    confirmedDate = new Date()
    this.confirmedByUserId = confirmingUserId
  }
}
