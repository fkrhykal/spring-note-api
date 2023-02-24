package com.project.note.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;


public record RegisterAccountRequest(
        @NotBlank
        @Length(max = 25)
        String firstname,
        @NotBlank
        @Length(max = 25)
        String lastname,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password
) {
}
