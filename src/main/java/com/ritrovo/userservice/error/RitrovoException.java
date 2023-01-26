package com.ritrovo.userservice.error;

import org.springframework.http.HttpStatus;

public class RitrovoException extends Exception {

    private HttpStatus status;
    private String message;

    public RitrovoException(HttpStatus status, String message) {
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
