package com.vellkare

import com.vellkare.api.RestMessage

class ErrorController {

  static responseFormats = ['json', 'xml']

  def handleNotFound() {
    log.error("In handleNotFound")
    respond new RestMessage('endpoint.notfound', [])
  }

  def handleForbidden() {
    log.error("In handleForbidden")
    respond new RestMessage('endpoint.forbidden', [])
  }

  def handleInvalidMethod() {
    log.error("In handleInvalidMethod")
    respond new RestMessage('endpoint.invalidMethod', [])
  }

  def handleError() {
    log.error("In Handling Error")
    if (request.exception) {
      log.error("Internal Error", request.exception)
    }
    respond new RestMessage('internal.error', [])
  }
}
