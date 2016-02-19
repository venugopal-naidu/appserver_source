package com.vellkare.core

import com.vellkare.api.RestMessage
import grails.plugin.springsecurity.annotation.Secured
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/v0/sample")
@Api(value = "/v0/sample", description = "Operations about users")
@Produces(["application/json", "application/xml"])
class SampleApiController {

  static namespace = 'v0'
  static responseFormats = ['json', 'xml']

  @GET
  @Path("/about")
  @ApiOperation(value = "Api Info", notes = "public api", tags = ["all.public", "/v0/sample"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "info about API", response = RestMessage.class)
  ])
  def about() {
    respond new RestMessage("sample.api.info", [])
  }

  @GET
  @Path("/securedWithOAuth")
  @ApiOperation(value = "Secured By OAuth", notes = "Accessing Securing API using OAuth", tags = ["all.private", "/v0/sample"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "secure response", response = RestMessage.class)
  ])
  @Secured("#oauth2.isOAuth()")
  def securedWithOAuth() {
    respond new RestMessage("sample.api.secure.oauth", [])
  }


  @GET
  @Path("/securedWithUserOAuth")
  @ApiOperation(value = "Secured By user OAuth", notes = "Accessing Securing API using User OAuth", tags = ["all.private", "/v0/sample"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "secure response", response = RestMessage.class)
  ])
  @Secured("#oauth2.isUser()")
  def securedWithUserOAuth() {
    respond new RestMessage("sample.api.secure.userOAuth", [])
  }


  @GET
  @Path("/securedWithClientOAuth")
  @ApiOperation(value = "Secured By Client OAuth", notes = "Accessing Securing API using Client OAuth", tags = ["all.private", "/v0/sample"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "secure response", response = RestMessage.class)
  ])
  @Secured("#oauth2.isClient()")
  def securedWithClientOAuth() {
    respond new RestMessage("sample.api.secure.clientOAuth", [])
  }

}
