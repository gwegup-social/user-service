package com.ritrovo.userservice.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


public class ErrorMap {

    private HttpStatus status;
    private String message;

    private String details;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;


    public ErrorMap(HttpStatus status, String message, String details) {
        super();
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.details = details;
    }

    public ErrorMap(String message, String details) {
        super();
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.details = details;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}

