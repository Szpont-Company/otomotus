package org.otomotus.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Serwis do wysyłania wiadomości email.
 * <p>
 * Obsługuje wysyłanie emaili weryfikacyjnych i powiadomień użytkowników.
 * Operacje są asynchroniczne (@Async), aby nie blokować głównego wątku.
 * </p>
 *
 * @author Otomotus Development Team
 * @version 1.0
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(String email,  String verificationUrl) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Otomotus - Aktywacja konta");
        mailMessage.setText("Kliknij w link aby aktywowac konto: " + verificationUrl);
        mailMessage.setFrom("kurde_3poczta_juz@onet.pl");

        mailSender.send(mailMessage);
    }

    @Async
    public void sendNotificationEmail(String to, String subject, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailMessage.setFrom("kurde_3poczta_juz@onet.pl");

        mailSender.send(mailMessage);
    }

    /**
     * Wysyła email powitalny po pomyślnej aktywacji konta.
     * <p>
     * Metoda wywoływana po tym, jak użytkownik kliknie w link weryfikacyjny.
     * </p>
     *
     * @param to adres email użytkownika
     * @param username nazwa użytkownika
     */
    @Async
    public void sendWelcomeEmail(String to, String username) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Witaj w Otomotus - Rejestracja zakończona pomyślnie");
        mailMessage.setText("Cześć " + username + "!\n\n" +
                "Twoje konto zostało pomyślnie aktywowane. Możesz teraz w pełni korzystać z serwisu Otomotus.\n" +
                "Dziękujemy za dołączenie do naszej społeczności!\n\n" +
                "Pozdrawiamy,\n" +
                "Zespół Otomotus");
        mailMessage.setFrom("kurde_3poczta_juz@onet.pl");

        mailSender.send(mailMessage);
    }
}
