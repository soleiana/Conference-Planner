package com.conferenceplanner.rest.validators;

public class ValidationException extends RuntimeException {

    public ValidationException(String validationMessage) {
        super(validationMessage);
    }
}
