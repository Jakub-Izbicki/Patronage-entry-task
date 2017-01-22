package com.izbicki.jakub.Error;


import org.springframework.http.HttpStatus;

public class ErrorPayload {

    private HttpStatus httpStatus;
    private String internalMessage;
    private String userMessage;

    public ErrorPayload(HttpStatus httpStatus, String internalMessage, String userMessage) {
        this.httpStatus = httpStatus;
        this.internalMessage = internalMessage;
        this.userMessage = userMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getInternalMessage() {
        return internalMessage;
    }

    public void setInternalMessage(String internalMessage) {
        this.internalMessage = internalMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
