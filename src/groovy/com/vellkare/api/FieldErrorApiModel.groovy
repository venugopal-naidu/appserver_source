package com.vellkare.api


public class FieldErrorApiModel extends RestMessage {
    String objectName
	String propertyName

    public FieldErrorApiModel(String propertyName, String messageCode, def args) {
        super(messageCode,args)
		this.propertyName = propertyName
	}

}
