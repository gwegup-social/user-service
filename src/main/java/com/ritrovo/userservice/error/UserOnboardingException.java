package com.ritrovo.userservice.error;

import org.springframework.http.HttpStatus;

public class UserOnboardingException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public UserOnboardingException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
