package com.conferenceplanner.core.services;

public class AccessException extends RuntimeException {

    private AccessErrorCode errorCode;

    public AccessException(String errorMessage, AccessErrorCode errorCode) {
        super(errorMessage);
    }
}
