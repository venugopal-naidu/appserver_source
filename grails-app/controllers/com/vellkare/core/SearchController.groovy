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

  def transformObject (def fromType , def toType, def fromObj){

    if(fromObj instanceof Doctor && toType == DoctorHospitalMini) {
      def doctor = fromObj
      DoctorHospitalMini d = new DoctorHospitalMini()
      ['id','name', 'description', 'website', 'velkareVerified',
       'degree1', 'univ1', 'degree2', 'univ2', 'degree3', 'univ3',
       'degree4', 'univ4', 'degree5', 'univ5', 'experience', 'awards', 'language'].each { name ->
        def val = doctor."${name}"
        d."${name}" = val
      }

      if (doctor.hospitals) {
        d.hospitals = doctor.hospitals.collect { DoctorHospital dh ->
          HospitalAvailabilityMini mini = new HospitalAvailabilityMini()
          mini.id = dh.hospital.id
          mini.name = dh.hospital.name
          mini.address1 = dh.hospital.address1
          mini.address2 = dh.hospital.address2
          mini.address3 = dh.hospital.address3
          mini.address4 = dh.hospital.address4
          ["MONDAY", 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY','SATURDAY','SUNDAY'].each {
            mini.availability.put ( it,[new AvailabilityMini(from: dh.serviceHoursFrom, to: dh.serviceHoursTo)] )
          }
          mini
        }
      }
      if(doctor.specialities){
        d.specialties = doctor.specialities.collect { it.speciality.name.toLowerCase().capitalize() }
      }
      def photoUrl = grailsApplication.config.images.doctors.path?:"/images/doctor/"
      photoUrl= "${photoUrl}${doctor.id}.jpeg"
      d.photoUrl=photoUrl
      return d
    }
    if(fromObj instanceof Lab && toType == LabMini) {
      def lab = fromObj
      LabMini d = new LabMini()
      ['id', 'name', 'address1', 'address2',
       'address3', 'address4', 'state', 'city', 'country', 'postalCode',
       'website','velkareVerified'].each { name ->
        def val = lab."${name}"
        d."${name}" = val
      }
      def photoUrl = grailsApplication.config.images.labs.path?:"/images/lab/"
      photoUrl= "${photoUrl}${lab.id}.jpeg"
      d.labImageUrl = photoUrl
      return d
    }
    return null
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
      transformObject(doctor.class, DoctorHospitalMini, doctor)
    }

    respond (location: cmd.location, hospital: cmd.hospital, specialty: cmd.specialty,
    doctors:doctors)
  }

  def listTestAndLabsNames(){
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
    respond (tests: tests, labs:labs)
  }

  def listLabsForTest(SearchCommand cmd){
    def labs = LabPackageTest.createCriteria().list{
      createAlias('test', 't')
      createAlias('lab', 'l')
      projections {
        property 'l.name'
      }
      if(cmd.testName)
        ilike('t.name', cmd.testName)
    }.unique()*.toLowerCase()*.capitalize()

    respond( testName :cmd.testName, labs: labs )
  }

  def listTestInLab(SearchCommand cmd){
    def tests = LabPackageTest.createCriteria().list{
      createAlias('test', 't')
      createAlias('lab', 'l')
      projections {
        property 't.name'
      }
      if(cmd.labName)
        ilike('l.name', cmd.labName)
    }.unique()*.toLowerCase()*.capitalize().sort()

    respond( labName :cmd.labName, tests: tests )

  }
  def listLabs(SearchCommand cmd){
    def labs = LabPackageTest.createCriteria().list{
      createAlias('test', 't')
      createAlias('lab', 'b')
      projections {
        distinct 'lab'
      }
      if(cmd.testName)
        ilike('t.name', cmd.testName)
      if(cmd.labName)
        ilike('l.name', cmd.labName)

    }.collect { transformObject(Lab, LabMini, it)}
    respond( testName :cmd.testName, labs: labs )
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
