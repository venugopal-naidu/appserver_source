package com.vellkare.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import grails.util.Holders

@JsonIgnoreProperties(["properties","validationTagLib"])
public class RestMessage {

    String messageCode;
	String message;

    def validationTagLib

    public RestMessage(String code, def args) {
        messageCode=code
        message=translateMessage(code,args)
    }

    public String translateMessage(code,args){
        getValidationTagLib().message(code:code,args:args)
    }

    public getValidationTagLib() {
        if(! this.@validationTagLib ) this.@validationTagLib = Holders.grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib')
        return this.@validationTagLib
    }
}
