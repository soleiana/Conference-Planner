package com.conferenceplanner.rest.domain;

import com.conferenceplanner.core.services.AccessErrorCode;
import org.springframework.http.HttpStatus;

public enum ResourceAccessErrorCode {

    RESOURCE_NOT_FOUND(AccessErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND),
    RESOURCE_CONFLICT(AccessErrorCode.CONFLICT, HttpStatus.CONFLICT)
    ;

    private AccessErrorCode errorCode;
    private HttpStatus httpStatus;

    private ResourceAccessErrorCode(AccessErrorCode errorCode, HttpStatus httpStatus){
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public AccessErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public static HttpStatus getHttpStatus(AccessErrorCode errorCode) {
        for (ResourceAccessErrorCode code: values()) {
            if (code.errorCode == errorCode)
                return code.getHttpStatus();
        }
        throw new IllegalArgumentException("Invalid accessErrorCode!");
    }
}
