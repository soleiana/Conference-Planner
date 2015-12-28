package com.conferenceplanner.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class AppRestExceptionHandler extends ResponseEntityExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(AppRestExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
        log.error("Exception in rest service invocation!", e);

        String bodyOfResponse = "Internal server error";
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
