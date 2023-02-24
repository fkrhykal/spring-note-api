package com.project.note.validation;

import com.project.note.exception.ValidationException;

public interface ValidationUtil {
    <T> void validate(T value) throws ValidationException;
}
