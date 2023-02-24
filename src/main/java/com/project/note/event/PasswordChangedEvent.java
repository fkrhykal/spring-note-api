package com.project.note.event;

public record PasswordChangedEvent(
        String firstname,
        String email
) {
}
