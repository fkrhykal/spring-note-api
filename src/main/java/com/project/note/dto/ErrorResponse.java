package com.project.note.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ErrorResponse<T>(int code, String status, T error) {

    public ErrorResponse(HttpStatus status, T error) {
        this(status.value(), status.getReasonPhrase(), error);
    }

    public static <T> ErrorResponse<T> badRequest(T error) {
        return new ErrorResponse<>(HttpStatus.BAD_REQUEST, error);
    }

    public static <T> ErrorResponse<T> unauthorized(T error) {
        return new ErrorResponse<>(HttpStatus.UNAUTHORIZED, error);
    }

    public static <T> ErrorResponse<T> notFound(T error) {
        return new ErrorResponse<>(HttpStatus.NOT_FOUND, error);
    }

    public ResponseEntity<ErrorResponse<T>> toEntity() {
        return ResponseEntity.status(this.code).body(this);
    }
}
