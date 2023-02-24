package com.project.note.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record SuccessResponse<T>(int code, String status, T data) {

    public SuccessResponse(HttpStatus status, T data) {
        this(status.value(), status.getReasonPhrase(), data);
    }

    public static <T> SuccessResponse<T> ok(T data) {
        return new SuccessResponse<>(HttpStatus.OK, data);
    }

    public ResponseEntity<SuccessResponse<T>> toResponseEntity() {
        return ResponseEntity.status(this.code).body(this);
    }
}
