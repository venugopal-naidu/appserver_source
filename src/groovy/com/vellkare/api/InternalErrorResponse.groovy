package com.vellkare.api
/**
 */
class InternalErrorResponse extends RestMessage {

    public InternalErrorResponse(String code, def args) {
        super(code, args)
    }
}
