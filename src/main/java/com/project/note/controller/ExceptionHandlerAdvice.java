package com.project.note.controller;

import com.project.note.dto.ErrorResponse;
import com.project.note.exception.AuthenticationException;
import com.project.note.exception.ResourceNotFoundException;
import com.project.note.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse<String>> handle(ResourceNotFoundException exception) {
        return ErrorResponse.notFound(exception.getMessage()).toEntity();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse<String>> handle(AuthenticationException exception) {
        return ErrorResponse.unauthorized(exception.getMessage()).toEntity();
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse<Map<String, String>>> handle(ValidationException exception) {
        return ErrorResponse.badRequest(exception.getErrors()).toEntity();
    }
}
