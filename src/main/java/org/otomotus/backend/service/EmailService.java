package org.otomotus.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
}
