package com.izbicki.jakub.Error;


public class ApiNotFoundException extends RuntimeException {

    private String resourceName;

    public ApiNotFoundException(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
