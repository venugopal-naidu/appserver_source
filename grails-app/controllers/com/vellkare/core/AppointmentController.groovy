package com.vellkare.core

import com.vellkare.api.FieldErrorApiModel
import com.vellkare.api.ValidationErrorResponse
import com.vellkare.util.DateUtil
import grails.plugin.springsecurity.annotation.Secured
import org.apache.http.HttpStatus
import org.codehaus.groovy.grails.validation.Validateable

import javax.ws.rs.Produces
import java.text.SimpleDateFormat

@Produces(["application/json", "application/xml"])
class AppointmentController {
  static namespace = 'v0'
  static responseFormats = ['json', 'xml']

  def appointmentService
  def securityService

  @Secured("#oauth2.isUser()")
  def makeAnAppointment(AppointmentCommand cmd) {

    def member = securityService.currentMember
    if (!cmd.validate()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse(cmd.errors)
      log.debug "cmd errors : " + cmd.errors
      return
    }

    Doctor d = cmd.doctorId ? Doctor.get(cmd.doctorId) : null

    Hospital h = cmd.hospitalId ? Hospital.get(cmd.hospitalId) : null

    Lab l = cmd.labId ? Lab.get(cmd.labId) : null

    if(!d && !h && !l){
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('doctorId', 'appointment.doctor.select', [])])
      return
    }


    Appointment app = appointmentService.makeAnAppointmet(member, d, h, l, cmd.appointmentFromTime(),
      cmd.appointmentToDate(), cmd.notes);
    respond(transform(app))
  }

  @Secured("#oauth2.isUser()")
  def cancelAppointment(Long appointmentId) {
    Appointment app = Appointment.get(appointmentId)
    if (!app || !app.canCancel()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('appointmentId', 'appointment.cancel.rejected', [])])
      return
    }
    appointmentService.cancelAppointment(app)
    respond(cancel: 'SUCCESS', appointmentId: appointmentId)
  }

//  @Secured(value=["hasRole('ROLE_CSR')"], httpMethod='GET')
  def confirmAppointment(Long appointmentId) {
    Appointment app = Appointment.get(appointmentId)
    if (!app || !app.canConfirm()) {
      response.setStatus(HttpStatus.SC_BAD_REQUEST)
      respond new ValidationErrorResponse([new FieldErrorApiModel('appointmentId', 'appointment.confirm.rejected', [])])
      return
    }
    appointmentService.confirmAppointment(app)
    respond(confirmed: 'SUCCESS', appointmentId: appointmentId)

  }

  @Secured("#oauth2.isUser()")
  def listAppointments() {
    def member = securityService.currentMember
    def appointments = appointmentService.listAppointments(member)

    appointments.past = appointments.past.collect { transform(it) }
    appointments.upcoming = appointments.upcoming.collect { transform(it) }
    respond(appointments)
  }

  def transform(Appointment app){
    AppointmentMini mini = new AppointmentMini()
    mini.appointmentId = app.id
    mini.appointmentType = app.type.name()
    if(app.doctor) {
      mini.doctorId = app.doctor.id
      mini.doctorDetails = app.doctor.name
    }

    if(app.hospital) {
      mini.hospitalId = app.hospital.id
      mini.hospitalDetails = app.hospital.name
    }
    if(app.lab) {
      mini.labId = app.lab.id
      mini.labDetails = app.lab.name
    }

    mini.appointmentDate= DateUtil.getPrintableDateString(app.fromTime)
    mini.fromTime = DateUtil.getPrintableTimeString(app.fromTime)
    mini.toTime = DateUtil.getPrintableTimeString(app.toTime)
    mini.appointmentCreatedDate = DateUtil.getPrintableDateTimeString(app.bookingDate)
    mini.status = app.status.name()
    mini.hasMedicalRecords = MedicalRecord.countByAppointment(app)>0
    mini
  }
}


class AppointmentCommand {

  String fromTime
  String toTime
  Long doctorId
  Long hospitalId
  Long labId
  String notes

  static String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

  SimpleDateFormat dfTime = new SimpleDateFormat(DATE_TIME_FORMAT)

  static constraints = {
    notes nullable: true
    doctorId nullable: true , validator: { val, cmd, errors ->
      if(val){
        Doctor d = Doctor.get(val)
        if(!d){
          errors.rejectValue("doctorId","doctorId.invalid","Please select valid doctor")
        }
      }
    }
    hospitalId nullable: true , validator: { val, cmd, errors ->
      if(val){
        Hospital d = Hospital.get(val)
        if(!d){
          errors.rejectValue("hospitalId","hospitalId.invalid","Please select valid hospital")
        }
      }
    }
    labId nullable: true , validator: { val, cmd, errors ->
      if(val){
        def d = Lab.get(val)
        if(!d){
          errors.rejectValue("labId","labId.invalid","Please select valid Lab")
        }
      }
    }

  }

  Date appointmentFromTime(){
    dfTime.parse(fromTime)
  }

  Date appointmentToDate(){
    dfTime.parse(toTime)
  }

}