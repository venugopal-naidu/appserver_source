class UrlMappings {

  static mappings = {

    // About API
    "/api/about"(controller: 'about', action: 'index') {}

    // Sample API
    "/api/$namespace/sample/about"(controller: 'sampleApi', action: 'about') {}
    "/api/$namespace/sample/securedWithOAuth"(controller: 'sampleApi', action: 'securedWithOAuth') {}
    "/api/$namespace/sample/securedWithUserOAuth"(controller: 'sampleApi', action: 'securedWithUserOAuth') {}
    "/api/$namespace/sample/securedWithClientOAuth"(controller: 'sampleApi', action: 'securedWithClientOAuth') {}

    // member API
    "/api/$namespace/users/signup"(controller: 'user') {
      action = [POST: "create"]
    }

    "/api/$namespace/users/signupCSR"(controller: 'user') {
      action = [POST: "createCSR"]
    }


    "/api/$namespace/users/$id"(controller: 'user') {
      constraints {
        id(matches: /[0-9]*/)
      }
      action = [GET: "profile", PUT: "update"]
    }
    "/api/$namespace/users/profile"(controller: 'user'){
      action = [PUT:'update', GET: 'profile']
    }

    // auth API
    "/api/$namespace/auth/login"(controller: 'auth') {
      action = [POST: "login"]
    }
    "/api/$namespace/auth/logout"(controller: 'auth') {
      action = [GET: "logout"]
    }
    "/api/$namespace/auth/forgotPassword"(controller: 'auth') {
      action = [POST: "resendPassword"]
    }
    "/api/$namespace/auth/resetPassword"(controller: 'auth') {
      action = [POST: "resetPassword"]
    }
    "/api/$namespace/auth/changePassword"(controller: 'auth') {
      action = [POST: "changePassword"]
    }

    // social API
    "/api/$namespace/auth/social/$socialId/login"(controller: 'social') {
      action = [GET: "login"]
    }
    "/api/$namespace/auth/social/$socialId/authorize"(controller: 'social') {
      action = [GET: "authorize"]
    }
    "/api/$namespace/auth/social/$socialId/connect"(controller: 'social') {
      action = [POST: "socialConnect"]
    }
    "/api/$namespace/auth/social/digits/connect"(controller: 'digits') {
      action = [POST: "connect"]
    }

    "/api/$namespace/media"(controller: 'media') {
      action = [GET: "getMedia", POST: "saveMedia"]
    }

    "/api/$namespace/registration/create"(controller: 'registration', action: 'register')
    "/api/$namespace/registration/verifyUid"(controller: 'registration', action: 'verifyUid')
    "/api/$namespace/registration/confirm"(controller: 'registration', action: 'confirmRegistration')

    "/api/$namespace/search/doctor"(controller: 'search', action: 'searchDoctor') {}
    "/api/$namespace/search/specialtiesAndHospitals"(controller: 'search', action: 'listSpecialitiesAndHospitals') {}
    "/api/$namespace/search/hospitals"(controller: 'search', action: 'listHospitals') {}
    "/api/$namespace/search/hospitalNames"(controller: 'search', action: 'listHospitalsNames') {}
    "/api/$namespace/search/hospital/specialties"(controller: 'search', action: 'listHospitalSpecialities') {}
    "/api/$namespace/search/lab/testsAndLabs"(controller: 'search', action: 'listTestAndLabsNames') {}
    "/api/$namespace/search/lab/listLabNames"(controller: 'search', action: 'listLabsForTest') {}
    "/api/$namespace/search/lab/listTests"(controller: 'search', action: 'listTestInLab') {}
    "/api/$namespace/search/lab/listLabs"(controller: 'search', action: 'listLabs') {}

    "/api/$namespace/appointment/create"(controller: 'appointment', action: 'makeAnAppointment') {}
    "/api/$namespace/appointment/cancel/$appointmentId"(controller: 'appointment', action: 'cancelAppointment') {}
    "/api/$namespace/appointment/confirm/$appointmentId"(controller: 'appointment', action: 'confirmAppointment') {}
    "/api/$namespace/appointment/list"(controller: 'appointment', action: 'listAppointments') {}


    "/api/$namespace/doctor/$action"(controller: 'doctor') {}



    //  add new mappings above for better managing

    "/api/info"(controller: 'apiDoc', action: 'index') {}
    "/api/swagger-json"(controller: 'apiDoc', action: 'swaggerJson') {}

    "/api/swagger"(controller: 'apiDoc', action: 'swagger') {}
    "/api/swagger-api"(controller: 'apiDoc', action: 'swaggerApi') {}

    "/"(resource: "/index.html")

    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }

    // TODO: on "/api/*" requests the 400/500 responses should be JSON
    "403"(controller: 'error', action: 'handleForbidden')      // Forbidden, no access
    "404"(controller: 'error', action: 'handleNotFound')       // Not found
    "405"(controller: 'error', action: 'handleInvalidMethod')  // Not allowed GET vs POST etc.
    "500"(controller: 'error', action: 'handleError')          // Internal Error
    //"400"(controller:'error', action:'handleBadData')      // Validation Errors

  }
}
