package com.vellkare.core

import grails.transaction.Transactional


class AppointmentService {
  static transactional = false

  def grailsApplication
  def mailService
  def contentService

  @Transactional
  def makeAnAppointmet(Member member, Doctor doctor, Hospital hospital, Lab lab,
                       Date fromTime, Date toTime, String notes) {
    def appointment = new Appointment(member: member, doctor: doctor, hospital:hospital, lab: lab,
    fromTime: fromTime, toTime: toTime, notes: notes,
      type: doctor?Appointment.AppointmentType.DOCTOR_APPOINTMENT:Appointment.AppointmentType.LAB_DIAGNOSIS)
    appointment.save(flush: true, failOnError: true)
    if (grailsApplication.config.app?.emails?.newAppointment && member.email) {
      mailService.sendMail {
        to member.email
        subject "Your booking request is being looked into!"
        def model = [appointment: appointment]
        model << [ appointmentType:  appointment.type==Appointment.AppointmentType.DOCTOR_APPOINTMENT?'doctor': 'lab']
        def imageName = (appointment.doctor?appointment.doctorId:appointment.labId)+'.jpeg'
        model << [imageLink:"${grailsApplication.config.grails.serverURL}/images/${model.appointmentType}/${imageName}" ]
        def bodyArgs = [view: '/emails/appointment/new', model: model]

        def mailHtml = contentService.applyTemplate(bodyArgs.view, bodyArgs.model)
        html(mailHtml)
      }
    }
    appointment
  }

  @Transactional
  def cancelAppointment(Appointment app, Long cancellingUserId = null, String cancelReason = null ){
    if(app.canCancel()){
      app.cancel(cancellingUserId,cancelReason)
      app.save(flush: true, failOnError: true)

      if (grailsApplication.config.app?.emails?.cancelAppointment && app.member.email) {
        mailService.sendMail {
          to app.member.email
          subject "Your booking request is Cancelled!"
          def bodyArgs = [view: '/emails/appointment/cancel', model: [appointment: app]]
          def mailHtml = contentService.applyTemplate(bodyArgs.view, bodyArgs.model)
          html(mailHtml)
        }
      }
    }
  }

  @Transactional
  def confirmAppointment(Appointment app, Long confirmingUserId = null ){
    if(app.canConfirm()){
      app.confirm(confirmingUserId)
      app.save(flush: true, failOnError: true)
      if (grailsApplication.config.app?.emails?.confirmAppointment && app.member.email) {
        mailService.sendMail {
          to app.member.email
          subject "Your appointment is Confirmed!"
          def bodyArgs = [view: '/emails/appointment/confirmation', model: [appointment: app]]
          def mailHtml = contentService.applyTemplate(bodyArgs.view, bodyArgs.model)
          html(mailHtml)
        }
      }
    }
  }

  def listAppointments(Member member){
    def upcomingBookings = Appointment.createCriteria().list{
      ne('status',Appointment.AppointmentStatus.CANCELLED)
      gt('fromTime', new Date().clearTime())
      order('fromTime', 'asc')
    }

    def pastBookings = Appointment.createCriteria().list{
      lt('toTime', (new Date()+1).clearTime())
      not{'in'('status', [Appointment.AppointmentStatus.CANCELLED, Appointment.AppointmentStatus.NO_SHOW])}
      order('toTime', 'desc')
    }
    [upcoming:upcomingBookings, past:pastBookings]
  }
}
