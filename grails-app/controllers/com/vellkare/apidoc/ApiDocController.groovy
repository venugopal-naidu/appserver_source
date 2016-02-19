package com.vellkare.apidoc

import org.codehaus.groovy.grails.web.mapping.LinkGenerator

class ApiDocController {

  LinkGenerator grailsLinkGenerator
  def apiDocService


  def swagger = {
    String basePath = grailsLinkGenerator.serverBaseURL
    render(view: 'index', model: [apiDocsPath: "${basePath}/api/swagger-api"])
  }
  def swaggerApi = {
    render apiDocService.generateJSON()
  }

  def index = {
    String basePath = grailsLinkGenerator.serverBaseURL
    render(view: 'index', model: [apiDocsPath: "${basePath}/api/swagger-json"])
  }

  def swaggerJson = {
    apiDocService.generateJSON()
    render """
{
  ${Swagger.ABOUT},
  "host": "localhost:8080/vellkare",
  "basePath": "/api/v0",
  "tags": [
    ${Auth.AUTH_TAG},
    ${User.USER_TAG},
    ${Media.MEDIA_TAG}
  ],
  "schemes": ["http"],
  "paths": {
    "/users/signup":{
      "post":${Auth.SIGNUP_API}
    },
    "/auth/login": {
      "post": ${Auth.LOGIN_API}
    },
    "/auth/logout": {
      "get": ${Auth.LOGOUT_API}
    },
    "/users/{userId}":{
      "get":${User.PROFILE_GET_API},
      "put":${User.PROFILE_UPDATE_API}
    },
    "/users/create":{
      "get":${User.PROFILE_GET_API},
      "put":${User.PROFILE_UPDATE_API}
    },
    "/media":{
      "get":${Media.MEDIA_GET_API},
      "post":${Media.MEDIA_UPLOAD_API}
    },
    "/auth/social/{socialId}/login":{
      "get":${Social.SOCIAL_GET_API}
    },
    "/auth/social/{socialId}/connect":{
      "post":${Social.SOCIAL_CONNECT_API}
    },
    "/auth/changePassword":{
      "post":${Auth.CHANGE_PASSWORD_API}
    },
    "/auth/forgotPassword":{
      "post":${Auth.FORGOT_PASSWORD_API}
    },
    "/auth/resetPassword":{
      "post":${Auth.RESET_PASSWORD_API}
    }
  },
  "definitions": {
    "User": ${User.USER_DEF},
    "Login": ${Auth.LOGIN_DEF},
    "UserUpdate":${User.PROFILE_UPDATE_DEF},
    "SocialLogin":${Social.SOCIAL_DEF},
    "ChangePassword":${Auth.CHANGE_PASSWORD_DEF},
    "ForgotPassword":${Auth.FORGOT_PASSWORD_DEF},
    "ResetPassword":${Auth.RESET_PASSWORD_DEF},
    "ApiResponse": ${APIResponse.RESPONSE}
  }
}
"""
  }
  class APIResponse {
    static String RESPONSE = """
{
      "type": "object",
      "properties": {
        "code": {
          "type": "integer",
          "format": "int32"
        },
        "type": {
          "type": "string"
        },
        "message": {
          "type": "string"
        }
      }
    }
"""
    static String SIGNUP_RESPONSE = """{
            "description": "Successful operation",
            "schema": {
                "type": "object",
                "required": [ "status", "type", "message", "results", "count" ],
                "properties": {
                    "status": {
                        "type": "string",
                        "default":"SUCCESS"
                    },
                    "type": {
                        "type": "string",
                        "default":"SignupResponse"
                    },
                    "message": {
                        "type": "string",
                        "default":"User Successfully Registerd."
                    },
                    "results": {
                        "type": "array",
                        "items": {
                            "type": "object",
                            "required": [ "user_id" ],
                            "properties": {
                                "user_id": {
                                    "type": "integer",
                                    "format": "int64"
                                }
                            }
                        }
                    },
                    "count": {
                        "type": "integer",
                        "format": "int64",
                        "default":1
                    }
                }
            }
          }
"""
    static String LOGIN_RESPONSE = """{
            "description": "Successful operation",
            "schema": {
                "type": "object",
                "required": [ "status", "type", "message", "results", "count" ],
                "properties": {
                    "status": {
                        "type": "string",
                        "default":"SUCCESS"
                    },
                    "type": {
                        "type": "string",
                        "default":"TOKEN"
                    },
                    "message": {
                        "type": "string",
                        "default":"Login Success."
                    },
                    "results": {
                        "type": "array",
                        "items": {
                            "type": "object",
                            "required": [ "scope", "user_id", "expires_in", "token_type", "refresh_token", "access_token" ],
                            "properties": {
                                "scope": {
                                    "type": "string"
                                },
                                "user_id": {
                                    "type": "integer",
                                    "format": "int64"
                                },
                                "expires_in": {
                                    "type": "integer",
                                    "format": "int64"
                                },
                                "token_type": {
                                    "type": "string"
                                },
                                "refresh_token": {
                                    "type": "string"
                                },
                                "access_token": {
                                    "type": "string"
                                }
                            }
                        }
                    },
                    "count": {
                        "type": "integer",
                        "format": "int64",
                        "default":1
                    }
                }
            }
          }
"""
    static String CHANGE_PASSWORD_RESPONSE = """{
            "description": "Successful operation",
            "schema": {
                "type": "object",
                "required": [ "status", "type", "message" ],
                "properties": {
                    "status": {
                        "type": "string",
                        "default":"SUCCESS"
                    },
                    "type": {
                        "type": "string",
                        "default":"SUCCESS"
                    },
                    "message": {
                        "type": "string",
                        "default":"Your password has been successfully changed."
                    }
                }
            }
          }"""
    static String FORGOT_PASSWORD_RESPONSE = """{
            "description": "Successful operation",
            "schema": {
                "type": "object",
                "required": [ "status", "type", "message" ],
                "properties": {
                    "status": {
                        "type": "string",
                        "default":"SUCCESS"
                    },
                    "type": {
                        "type": "string",
                        "default":"SUCCESS"
                    },
                    "message": {
                        "type": "string",
                        "default":"We've sent password reset instructions to your email address."
                    }
                }
            }
          }"""
    static String RESET_PASSWORD_RESPONSE = """{
            "description": "Successful operation",
            "schema": {
                "type": "object",
                "required": [ "status", "type", "message" ],
                "properties": {
                    "status": {
                        "type": "string",
                        "default":"SUCCESS"
                    },
                    "type": {
                        "type": "string",
                        "default":"SUCCESS"
                    },
                    "message": {
                        "type": "string",
                        "default":"Your password changed successfully."
                    }
                }
            }
          }"""
  }

  class Swagger {
    static String ABOUT = """
  "swagger": "2.0",
  "info": {
    "description": "This is a grails starter api server.  You can find out more about Starter API Server at [http://wiki.vellkare.com]. For this sample, you can use the api key `special-key` to test the authorization filters.",
    "version": "1.0.0",
    "title": "Starter API Server",
    "termsOfService": "http://swagger.io/terms/",
    "contact": {
      "email": "apiteam@swagger.io"
    },
    "license": {
      "name": "Apache 2.0",
      "url": "http://www.apache.org/licenses/LICENSE-2.0.html"
    }
  }
"""
  }

  class Auth {
    static String AUTH_TAG = """
    {
      "name": "Auth",
      "description": "Everything about your Authorization",
      "externalDocs": {
        "description": "Find out more",
        "url": "http://swagger.io"
      }
    }
"""
    static String LOGIN_API = """
{
        "tags": [
          "Auth"
        ],
        "summary": "User login",
        "description": "",
        "operationId": "create",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "body",
            "name": "body",
            "description": "Login object",
            "required": true,
            "schema": {
              "\$ref": "#/definitions/Login"
            }
          }
        ],
        "responses": {
          "405": {
            "description": "Invalid input"
          },
          "200": ${APIResponse.LOGIN_RESPONSE}
        },
        "security": []
      }
"""
    static String SIGNUP_API = """
 {
        "tags": [
          "User"
        ],
        "summary": "Add a new user",
        "description": "",
        "operationId": "create",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "body",
            "name": "body",
            "description": "User object",
            "required": true,
            "schema": {
              "\$ref": "#/definitions/User"
            }
          }
        ],
        "responses": {
          "405": {
            "description": "Invalid input"
          },
          "200": ${APIResponse.SIGNUP_RESPONSE}
        },
        "security": []
      }
"""
    static String LOGIN_DEF = """{
      "type": "object",
      "required": [ "clientId", "username", "password" ],
      "properties": {
        "clientId": { "type": "string", "default":"my-client" },
        "username": { "type": "string" },
        "password": { "type": "string" }
      },
      "xml": {
        "name": "Auth"
      }
    }
"""
    static String LOGOUT_API = """
{
        "tags": [
          "Auth"
        ],
        "summary": "Logout user",
        "description": "",
        "operationId": "logout",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "responses": {
          "403": {
            "description": "UnAuthorized"
          }
        },
        "security": [
          {
            "key": []
          }
        ]
      }
"""
    static String FORGOT_PASSWORD_DEF = """{
      "type": "object",
      "required": [ "email" ],
      "properties": {
        "email": { "type": "string" }
      },
      "xml": {
        "name": "Auth"
      }
    }"""
    static String FORGOT_PASSWORD_API = """{
        "tags": [
          "Auth"
        ],
        "summary": "used to reset password",
        "description": "",
        "operationId": "create",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "body",
            "name": "body",
            "description": "ForgotPassword object",
            "required": true,
            "schema": {
              "\$ref": "#/definitions/ForgotPassword"
            }
          }
        ],
        "responses": {
          "200": ${APIResponse.FORGOT_PASSWORD_RESPONSE}
        },
        "security": [
          {
            "key": []
          }
        ]
      }"""
    static String RESET_PASSWORD_DEF = """{
      "type": "object",
      "required": [ "newPassword", "token" ],
      "properties": {
        "newPassword": { "type": "string" },
        "token": { "type": "string" }
      },
      "xml": {
        "name": "Auth"
      }
    }"""
    static String RESET_PASSWORD_API = """{
        "tags": [
          "Auth"
        ],
        "summary": "Change Password of a User",
        "description": "",
        "operationId": "create",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "body",
            "name": "body",
            "description": "ResetPassword object",
            "required": true,
            "schema": {
              "\$ref": "#/definitions/ResetPassword"
            }
          }
        ],
        "responses": {
          "200": ${APIResponse.RESET_PASSWORD_RESPONSE}
        },
        "security": [
          {
            "key": []
          }
        ]
      }"""
    static String CHANGE_PASSWORD_DEF = """{
      "type": "object",
      "required": [ "password", "newPassword" ],
      "properties": {
        "password": { "type": "string" },
        "newPassword": { "type": "string" }
      },
      "xml": {
        "name": "Auth"
      }
    }"""
    static String CHANGE_PASSWORD_API = """{
        "tags": [
          "Auth"
        ],
        "summary": "Change Password of a User",
        "description": "",
        "operationId": "create",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "body",
            "name": "body",
            "description": "ChangePassword object",
            "required": true,
            "schema": {
              "\$ref": "#/definitions/ChangePassword"
            }
          }
        ],
        "responses": {
          "403": {
            "description": "UnAuthorized"
          },
          "200": ${APIResponse.CHANGE_PASSWORD_RESPONSE}
        },
        "security": [
          {
            "key": []
          }
        ]
      }"""
  }

  class User {
    static String USER_TAG = """
    {
      "name": "User",
      "description": "Operations about User",
      "externalDocs": {
        "description": "Find out more about our store",
        "url": "http://swagger.io"
      }
    }
"""
    static String USER_DEF = """{
      "type": "object",
      "required": [ "clientId", "username", "password", "firstName" ],
      "properties": {
        "clientId": { "type": "string", "default": "my-client" },
        "username": { "type": "string" },
        "firstName": { "type": "string" },
        "lastName": { "type": "string" },
        "email": { "type": "string" },
        "password": { "type": "string" }
      },
      "xml": {
        "name": "User"
      }
    }
"""
    static String PROFILE_GET_API = """
{
        "tags": [
          "User"
        ],
        "summary": "Add a new user",
        "description": "",
        "operationId": "create",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "path",
            "name": "userId",
            "description": "Id of the User.",
            "required": true,
            "type":"integer",
            "format":"int64"
          }
        ],
        "responses": {
          "403": {
            "description": "UnAuthorized"
          }
        },
        "security": [
          {
            "key": []
          }
        ]
      }
"""
    static String PROFILE_UPDATE_DEF = """{
      "type": "object",
      "required": [ "firstName" ],
      "properties": {
        "firstName": { "type": "string" },
        "lastName": { "type": "string" },
        "primaryPhone": { "type": "string" },
        "description": { "type": "string" }
      },
      "xml": {
        "name": "User"
      }
    }
"""
    static String PROFILE_UPDATE_API = """
{
        "tags": [
          "User"
        ],
        "summary": "Update a user",
        "description": "",
        "operationId": "create",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "path",
            "name": "userId",
            "description": "Id of the User.",
            "required": true,
            "type":"integer",
            "format":"int64"
          },
          {
            "in": "body",
            "name": "body",
            "description": "User object",
            "required": true,
            "schema": {
              "\$ref": "#/definitions/UserUpdate"
            }
          }
        ],
        "responses": {
          "403": {
            "description": "UnAuthorized"
          }
        },
        "security": [
          {
            "key": []
          }
        ]
      }
"""
  }

  class Media {
    static String MEDIA_TAG = """
    {
      "name": "Media",
      "description": "Operations for a media",
      "externalDocs": {
        "description": "Find out more about our store",
        "url": "http://swagger.io"
      }
    }
"""

    static String MEDIA_GET_API = """
    {
        "tags": [
          "Media"
        ],
        "summary": "Get Media.",
        "description": "",
        "operationId": "create",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "query",
            "name": "id",
            "description": "id of the user or workspace ( depends on mediafor )",
            "required": true,
            "type":"integer",
            "format":"int64"
          },
          {
            "in": "query",
            "name": "mediafor",
            "description": "Media for the profile or workspace.",
            "required": true,
            "type":"string"
          }
        ],
        "responses": {
          "403": {
            "description": "UnAuthorized"
          }
        },
        "security": [
          {
            "key": []
          }
        ]
      }
"""

    static String MEDIA_UPLOAD_API = """
{
        "tags": [
          "Media"
        ],
        "summary": "Upload a media",
        "description": "",
        "operationId": "create",
        "consumes": [
          "multipart/form-data"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "query",
            "name": "id",
            "description": "Id of the User.",
            "required": true,
            "type":"integer",
            "format":"int64"
          },
          {
            "in": "query",
            "name": "mediafor",
            "description": "media for user or workspace.",
            "required": true,
            "type":"string"
          },
          {
            "name": "file",
            "in": "formData",
            "description": "file to upload",
            "required": true,
            "type": "file"
          }
        ],
        "responses": {
          "403": {
            "description": "UnAuthorized"
          }
        },
        "security": [
          {
            "key": []
          }
        ]
      }
"""

  }

  class Social {
    static String SOCIAL_DEF = """
{
      "type": "object",
      "required": [ "clientId","accessToken" ],
      "properties": {
        "clientId": { "type": "string", "default": "my-client" },
        "accessToken": { "type": "string" },
        "expiresIn": { "type": "integer" }
      },
      "xml": {
        "name": "Auth"
      }
    }
"""
    static String SOCIAL_GET_API = """
{
        "tags": [
          "Auth"
        ],
        "summary": "Login with social account",
        "description": "",
        "operationId": "create",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "path",
            "name": "socialId",
            "description": "social id like facebook, googlePlus, etc.",
            "required": true,
            "type":"string"
          }
        ],
        "responses": {
          "405": {
            "description": "Invalid input"
          }
        },
        "security": [
          {
            "key": []
          }
        ]
      }
"""
    static String SOCIAL_CONNECT_API = """
{
        "tags": [
          "Auth"
        ],
        "summary": "connect with social account",
        "description": "",
        "operationId": "create",
        "consumes": [
          "application/json"
        ],
        "produces": [
          "application/json"
        ],
        "parameters": [
          {
            "in": "path",
            "name": "socialId",
            "description": "social id like facebook, googlePlus, etc.",
            "required": true,
            "type":"string"
          },
          {
            "in": "body",
            "name": "body",
            "description": "Social object",
            "required": true,
            "schema": {
              "\$ref": "#/definitions/SocialLogin"
            }
          }
        ],
        "responses": {
          "405": {
            "description": "Invalid input"
          }
        },
        "security": [
          {
            "key": []
          }
        ]
      }
"""
  }
}
