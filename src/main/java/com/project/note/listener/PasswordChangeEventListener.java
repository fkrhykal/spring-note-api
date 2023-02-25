package com.project.note.listener;

import com.project.note.event.PasswordChangedEvent;
import com.project.note.service.EmailNotificationService;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordChangeEventListener {

    private final EmailNotificationService emailNotificationService;

    private final Retry sendPasswordChangedNotificationRetry = Retry.of("sendPasswordChangedNotification", RetryConfig.custom().build());
    

    @EventListener
    public void sendPasswordChangedNotification(PasswordChangedEvent event) throws MessagingException {
        sendPasswordChangedNotificationRetry.executeRunnable(() -> {
            emailNotificationService.sendPasswordChangedNotification(event.firstname(), event.email());
        });
    }
}
