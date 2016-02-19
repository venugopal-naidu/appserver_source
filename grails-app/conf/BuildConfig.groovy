grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
  // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
  //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

  // configure settings for the test-app JVM, uses the daemon by default
  test   : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon: true],
  // configure settings for the run-app JVM
  run    : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve: false],
  // configure settings for the run-war JVM
  war    : [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve: false],
  // configure settings for the Console UI JVM
  console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
  // inherit Grails' default dependencies
  inherits("global") {
    // specify dependency exclusions here; for example, uncomment this to disable ehcache:
    // excludes 'ehcache'
  }
  log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  checksums true // Whether to verify checksums on resolve
  legacyResolve false
  // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

  repositories {
    inherits true // Whether to inherit repository definitions from plugins

    grailsPlugins()
    grailsHome()
    mavenLocal()
    grailsCentral()
    mavenCentral()
    // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
    //mavenRepo "http://repository.codehaus.org"
    //mavenRepo "http://download.java.net/maven/2/"
    //mavenRepo "http://repository.jboss.com/maven2/"
    mavenRepo 'http://maven.restlet.org'
  }

  dependencies {
    // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
    // runtime 'org.postgresql:postgresql:9.3-1101-jdbc41'
    runtime 'mysql:mysql-connector-java:5.1.35'
    test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"
    compile "org.springframework.security:spring-security-core:3.2.3.RELEASE"
    compile "org.springframework.security:spring-security-web:3.2.3.RELEASE"
    //compile "com.wordnik:swagger-core_2.10:1.3.2"
    compile "io.swagger:swagger-core:1.5.3"
    compile "io.swagger:swagger-jaxrs:1.5.3"
    compile "javax.ws.rs:jsr311-api:1.1.1"
    //compile "org.reflections:reflections:0.9.9-RC1"
    compile "org.apache.httpcomponents:httpcore:4.4.1"

    // GEB
    test "org.gebish:geb-junit4:0.12.2"
    test "org.gebish:geb-spock:0.12.2"
    test "org.seleniumhq.selenium:selenium-support:2.45.0"
    test "org.seleniumhq.selenium:selenium-firefox-driver:2.45.0"

    // REST functional tests
    test 'org.apache.httpcomponents:fluent-hc:4.2.5'
    runtime 'org.codehaus.groovy.modules.http-builder:http-builder:0.7'

  }

  plugins {
    // plugins for the build system only
    build ":tomcat:7.0.55"

    // plugins for the compile step
    compile ":scaffolding:2.1.2"
    compile ':cache:1.1.8'
    compile ":asset-pipeline:1.9.9"
    //compile ":swagger4jaxrs:0.2"
    //compile ":jaxrs:0.11"
    compile ":restapidoc:0.1.4"

    // plugins needed at runtime but not for compilation
    // or ":hibernate:3.6.10.18"
    runtime ":hibernate4:4.3.6.1"
    //{
    //exclude module: 'xml-apis'
    //}

    runtime ":database-migration:1.4.0"
    runtime ":jquery:1.11.1"
    runtime "org.grails.plugins:jquery:1.11.1"
    runtime "org.grails.plugins:cors:1.1.8"

    compile "org.grails.plugins:jquery-ui:1.10.4"
    compile "org.grails.plugins:twitter-bootstrap:3.2.0.2"

    //compile "org.grails.plugins:email-confirmation:2.0.8"
    compile "org.grails.plugins:platform-core:1.0.0"
    compile "org.grails.plugins:quartz:1.0.2"
    compile "org.grails.plugins:mail:1.0.7"
    compile "org.grails.plugins:spring-security-core:2.0-RC4"
    compile "org.grails.plugins:spring-security-oauth2-provider:2.0-RC1"
    compile "org.grails.plugins:console:1.5.3"

    // REST functional tests
    test ":rest-client-builder:2.0.1"
    test ":remote-control:2.0"

    // FOR GEB
    compile ":geb:0.12.2"

    //rest-api-doc is also interesting http://loic911.github.io/restapidoc/index.html
    //compile "org.grails.plugins:rest-api-doc:0.6.1"
    //compile 'org.grails.plugins:plugin-config:0.2.0'

    // Uncomment these to enable additional asset-pipeline capabilities
    //compile ":sass-asset-pipeline:1.9.0"
    //compile ":less-asset-pipeline:1.10.0"
    //compile ":coffee-asset-pipeline:1.8.0"
    //compile ":handlebars-asset-pipeline:1.3.0.3"
  }
}
