package com.project.note.validation;

import com.project.note.exception.ValidationException;

public interface Validation<T> {
    void validate(T value) throws ValidationException;
}
