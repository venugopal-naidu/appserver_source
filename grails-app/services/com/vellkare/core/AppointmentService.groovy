package com.vellkare.core

import grails.transaction.Transactional


class AppointmentService {
  static transactional = false

  def grailsApplication
  def mailService
  def contentService
  def makeAnAppointmet(Member member, Doctor doctor, Hospital hospital, Lab lab,
                       Date fromTime, Date toTime, String notes) {
    def appointment = new Appointment(member: member, doctor: doctor, hospital:hospital,
    fromTime: fromTime, toTime: toTime, notes: notes,
      type: doctor?Appointment.AppointmentType.DOCTOR_APPOINTMENT:Appointment.AppointmentType.LAB_DIAGNOSIS)
    appointment.save(flush: true, failOnError: true)
    if (grailsApplication.config.app?.emails?.newAppointment && m.email) {
      mailService.sendMail {
        to m.email
        subject "Your booking request is being looked into!"
        def bodyArgs = [view: '/emails/appointment/new', model: [member: m]]
        def mailHtml = contentService.applyTemplate(bodyArgs.view, bodyArgs.model)
        html(mailHtml)
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
      lt('endTime', (new Date()+1).clearTime())
      not(
        'in'('status', [Appointment.AppointmentStatus.CANCELLED,
                        Appointment.AppointmentStatus.NO_SHOW])
      )
      order('endTime', 'desc')
    }
    [upcoming:upcomingBookings, past:pastBookings]
  }
}
