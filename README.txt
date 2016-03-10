Environment setup
====================

Download the following 
------------------------
	download grails-2.4.4 latest versions available from https://grails.org/download.html
	download gradle-2.1 latest versions from https://gradle.org/downloads/


upzip the zip files
-----------------------

	Windows
	---------
	> cd ~/applications
	> unzip ~/Downloads/grails-2.4.4.zip
	> unzip ~/Downloads/gradle-2.1-all.zip

	MAC
	---------
	> cd ~/applications
	> unzip ~/Downloads/grails-2.4.4.zip
	> unzip ~/Downloads/gradle-2.1-all.zip

and keep those folders in any location, then set these environment variables pointing to that location, like below

	set GRAILS_HOME=~/applications/grails-2.4.4
	set GRADLE_HOME=~/applications/gradle-2.1

and add these in your PATH, like

	set PATH=%PATH%;%GRAILS_HOME%/bin;%GRADLE_HOME%/bin;

DataBase Setup
==========================

	You have to have the mysql database installed in your machine.

	and then create a schema named starter_app_dev in mysql database

	\> mysql -u root -p
	\> {enter password}
	mysql> create database vellkare

API Server Configuration
==========================
set the grails.serverURL = "http://vellkare.com:8080/vellkare" for development environment to use social connections like facebook, linkedIn, GitHub and Instagram

and set the loopback ip address to starterapp.com in hosts file
eg:
127.0.0.1    vellkare.com
	
	
StarterAPI code setup
==========================

	clone the repository from and run the starter app

	\> git clone git@scm.vellkare.com:vellkare/vellkare.git
	\> cd vellkare
	\vellkare> git co module
	\vellkare> grails run-app

	To start server in debug mode
	\vellkare> grails --debug-fork run-app


	To test api
	\vellkare> grails -Dgeb.build.baseUrl=http://localhost::8080/vellkare test-app --functional SampleApiFunctionalSpec

	To see list of all API endpoints and their respective input and output parameters visit the page after server starts
	http://localhost:8080/vellkare/api/swagger

	
Features
==========================
1. Registration Module
	1.1 Signup
	1.2 Social Signup
		1.2.1 Facebook Signup
		1.2.2 GooglePlus Signup
	1.3 Login
	1.4 Social Login
		1.4.1 Facebook Login
		1.4.2 GooglePlus Login
	1.5 Logout
2 User Module
	2.1 Profile View
	2.2 Profile update
	2.3 Photo Upload
	2.4 Photo View
	2.5 Forgot Password
	2.6 Change Password
	
	
Full API Documentation 
===========================
http://wiki.vellkare.com/index.php/VellKareAPI

Relevant Documentation
=========================
Using Swagger Annotations
  https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X#api
  - @ApiModel are converting all the properties, including errors, allErrors that exist on a domain.

Grails Rest API Plugin
  http://loic911.github.io/restapidoc/index.html ( alternate to swagger )

http://grails.github.io/grails-doc/2.4.5/guide/webServices.html
http://www.slideshare.net/SpringCentral/so2gx-building-apiswithgrails
http://www.slideshare.net/SpringCentral/grails-testing
http://www.slideshare.net/gagana24/ast-transformation

Good Groovy documentation
  http://gr8labs.org/getting-groovy/


TODO:
=================
1. Unit / Integration tests
   - Test URL parameters data binding 
       - UrlMappingsSpec  (for some reason its not identifying @TestFor(UrlMappings), it works if i add a package to UrlMappings, tests work, but app wouldn't start
   - Test JSON data binding
       - UserControllerSpec."test invalid data"
   - Test JSON responses
       - UserControllerSpec."test invalid data"
       - UserControllerSpec."test member not found"
  - Test services injected in domain ( LoginSpec )
       - Login.memberService ?? 
  - Register Custom Marshallers in Controller Unit Tests UserControllerSpec
       - defineBeans{ }, setup() { bean.registerMarshallers() }
  - Test error codes in responses
       - UserControllerSpec."update(): test invalid data"
  - Test transaction rollback doesn't work with H2,MySQL,defaultAutoCommit=false
       - UserControllerIntegrationSpec."update(): an exception during update should return json.code 500"
  - Test locale resolver config


TODO:
  - blog about transaction rollback 
  - writing custom locale resolver
   

2. API Test automation using GEB

3. Controller cleanup
    - messages should be internalionalized, we should accept a language header "Accept-Language"
    - 400/500 messages should be JSON/XML format for all "/api/*" based requests, for others it could be HTML
    - How can we support both JSON/XML responses
    - RestResponse, RestError object can these be made better, with subclassed objects for appropriate responses ??
    - Secure the API's based on OAuth scopes
    - Loading metadata ( modules, roles, authorities )

4. Understand OAuth Spring security filters 
    - how is the principal getting loaded from the token ( we should not be storing the principal in session )
    - how to secure api's based on oauth scopes

5. Swagger UI
    - generate documentation using annotations "ApiDocController/ApiDocService.generateJSON()" currently the response is statically generated as JSON