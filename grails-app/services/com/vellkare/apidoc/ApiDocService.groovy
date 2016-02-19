package com.vellkare.apidoc

import grails.transaction.Transactional
import io.swagger.util.Json;

@Transactional
class ApiDocService {

  def swaggerConfig

  /*
   * generates SWAGGer JSON
   */

  def generateJSON() {

    String[] schemes = ["http"] as String[]
    swaggerConfig.setSchemes(schemes)
    swaggerConfig.setScan(true)
    def swagger = swaggerConfig.getSwagger()


    Json.mapper().writeValueAsString(swagger);
  }
}
