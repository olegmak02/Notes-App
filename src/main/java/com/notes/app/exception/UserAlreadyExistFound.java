package com.notes.app.exception;

public class UserAlreadyExistFound extends RuntimeException {
    public UserAlreadyExistFound(String message) {
        super(message);
    }
}
