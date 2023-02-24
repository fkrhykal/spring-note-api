package com.project.note.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends Exception {

    public ValidationException() {
    }

    public ValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

    private Map<String, String> errors = new HashMap<>();

    public ValidationException addError(String field, String message) {
        errors.put(field, message);
        return this;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
