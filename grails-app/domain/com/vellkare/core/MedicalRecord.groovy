package com.vellkare.core

/**
 * Created by roopesh on 25/03/16.
 */
class MedicalRecord {
  Member member
  MedicalRecordType recordType
  Appointment appointment

  String name
  String notes
  Date recordDate
  Date uploadDate = new Date()
  Date deletedDate
  boolean deleted = false

  Media media

  static belongsTo = [member: Member]

  static constraints = {
    name nullable: true
    notes nullable: true, maxSize: 5000
    appointment nullable:true
    deletedDate nullable: true
    media nullable: true
    recordDate nullable: true
  }

  static mapping = {
    media cascade: "save-update,delete"
  }

  def deleteRecord(){
    deletedDate = new Date()
    deleted = true
  }
}
