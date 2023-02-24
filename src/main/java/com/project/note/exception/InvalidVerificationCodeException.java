package com.project.note.exception;

public class InvalidVerificationCodeException extends Exception {

    public InvalidVerificationCodeException() {
    }

    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}
