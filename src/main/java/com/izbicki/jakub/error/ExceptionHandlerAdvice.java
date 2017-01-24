package com.izbicki.jakub.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ApiCustomException.class)
    public ResponseEntity handleException(ApiCustomException e){

        return ResponseEntity.status(e.getHttpStatus()).body(
                new ErrorPayload(e.getHttpStatus(), e.getMessage(), e.getUserMessage()));
    }

    @ExceptionHandler(ApiNotFoundException.class)
    public ResponseEntity handleException(ApiNotFoundException e){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorPayload(HttpStatus.NOT_FOUND,
                        "The requested resource does not exist: " + e.getResourceName(),
                        "Sorry, the requested page was not found."));
    }
}
