package com.vellkare

import com.vellkare.api.FieldErrorApiModel
import com.vellkare.api.RestMessage
import com.vellkare.core.AvailabilityMini
import com.vellkare.core.Doctor
import com.vellkare.core.DoctorHospital
import com.vellkare.core.DoctorHospitalMini
import com.vellkare.core.HospitalAvailabilityMini
import com.vellkare.core.Member
import com.vellkare.core.MemberApiModel
import com.vellkare.core.SocialAccountApiModel
import com.vellkare.social.SocialConnection
import grails.converters.JSON
import org.codehaus.groovy.grails.commons.GrailsClassUtils

import java.beans.PropertyDescriptor

class CustomMarshallerRegistrar {

    private static String getTypeValue(Class cls, String name, Object val) {
        PropertyDescriptor desc = GrailsClassUtils.getProperty(cls, name)
        def value
        println "name : "+name
        if(desc.propertyType == String.class) {
            println "in if : "+val
            value = (val == null) ? '' : val
            println "value : "+value
        }
        else if(desc.propertyType == Number.class || desc.propertyType == Long.class ) {
            println "in else : "+val
            value = (val == null) ? 0 : val
            println "value : "+value
        }
        return value
    }

    void registerMarshallers() {

        JSON.registerObjectMarshaller(Enum) {
            return it.name()
        }

        JSON.registerObjectMarshaller(RestMessage) {
            def map = [:]
            map['messageCode'] = it?.messageCode ?: ''
            map['message'] = it?.message ?: ''
            return map
        }

        JSON.registerObjectMarshaller(FieldErrorApiModel) {
            def map = ['messageCode', 'message', 'objectName', 'propertyName'].collectEntries { name ->
                def val = it?."${name}"
                [(name): ( val == null) ? '' : val ]
            }
            return map
        }

        JSON.registerObjectMarshaller(com.vellkare.core.Member) {
            def map = ['id','email','firstName','lastName','primaryPhone','description'].collectEntries { name ->
                def val = it?."${name}"
                [(name): ( val == null) ? '' : val ]
            }
            map['socialAccounts'] = SocialConnection.findAllByLogin(it?.login).collect{
                [id: it?.id, email: it?.email, socialType: it?.providerId, imageUrl: it?.imageUrl]
            } ?: []
            return map
        }

        Member.metaClass.asType = { Class clazz ->
            Member member = delegate
            if( clazz == MemberApiModel ) {
                MemberApiModel m = new MemberApiModel()
                ['id','email','firstName','lastName','primaryPhone','description'].each { name ->
                    def val = member."${name}"
                    /*println "in astype val : "+val
                    m."${name}" = getTypeValue(m.getClass(), name, val)
                    println "in astype m.name : "+m."${name}"*/
                    PropertyDescriptor desc = GrailsClassUtils.getProperty(m.getClass(), name)
                    if (desc.propertyType == String.class) {
                        m."${name}" = (val == null) ? '' : val
                    } else if (desc.propertyType == Number.class || desc.propertyType == Long.class) {
                        m."${name}" = (val == null) ? 0 : val
                    }
                }
                m.socialAccounts =  SocialConnection.findAllByLogin(member?.login).collect{
                    new SocialAccountApiModel(id: it?.id, email: it?.email, socialType: it?.providerId, imageUrl: it?.imageUrl)
                } ?: []
                return m
            }
            return null
        }

    }

}