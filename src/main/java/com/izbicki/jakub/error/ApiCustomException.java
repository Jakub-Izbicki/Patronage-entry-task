package com.izbicki.jakub.error;


import org.springframework.http.HttpStatus;

public class ApiCustomException extends RuntimeException {

    private HttpStatus httpStatus;
    private String userMessage;

    public ApiCustomException(HttpStatus httpStatus, String internalMessage, String userMessage) {

        super(internalMessage);
        this.httpStatus = httpStatus;
        this.userMessage = userMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
