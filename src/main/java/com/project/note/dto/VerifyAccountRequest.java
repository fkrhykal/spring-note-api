package com.project.note.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyAccountRequest(
        @NotBlank
        String id,

        @NotBlank
        String verificationCode
) {
}
