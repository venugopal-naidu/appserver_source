package logging

import groovy.util.logging.Log4j

@Log4j
class LoggingFilters {

  def grailsApplication

  def filters = {
    all(controller: '*', action: '*') {
      before = {
        println(">>>>>>>>>>>>>>>> started: ${controllerName}.${actionName} request: ${request.requestURI} params: ${params}")
      }
      after = { Map model ->
        println(">>>>>>>>>>>>>>>> completed: ${controllerName}.${actionName} request: ${request.requestURI} ")
      }
      afterView = { Exception e ->
        if (e)
          println(">>>>>>>>>>>>>>>> exception: ${controllerName}.${actionName} request: ${request.requestURI}")
      }
    }
  }
}
