package com.vellkare.core

import com.vellkare.util.BeanUtil
import org.codehaus.groovy.grails.validation.Validateable

import javax.ws.rs.Produces

@Produces(["application/json", "application/xml"])
class SearchController {

  static namespace = 'v0'
  static responseFormats = ['json', 'xml']

  def memberService
  def securityService
  def grailsApplication

  def listSpecialitiesAndHospitals(String location) {
    if (!location) {
      location = 'Hyderabad'
    }
    def specialties = Speciality.findAll().collect { it.name?.toLowerCase()?.capitalize() }
      .grep().unique().sort()
    def hospitalNames = Hospital.findAllWhere(city: location).collect { it.name?.toLowerCase()?.capitalize() }
      .grep().unique().sort()
    respond (specialties: specialties, hospitals: hospitalNames)
  }

  def listHospitals(SearchCommand cmd){
    cmd.location = cmd.location?:'Hyderabad'
    def hospitals =  Hospital.createCriteria().list{
      if(cmd.specialty) {
        ilike('specialists', "%${cmd.specialty}%")
      }
      eq('city', cmd.location)
    }
    respond (location: cmd.location, specialty: cmd.specialty, hospitals: hospitals)
  }


  def listHospitalsNames(SearchCommand cmd){
    cmd.location = cmd.location?:'Hyderabad'
    def hospitals =  Hospital.createCriteria().list{
      projections{
        property 'name'
      }
      if(cmd.specialty) {
        ilike('specialists', "%${cmd.specialty}%")
      }
      eq('city', cmd.location)
    }*.toLowerCase()*.capitalize().sort().unique()
    respond (location: cmd.location, specialty: cmd.specialty, hospitals: hospitals)
  }



  def listHospitalSpecialities(SearchCommand cmd){
    cmd.location = cmd.location?:'Hyderabad'
    def specialties = Hospital.createCriteria().list{
      if(cmd.hospital) {
        ilike('name', "%${cmd.hospital}%")
      }
      if(cmd.location) {
        ilike('city', "%${cmd.location}%")
      }
    }.collect{
      it.specialists?.tokenize(',')*.toLowerCase()*.trim()*.capitalize()
    }.flatten().grep().unique().sort()

    respond (location: cmd.location, hospital: cmd.hospital, specialties: specialties)
  }

  def searchDoctor(SearchCommand cmd) {
    cmd.location = cmd.location ?: 'Hyderabad'
    def doctors = DoctorHospital.createCriteria().list{
      createAlias('hospital', 'hospital')
      createAlias('doctor', 'doctor')
      projections{
        distinct 'doctor'
      }
      if(cmd.hospital) {
        ilike('hospital.name', "%${cmd.hospital}%")
      }
      if(cmd.location) {
        ilike('hospital.city', "%${cmd.location}%")
      }
      if(cmd.specialty) {
        ilike('hospital.specialists', "%${cmd.specialty}%")
      }
    }.collect{ doctor->
      BeanUtil.transformObject(doctor.class, DoctorHospitalMini, doctor)
    }

    respond (location: cmd.location, hospital: cmd.hospital, specialty: cmd.specialty,
    doctors:doctors)
  }

  def listTestAndLabsNames(SearchCommand cmd){
    def tests = LabPackageTest.createCriteria().list{ createAlias('test', 't')
      projections {
        distinct 't.name'
      }
    }*.toLowerCase()*.capitalize().sort()

    def labs = LabPackageTest.createCriteria().list{ createAlias('lab', 'l')
      projections {
        distinct 'l.name'
      }
    }*.toLowerCase()*.capitalize().sort()
    respond (location:cmd.location, tests: tests, labs:labs)
  }

  def listLabsForTest(SearchCommand cmd){
    def labs = LabPackageTest.createCriteria().list{
      createAlias('test', 't')
      createAlias('lab', 'l')
      projections {
        property 'l.name'
      }
      if(cmd.testName)
        ilike('t.name', "%${cmd.testName}%")
    }.unique()*.toLowerCase()*.capitalize()

    respond( testName :cmd.testName, location:cmd.location, labs: labs )
  }

  def listTestInLab(SearchCommand cmd){
    def tests = LabPackageTest.createCriteria().list{
      createAlias('test', 't')
      createAlias('lab', 'l')
      projections {
        property 't.name'
      }
      if(cmd.labName)
        ilike('l.name', "%${cmd.labName}%")
    }.unique()*.toLowerCase()*.capitalize().sort()

    respond( labName :cmd.labName, location:cmd.location, tests: tests )

  }
  def listLabs(SearchCommand cmd){
    def labs = LabPackageTest.createCriteria().list{
      createAlias('test', 't')
      createAlias('lab', 'l')
      projections {
        distinct 'lab'
      }
      if(cmd.testName)
        ilike('t.name', "%${cmd.testName}%")
      if(cmd.labName)
        ilike('l.name', "%${cmd.labName}%")

    }.collect { BeanUtil.transformObject(Lab, LabMini, it)}
    respond( testName :cmd.testName, labName:cmd.labName, location:cmd.location, labs: labs )
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
  String specialty
  String hospital
  String location
  String testName
  String labName
}
