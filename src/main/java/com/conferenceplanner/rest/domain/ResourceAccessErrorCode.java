package com.conferenceplanner.rest.domain;

import com.conferenceplanner.core.services.ApplicationErrorCode;
import org.springframework.http.HttpStatus;

public enum ResourceAccessErrorCode {

    RESOURCE_NOT_FOUND(ApplicationErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND),
    RESOURCE_CONFLICT(ApplicationErrorCode.CONFLICT, HttpStatus.CONFLICT)
    ;

    private ApplicationErrorCode errorCode;
    private HttpStatus httpStatus;

    private ResourceAccessErrorCode(ApplicationErrorCode errorCode, HttpStatus httpStatus){
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ApplicationErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public static HttpStatus getHttpStatus(ApplicationErrorCode errorCode) {
        for (ResourceAccessErrorCode code: values()) {
            if (code.errorCode == errorCode)
                return code.getHttpStatus();
        }
        throw new IllegalArgumentException("Invalid applicationErrorCode!");
    }
}
