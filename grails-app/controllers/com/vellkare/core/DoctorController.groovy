package com.vellkare.core

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException

import javax.ws.rs.Produces

@Produces(["application/json", "application/xml"])
class DoctorController {
  static responseFormats = ['json', 'xml']
  static namespace = 'v0'

  static allowedMethods = [save: "POST", update: "POST", delete: "POST", show: "GET"]

  def doctorService

  def index() {
    list()
  }

//  @Secured(["#oauth2.isUser() or #oauth2.isClient() "])
  def list(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond ([doctorList: Doctor.list(params), doctorCount: Doctor.count()]) as JSON
  }
  def create() {
    [doctorInstance: new Doctor(params)]
  }
  @Transactional
  @Secured(["#oauth2.isUser() or #oauth2.isClient() "])
  def save() {
    Doctor doctorInstance = new Doctor(params)
    def specialities = params.list 'specialities'
    if(specialities){
      doctorInstance.specialities.addAll(specialities)
    }
    doctorInstance.save(flush:true, failOnError:true)
//    doctorService.save(doctorInstance)
    list()
  }
  def show(Long id) {
    def doctorInstance = Doctor.get(id)
    respond doctorInstance
  }
  def edit(Long id) {
    def personInstance = Doctor.get(id)
    if (!personInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'doctor.label', default: 'Doctor'), id])
      redirect(action: "list")
      return
    }
    [personInstance: personInstance]
  }
  def update(Long id, Long version) {
    def doctorInstance = Doctor.get(id)
    if (!doctorInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'doctor.label', default: 'Doctor'), id])
      redirect(action: "list")
      return
    }
    if (version != null) {
      if (doctorInstance.version > version) {
        doctorInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
          [message(code: 'doctor.label', default: 'Doctor')] as Object[],
          "Another user has updated this Person while you were editing")
        render(view: "edit", model: [doctorInstance: doctorInstance])
        return
      }
    }
    doctorInstance.properties = params
    if (!doctorInstance.save(flush: true)) {
      render(view: "edit", model: [doctorInstance: doctorInstance])
      return
    }
    flash.message = message(code: 'default.updated.message', args: [message(code: 'doctor.label', default: 'Doctor'), doctorInstance.id])
    redirect(action: "show", id: doctorInstance.id)
  }
  def delete(Long id) {
    def doctorInstance = Doctor.get(id)
    if (!doctorInstance) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'doctor.label', default: 'Doctor'), id])
      redirect(action: "list")
      return
    }
    try {
      doctorInstance.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [message(code: 'doctor.label', default: 'Doctor'), id])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'doctor.label', default: 'Doctor'), id])
      redirect(action: "show", id: id)
    }
  }
}
