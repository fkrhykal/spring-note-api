package com.project.note.dto;

public record ResetPasswordRequest(
        String id,
        String resetPasswordCode,
        String newPassword
) {
}
