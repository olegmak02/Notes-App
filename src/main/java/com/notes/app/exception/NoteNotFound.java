package com.notes.app.exception;

public class NoteNotFound extends RuntimeException {
    public NoteNotFound(String message) {
        super(message);
    }
}
