package com.project.note.listener;

import com.project.note.event.ResetPasswordCodeCreatedEvent;
import com.project.note.service.EmailNotificationService;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResetPasswordCodeCreatedEventListener {

    private final EmailNotificationService emailNotificationService;
    private final Retry sendResetPasswordCodeNotificationRetry = Retry.of("sendResetPasswordCode", RetryConfig.custom().build());

    @EventListener
    public void sendResetPasswordCodeNotification(ResetPasswordCodeCreatedEvent event) {
        sendResetPasswordCodeNotificationRetry.executeRunnable(() -> {
            emailNotificationService.sendResetPasswordCode(event.firstname(), event.userEmail(), event.resetPasswordCode());
        });
    }
}
