package com.project.note.dto;

public record NoteDto(
        String id,
        String title,
        String body,
        Long createdAt,
        Long updatedAt
) {
}
