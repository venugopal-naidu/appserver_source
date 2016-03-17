package com.vellkare.core

import groovy.text.Template
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine
import org.codehaus.groovy.grails.web.util.GrailsPrintWriter

/**
 * Created by roopesh on 17/03/16.
 */
class ContentService {
  static transactional = false
  def grailsApplication
  def groovyPagesUriService

  def applyTemplate(String templateName, final Map model = [:]) {
    StringWriter sw = null
    try {
      GroovyPagesTemplateEngine engine = grailsApplication.mainContext.getBean(GroovyPagesTemplateEngine)
      String resolvedName = groovyPagesUriService.getAbsoluteTemplateURI(templateName)

      def scriptSource = engine.findScriptSource(resolvedName)
      Template template = engine.createTemplate(scriptSource)

      def writable = template.make(model)
      sw = new StringWriter()
      writable.writeTo(new GrailsPrintWriter(sw))
    } catch (e) {
      log.error "Error rendering template for email: ${templateName}", e
    }
    return sw?.toString()
  }

}
