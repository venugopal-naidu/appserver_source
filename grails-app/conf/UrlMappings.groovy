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


    "/api/$namespace/search/doctor"(controller: 'search', action: 'searchDoctor') {}

    //  add new mappings above for better managing

    "/api/info"(controller: 'apiDoc', action: 'index') {}
    "/api/swagger-json"(controller: 'apiDoc', action: 'swaggerJson') {}

    "/api/swagger"(controller: 'apiDoc', action: 'swagger') {}
    "/api/swagger-api"(controller: 'apiDoc', action: 'swaggerApi') {}

    "/"(view: "/index")

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
