package com.project.note.dto;

public record AccountDto(
        String id,
        String firstname,
        String lastname,
        String email,
        String status
) {
}
