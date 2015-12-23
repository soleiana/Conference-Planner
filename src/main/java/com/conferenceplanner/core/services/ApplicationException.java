package com.conferenceplanner.core.services;

public class ApplicationException extends RuntimeException {

    private ApplicationErrorCode errorCode;

    public ApplicationException(String errorMessage, ApplicationErrorCode errorCode) {
        super(errorMessage);
    }

    public ApplicationErrorCode getErrorCode() {
        return errorCode;
    }
}
