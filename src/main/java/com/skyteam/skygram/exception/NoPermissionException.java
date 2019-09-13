package com.skyteam.skygram.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoPermissionException extends RuntimeException {

    private String message;

    public NoPermissionException() {
        super("No permission");
    }

    public NoPermissionException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
