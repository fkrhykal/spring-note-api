package com.project.note.listener;

import com.project.note.event.ResetPasswordCodeCreatedEvent;
import com.project.note.service.EmailNotificationService;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordCodeCreatedEventListener {

    private final EmailNotificationService emailNotificationService;
    private final Retry sendResetPasswordCodeNotificationRetry;

    public ResetPasswordCodeCreatedEventListener(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
        this.sendResetPasswordCodeNotificationRetry = Retry.of("sendResetPasswordCode", RetryConfig.custom().build());
    }

    @EventListener
    public void sendResetPasswordCodeNotification(ResetPasswordCodeCreatedEvent event) {
        sendResetPasswordCodeNotificationRetry.executeRunnable(() -> {
            emailNotificationService.sendResetPasswordCode(event.firstname(), event.userEmail(), event.resetPasswordCode());
        });
    }
}
