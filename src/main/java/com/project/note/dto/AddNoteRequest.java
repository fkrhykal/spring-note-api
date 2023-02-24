package com.project.note.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record AddNoteRequest(
        @NotBlank
        @Length(max = 225)
        String title,
        @NotBlank
        @Length(max = 10_000)
        String body
) {
}
