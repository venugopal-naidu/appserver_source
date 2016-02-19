package com.vellkare.api

import org.springframework.validation.Errors

/**
 */
class ValidationErrorResponse {

    List<FieldErrorApiModel> errors

    public ValidationErrorResponse() {
    }

    public ValidationErrorResponse( Errors errors ) {
        this()
        this.errors = errors.allErrors.collect {
            new FieldErrorApiModel(it.field, it.code, it.arguments)
        }
    }

    public ValidationErrorResponse( List<FieldErrorApiModel> errors ){
        this.errors = errors
    }
}
