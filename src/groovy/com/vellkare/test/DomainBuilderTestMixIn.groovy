package com.vellkare.test

import com.vellkare.core.Login
import com.vellkare.core.Member

public class DomainBuilderTestMixIn {
    static def memberCounter = 0

    def buildMember(def properties=[:]) {
        def counter = properties.remove('memberCounter') ?: memberCounter++

        def memberProperties = [
            firstName:"firstName",
            lastName:'lastName',
            email:"email-${memberCounter}@abc.com",
            phone:123456789,
            description:'test description'
        ] << properties

        def login = buildLogin([memebrCounter:memberCounter])
        Member m = new Member(memberProperties)
        m.login = login
        if( hasProperty("mockMemberService") ) {
            m.memberService = mockMemberService
        }

        m.save(failOnError:true,flush:true)
    }

    def buildLogin(def properties=[:]) {
        def counter = properties.remove('memberCounter') ?: memberCounter++
        def loginProperties = [
                username:"username${counter}",
                password:'password',
                enabled:true
        ] << properties

        Login login = new Login(loginProperties)
        if( hasProperty("mockSpringSecurityService") ) {
            login.springSecurityService = mockSpringSecurityService
        }
        login.save(failOnError: true,flush:true)
    }
}
