package com.project.note.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendVerificationCode(String firstname, String email, String verificationCode) {
        Context context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("verificationCode", verificationCode);

        String templateName = "verification-code-notification-message";
        String content = templateEngine.process(templateName, context);
        sendHTMLMessage("Note Application Verification Code", email, content);
    }

    @Override
    public void sendResetPasswordCode(String firstname, String email, String resetPasswordCode) {
        Context context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("resetPasswordCode", resetPasswordCode);

        String templateName = "reset-password-code-notification-message";
        String content = templateEngine.process(templateName, context);
        sendHTMLMessage("Note Application Reset Password Code", email, content);
    }

    @Override
    public void sendPasswordChangedNotification(String firstname, String email) {
        Context context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("supportEmail", from);

        String templateName = "password-changed-notification-message";
        String content = templateEngine.process(templateName, context);
        sendHTMLMessage("Note Application Password Changed", email, content);
    }


    @SneakyThrows
    private void sendHTMLMessage(String subject, String to, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(from);
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setContent(content, MediaType.TEXT_HTML_VALUE);
        message.setSubject(subject);
        javaMailSender.send(message);
    }
}
