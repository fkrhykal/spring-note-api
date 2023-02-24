package com.project.note.event;

public record ResetPasswordCodeCreatedEvent(String firstname, String userEmail, String resetPasswordCode) {
}
