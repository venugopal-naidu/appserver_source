package com.vellkare.core

import org.codehaus.groovy.grails.validation.Validateable

class SearchController {

  static namespace = 'v0'
  static responseFormats = ['json', 'xml']
  static allowedMethods = [categories: 'GET', list: 'POST']

  def memberService
  def securityService


  def searchDoctor(SearchCommand cmd) {


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
  String hospitalName

}
