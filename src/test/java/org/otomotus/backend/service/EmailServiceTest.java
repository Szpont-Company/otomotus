package org.otomotus.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @Test
    @DisplayName("sendVerificationEmail: Should send email with correct recipient and link")
    void sendVerificationEmail_ShouldSendCorrectEmail() {
        // Given
        String to = "user@example.com";
        String verificationUrl = "http://localhost:8080/verify?token=123";

        // When
        emailService.sendVerificationEmail(to, verificationUrl);

        // Then
        verify(mailSender).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals("Otomotus - Aktywacja konta", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains(verificationUrl));
    }

    @Test
    @DisplayName("sendWelcomeEmail: Should send welcome email with username")
    void sendWelcomeEmail_ShouldSendCorrectEmail() {
        // Given
        String to = "user@example.com";
        String username = "JohnDoe";

        // When
        emailService.sendWelcomeEmail(to, username);

        // Then
        verify(mailSender).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals(to, sentMessage.getTo()[0]);
        assertTrue(sentMessage.getText().contains(username));
        assertTrue(sentMessage.getSubject().contains("Witaj w Otomotus"));
    }
}