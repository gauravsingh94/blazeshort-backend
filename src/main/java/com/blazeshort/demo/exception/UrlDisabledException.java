package com.blazeshort.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UrlDisabledException extends RuntimeException {
    public UrlDisabledException(String message) {
        super(message);
    }
}