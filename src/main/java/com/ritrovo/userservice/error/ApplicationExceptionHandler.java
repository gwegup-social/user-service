package com.ritrovo.userservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(RitrovoException.class)
    public ResponseEntity<?> RitrovoException(RitrovoException e, WebRequest webRequest) {
        ErrorMap errorMap = new ErrorMap(e.getStatus(), e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorMap, e.getStatus());
    }

    @ExceptionHandler(UserOnboardingException.class)
    public ResponseEntity<?> userOnboardingException(UserOnboardingException e, WebRequest webRequest) {
        ErrorMap errorMap = new ErrorMap(e.getStatus(), e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorMap, e.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeException(RuntimeException e, WebRequest webRequest) {
        ErrorMap errorMap = new ErrorMap( e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorMap,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> GlobalException(Exception e, WebRequest webRequest) {
        ErrorMap errorMap = new ErrorMap(e.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
