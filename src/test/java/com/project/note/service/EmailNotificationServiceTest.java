package com.project.note.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmailNotificationServiceTest {

    @Autowired
    EmailNotificationService emailNotificationService;

    @MockBean
    JavaMailSender javaMailSender;

    @Captor
    ArgumentCaptor<MimeMessage> mimeMessageArgumentCaptor;

    JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();

    @Value("${spring.mail.username}")
    String from;

    @BeforeEach
    public void setUp() {
        when(javaMailSender.createMimeMessage()).thenReturn(javaMailSenderImpl.createMimeMessage());
    }

    @Test
    public void testSendResetPasswordCode() throws MessagingException, IOException {
        emailNotificationService.sendResetPasswordCode(
                "John",
                "abc@example.com",
                "secret"
        );

        verify(javaMailSender).send(mimeMessageArgumentCaptor.capture());

        String content = mimeMessageArgumentCaptor.getValue().getContent().toString();

        assertTrue(content.contains("Dear John"));
        assertTrue(content.contains("secret"));
    }

    @Test
    public void testSendPasswordChangedNotification() throws MessagingException, IOException {
        emailNotificationService.sendPasswordChangedNotification(
                "John",
                "abc@example.com"
        );

        verify(javaMailSender).send(mimeMessageArgumentCaptor.capture());

        String content = mimeMessageArgumentCaptor.getValue().getContent().toString();

        assertTrue(content.contains("Dear John"));
        assertTrue(content.contains(from));
    }

    @Test
    public void testSendVerificationCode() throws MessagingException, IOException {
        emailNotificationService.sendVerificationCode(
                "John",
                "abc@example.com",
                "secret"
        );

        verify(javaMailSender).send(mimeMessageArgumentCaptor.capture());

        String content = mimeMessageArgumentCaptor.getValue().getContent().toString();

        assertTrue(content.contains("Dear John"));
        assertTrue(content.contains("secret"));
    }
}