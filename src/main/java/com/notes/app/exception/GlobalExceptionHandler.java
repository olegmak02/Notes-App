package com.notes.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NoteNotFound.class)
    public ResponseEntity handleResourceNotFoundException()  {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler({IllegalArgumentException.class, UserAlreadyExistFound.class})
    public ResponseEntity handleIllegalArgumentException()  {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
