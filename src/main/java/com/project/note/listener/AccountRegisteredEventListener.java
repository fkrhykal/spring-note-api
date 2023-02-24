package com.project.note.listener;


import com.project.note.event.AccountRegisteredEvent;
import com.project.note.service.EmailNotificationService;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AccountRegisteredEventListener {

    private final EmailNotificationService emailNotificationService;
    private final Retry sendEmailVerificationCodeRetry;

    public AccountRegisteredEventListener(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
        this.sendEmailVerificationCodeRetry = Retry.of("sendEmailVerificationCode", RetryConfig.custom().build());
    }

    @EventListener
    //TODO: added retry feature, maybe with resilience4j or manually
    public void sendEmailVerificationCode(AccountRegisteredEvent event) {
        sendEmailVerificationCodeRetry.executeRunnable(() -> {
            emailNotificationService.sendVerificationCode(event.firstname(), event.email(), event.verificationCode());
        });
    }
}