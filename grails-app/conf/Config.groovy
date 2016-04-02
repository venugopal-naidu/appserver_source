// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.app.context = "/"
grails.project.groupId = "com.vellkare" // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
                      all          : '*/*', // 'all' maps to '*' or the first available format in withFormat
                      atom         : 'application/atom+xml',
                      css          : 'text/css',
                      csv          : 'text/csv',
                      form         : 'application/x-www-form-urlencoded',
                      html         : ['text/html', 'application/xhtml+xml'],
                      js           : 'text/javascript',
                      json         : ['application/json', 'text/json'],
                      multipartForm: 'multipart/form-data',
                      rss          : 'application/rss+xml',
                      text         : 'text/plain',
                      hal          : ['application/hal+json', 'application/hal+xml'],
                      xml          : ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
  views {
    gsp {
      encoding = 'UTF-8'
      htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
      codecs {
        expression = 'html' // escapes values inside ${}
        scriptlet = 'html' // escapes output from scriptlets in GSPs
        taglib = 'none' // escapes output from taglibs
        staticparts = 'none' // escapes output from static template parts
      }
    }
    // escapes all not-encoded output at final stage of outputting
    // filteringCodecForContentType.'text/html' = 'html'
  }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart = false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

grails.cache.enabled = true

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {
  development {
    grails.logging.jul.usebridge = true
    grails.serverURL = "http://localhost:8080"
  }
  production {
    grails.serverURL = "http://www.velkare.com"
    grails.logging.jul.usebridge = false
    // TODO: grails.serverURL = "http://www.changeme.com"
  }
}

// log4j configuration
log4j.main = {
  // Example of changing the log pattern for the default console appender:
  //
  //appenders {
  //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
  //}

  error 'org.codehaus.groovy.grails.web.servlet',        // controllers
    'org.codehaus.groovy.grails.web.pages',          // GSP
    'org.codehaus.groovy.grails.web.sitemesh',       // layouts
    'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
    'org.codehaus.groovy.grails.web.mapping',        // URL mapping
    'org.codehaus.groovy.grails.commons',            // core / classloading
    'org.codehaus.groovy.grails.plugins',            // plugins
    'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
    'org.springframework',
    'org.hibernate',
    'net.sf.ehcache.hibernate'

  info 'com.vellkare',
    'grails.app.controllers.com.vellkare'

  debug 'com.vellkare.core'

  trace 'com.vellkare.SimpleTraceInterceptor'

  environments {
    test {
      info 'com.vellkare.core'
//            debug 'org.springframework.http.client'
//            debug 'org.springframework.web.client'
    }
  }

//  debug 'org.hibernate.SQL'

}

grails {
  mail {
    host = "smtp.gmail.com"
    port = 587
    username = "vellkare.dev@gmail.com"
    password = "vellkare"
    props = ["mail.debug"               : "true",
             "mail.smtp.protocol"       : "smtps",
             "mail.smtp.auth"           : "true",
             "mail.smtp.starttls.enable": "true",
             "mail.smtp.host"           : "smtp.gmail.com",
             "mail.smtp.user"           : "vellkare.dev@gmail.com",
             "mail.smtp.password"       : "vellkare"]
    disabled = false
  }
}

grails.mail.default.from = "vellkare.dev@gmail.com"

grails.plugin.springsecurity.rejectIfNoRule = false
grails.plugin.springsecurity.fii.rejectPublicInvocations = false

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.vellkare.core.Login'
//grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.vellkare.core.LoginRole'
//grails.plugin.springsecurity.authority.className = 'com.vellkare.core.Role'

// Added by the Spring Security OAuth2 Provider plugin:
grails.plugin.springsecurity.oauthProvider.clientLookup.className = 'com.vellkare.oauth.OAuthClient'
grails.plugin.springsecurity.oauthProvider.authorizationCodeLookup.className = 'com.vellkare.oauth.OAuthAuthorizationCode'
grails.plugin.springsecurity.oauthProvider.refreshTokenLookup.className = 'com.vellkare.oauth.OAuthRefreshToken'


grails {
  plugin {
    springsecurity {
      oauthProvider {
        accessTokenLookup {
          className = 'com.vellkare.oauth.OAuthAccessToken'
          authenticationKeyPropertyName = 'authenticationKey'
          authenticationPropertyName = 'authentication'
          authenticationJsonPropertyName = 'authenticationJson'
          usernamePropertyName = 'username'
          clientIdPropertyName = 'clientId'
          valuePropertyName = 'value'
          tokenTypePropertyName = 'tokenType'
          expirationPropertyName = 'expiration'
          refreshTokenPropertyName = 'refreshToken'
          scopePropertyName = 'scope'
        }
        tokenServices {
          accessTokenValiditySeconds = 60 * 60 // 60 mins
        }
      }
    }
  }
}

vellkare {
  media {
    healthRecord {
      maxSize = 1024*1024*10  // maximum 10 MB
      supportedTypes = ['tif', 'tiff', 'gif', 'jpeg', 'jpg',
                        'jif', 'jfif', 'jp2', 'jpx',  'j2k',
                        'j2c', 'fpx',  'pcd', 'png',  'pdf']
    }
  }
}

grails.plugin.springsecurity.providerNames = [
  'clientCredentialsAuthenticationProvider',
  'daoAuthenticationProvider',
  'anonymousAuthenticationProvider',
  'rememberMeAuthenticationProvider'
]

grails.exceptionresolver.params.exclude = ['password', 'client_secret']

grails.plugin.springsecurity.filterChain.chainMap = [
  '/oauth/token': 'JOINED_FILTERS,-oauth2ProviderFilter,-securityContextPersistenceFilter,-logoutFilter,-rememberMeAuthenticationFilter',
  '/api/**'     : 'JOINED_FILTERS,-securityContextPersistenceFilter,-logoutFilter,-rememberMeAuthenticationFilter',
  '/**'         : 'JOINED_FILTERS,-statelessSecurityContextPersistenceFilter',
]

cors.url.pattern = '/*'
cors.headers = ['Access-Control-Allow-Origin': '*']

logging.controller.enabled = true

grails.client.config.clientId = 'velkare-client'

app {
  emails {
    welcome = false
    otpVerification = true
    newAppointment = true
    cancelAppointment = false
    confirmAppointment = false

  }
  sms{
    welcome = false
    otpVerification = false
  }
}

verification.otp.size=10

images.doctors.path='/images/doctor/'
images.labs.path='/images/lab/'

grails.config.locations = ["file:/opt/tomcat/conf/velkare-config.groovy", "file:/usr/share/tomcat/conf/velkare-config.groovy"]

grails.mail.overrideAddress="" // send all emails to this email address

grails.plugin.console.enabled=true

