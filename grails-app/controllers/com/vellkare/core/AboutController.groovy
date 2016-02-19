package com.vellkare.core

import com.vellkare.api.RestMessage
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/about")
@Api(value = "/about", description = "Operations about users")
@Produces(["application/json", "application/xml"])
class AboutController {

  static responseFormats = ['json', 'xml']

  @GET
  @ApiOperation(value = "Api Info", notes = "public api", tags = ["all.public"])
  @ApiResponses(value = [
    @ApiResponse(code = 200, message = "info about API", response = RestMessage.class)
  ])
  def index() {
    respond new RestMessage("api.info", [])
  }

}
