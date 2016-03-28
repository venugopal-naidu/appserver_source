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
    def specialties = DoctorHospital.createCriteria().list{
      createAlias('hospital', 'hospital')
      createAlias('doctor', 'doctor')
      createAlias('doctor.specialities', 'sps')
      createAlias('sps.speciality', 'speciality')
      projections{
        distinct 'speciality.name'
      }
      ilike('hospital.city', "%${location}%")
    }*.toLowerCase()*.capitalize().sort()

      def hospitalNames = DoctorHospital.createCriteria().list{
        createAlias('hospital', 'hospital')
        projections{
          distinct 'hospital.name'
        }
        ilike('hospital.city', "%${location}%")
      }*.toLowerCase()*.capitalize().sort()

    respond (specialties: specialties, hospitals: hospitalNames)
  }

  def listHospitals(SearchCommand cmd){
    cmd.location = cmd.location?:'Hyderabad'
    def hospitals =  DoctorHospital.createCriteria().list{
      createAlias('hospital', 'hospital')
      createAlias('doctor', 'doctor')
      createAlias('doctor.specialities', 'sps')
      createAlias('sps.speciality', 'speciality')
      projections{
        distinct 'hospital'
      }
      if(cmd.location) {
        ilike('hospital.city', "%${cmd.location}%")
      }
      if(cmd.specialty) {
        ilike('speciality.name', "%${cmd.specialty}%")
      }
    }
    respond (location: cmd.location, specialty: cmd.specialty, hospitals: hospitals)
  }


  def listHospitalsNames(SearchCommand cmd){
    cmd.location = cmd.location?:'Hyderabad'
    def hospitalNames = DoctorHospital.createCriteria().list{
      createAlias('hospital', 'hospital')
      createAlias('doctor', 'doctor')
      createAlias('doctor.specialities', 'sps')
      createAlias('sps.speciality', 'speciality')
      projections{
        distinct 'hospital.name'
      }
      if(cmd.location) {
        ilike('hospital.city', "%${cmd.location}%")
      }
      if(cmd.specialty) {
        ilike('speciality.name', "%${cmd.specialty}%")
      }
    }*.toLowerCase()*.capitalize().sort()
    respond (location: cmd.location, specialty: cmd.specialty, hospitals: hospitalNames)
  }

  def listHospitalSpecialities(SearchCommand cmd){
    cmd.location = cmd.location?:'Hyderabad'
    def specialties = DoctorHospital.createCriteria().list{
      createAlias('hospital', 'hospital')
      createAlias('doctor', 'doctor')
      createAlias('doctor.specialities', 'sps')
      createAlias('sps.speciality', 'speciality')
      projections{
        distinct 'speciality.name'
      }
      if(cmd.location) {
        ilike('hospital.city', "%${cmd.location}%")
      }
      if(cmd.hospital) {
        ilike('hospital.name', "%${cmd.hospital}%")
      }
    }*.toLowerCase()*.capitalize().sort()

    respond (location: cmd.location, hospital: cmd.hospital, specialties: specialties)
  }

  def searchDoctor(SearchCommand cmd) {
    cmd.location = cmd.location ?: 'Hyderabad'
    def doctors = DoctorHospital.createCriteria().list{
      createAlias('hospital', 'hospital')
      if(cmd.specialty) {
        createAlias('doctor', 'doctor')
        createAlias('doctor.specialities', 'sps')
        createAlias('sps.speciality', 'speciality')
      }
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
        ilike('speciality.name', "%${cmd.specialty}%")
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

  def doctorHospitalDetails(SearchCommand cmd){
    if(cmd.doctorId && cmd.hospitalId) {
      DoctorHospital docHos = DoctorHospital.createCriteria().get {
        createAlias('hospital', 'hospital')
        createAlias('doctor', 'doctor')
        eq('hospital.id', cmd.hospitalId)
        eq('doctor.id', cmd.doctorId)
      }
      if(docHos) {
        def doc = docHos.doctor
        def hos = docHos.hospital
        def data = [:]
        def doctorDetails = [id: doc.id, name: doc.name, degree1: doc.degree1, degree2: doc.degree2,
        degree3: doc.degree3, specialties : doc?.specialities?.collect{it.speciality.name.toLowerCase().capitalize()}?:[],
        gender: doc.gender.name(), imageURL:(grailsApplication.config.images.doctors.path?:"/images/doctor/")+doc.id+".jpeg"
        ]

        def hospitalDetails = [id: hos.id, name: hos.name, address1: hos.address1, address2: hos.address2,
                               address3:hos.address3]
        data.doctor = doctorDetails
        data.hospital = hospitalDetails
        respond (data)
        return
      }
    }
    respond([:])
  }

  def labDetails(SearchCommand cmd){
    if(cmd.labId) {
      Lab lab = Lab.get(cmd.labId)
      if(lab){
        def data = [ lab: [ id: lab.id, name: lab.name, address1: lab.address1, address2: lab.address2,
          address3:lab.address3, imageURL:(grailsApplication.config.images.labs.path?:"/images/lab/")+lab.id+".jpeg"
         ]
        ]
        respond (data)
        return
      }
    }
    respond([:])
    return
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
  Long doctorId
  Long hospitalId
  Long labId
}
