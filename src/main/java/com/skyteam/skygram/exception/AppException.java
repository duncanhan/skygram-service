package com.skyteam.skygram.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {

    private String message;

    public AppException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
