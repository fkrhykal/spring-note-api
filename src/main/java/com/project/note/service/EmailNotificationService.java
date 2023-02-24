package com.project.note.service;

public interface EmailNotificationService {
    void sendVerificationCode(String firstname, String email, String verificationCode);

    void sendResetPasswordCode(String firstname, String email, String resetPasswordCode);

    void sendPasswordChangedNotification(String firstname, String email);
}

