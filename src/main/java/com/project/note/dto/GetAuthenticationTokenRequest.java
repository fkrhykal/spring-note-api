package com.project.note.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GetAuthenticationTokenRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {
}
