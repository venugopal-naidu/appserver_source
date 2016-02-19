import com.vellkare.CustomBeanFactoryPostProcessor
import com.vellkare.CustomMarshallerRegistrar
import com.vellkare.TransactionLogger
import com.vellkare.api.ApiEntryPoint
import com.vellkare.oauth.OAuthTokenStoreService
import io.swagger.jaxrs.config.BeanConfig
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

// Place your Spring DSL code here
beans = {

  println "Configuring Starter App  ...."
  customMarshallerRegistrar(CustomMarshallerRegistrar)

  swaggerConfig(BeanConfig) {
    def serverUrl = grailsApplication.config.grails.serverURL.toString()
    def hostName = serverUrl.substring(serverUrl.indexOf("://") + 3)
    resourcePackage = 'com.vellkare.core,com.vellkare.social'
    host = hostName
    basePath = "/api"
    version = 'v0' // Default "1".
    title = 'Core Registration API, Version V0' // Default: App Name.
    description = 'API for signup & registration'
    contact = 'webservices@vellkare.com'
    license = ''
    licenseUrl = ''
  }

  //https://developer.jboss.org/wiki/OpenSessioninView?_sscc=t
  //https://objectpartners.com/2010/10/19/grails-plumbing-spring-aop-interceptors/
  //http://docs.spring.io/spring/docs/2.5.x/reference/aop.html
  xmlns aop: "http://www.springframework.org/schema/aop"   //[1]
  aop {
    config("proxy-target-class": true) {  //[2]
      pointcut(id: "oAuthInterceptPoint", expression: "execution(* com.vellkare.oauth.*Service.*(..))")
      advisor('pointcut-ref': "oAuthInterceptPoint", 'advice-ref': "transactionLoggerAdvice")
      pointcut(id: "gormOauthInterceptPoint", expression: "execution(* grails.plugin.springsecurity.oauthprovider.GormTokenStoreService.*(..))")
      advisor('pointcut-ref': "gormOauthInterceptPoint", 'advice-ref': "transactionLoggerAdvice")
    }
  }

  transactionLoggerAdvice(TransactionLogger) {
    sessionFactory = ref("sessionFactory")
    flushOnMethods = [
      "GormTokenStoreService.removeAccessToken",
      "GormTokenStoreService.removeRefreshToken"
    ]
  }


  gormTokenStoreService(OAuthTokenStoreService) { bean ->
    bean.autowire = "byName"
  }

  // api entry points
  // added this entry point to the original entrypoint 'authenticationEntryPoint'
  // checkout CustomBeanFactoryPostProcessor
  apiRequestMatcher(AntPathRequestMatcher, "/api/**")
  apiEntryPoint(ApiEntryPoint)

  appBeanFactoryPostProcessor(CustomBeanFactoryPostProcessor) {}

  //Not required, as we are storing it in another column in OAuthTokenStoreService
  //oauth2AuthenticationSerializer(WLOAuth2AuthenticationSerializer)

  println ".... DONE Configuring Starter App"
}
