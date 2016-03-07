package com.vellkare.core

import org.codehaus.groovy.grails.validation.Validateable

import javax.ws.rs.Produces

@Produces(["application/json", "application/xml"])
class SearchController {

  static namespace = 'v0'
  static responseFormats = ['json', 'xml']
  static allowedMethods = [categories: 'GET', list: 'POST']

  def memberService
  def securityService

  def listSpecialitiesAndHospitals(String location) {
    if (!location) {
      location = 'Hyderabad'
    }
    def specialities = Speciality.findAll().collect { it.name?.toLowerCase()?.capitalize() }.unique()
    def hospitalNames = Hospital.findAllWhere(city: location).collect { it.name?.toLowerCase()?.capitalize() }.unique()
    respond (specialties: specialities, hospitals: hospitalNames)
  }

  def listHospitals(SearchCommand cmd){
    cmd.location = cmd.location?:'Hyderabad'
    def hospitals =  Hospital.createCriteria().list{
      if(cmd.speciality) {
        ilike('specialists', "%${cmd.speciality}%")
      }
      eq('city', cmd.location)
    }
    respond (location: cmd.location, specialty: cmd.speciality, hospitals: hospitals)
  }

  def listHospitalSpecialities(SearchCommand cmd){
    cmd.location = cmd.location?:'Hyderabad'
    def specialities =  Hospital.findByCityAndName(cmd.location, cmd.hospital)?.specialists?.tokenize(',')*.toLowerCase()*.capitalize()
    respond (location: cmd.location, hospital: cmd.hospital, specialities: specialities)
  }

  def searchDoctor(SearchCommand cmd) {
    cmd.location = cmd.location ?: 'Hyderabad'
    def doctors = []
    if (!cmd.hospital && cmd.speciality) {
      doctors = Doctor.createCriteria().list {
        eq('city', cmd.location)
        ilike('specialists', "%${cmd.speciality}%")
      }
    } else if (cmd.hospital && cmd.speciality) {
      doctors = DoctorHospital.createCriteria().list {
        projections {
          property('doctor')
        }
        alias 'hospital', 'h'
        ilike('h.name', "%${cmd.hospital}%")
      }.collect {
        it.specialites*.name*.toLowerCase().contains(cmd.speciality.toLowerCase())
      }
    }
  }

}

@Validateable
class SearchCommand {

  String addressLine
  String city
  String district
  String state
  Gender gender
  String doctorName
  String speciality
  String hospital
  String location

}
