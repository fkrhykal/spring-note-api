package com.project.note.dto;

public record UserCredentialDto(
        String id,
        String firstname,
        String lastname,
        String email
) {
}
