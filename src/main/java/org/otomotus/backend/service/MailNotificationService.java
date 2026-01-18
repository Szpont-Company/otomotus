package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.entity.MessageEntity;
import org.otomotus.backend.entity.UserEntity;
import org.otomotus.backend.exception.ResourceNotFoundException;
import org.otomotus.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailNotificationService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public void notifyMessage(MessageEntity msg) {
        UserEntity recipient = userRepository.findById(msg.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        UserEntity sender = userRepository.findById(msg.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        String content = "You've got a new message from " + sender.getFirstName() + " " + sender.getLastName() + ":\n\n"
                + msg.getMsgContent()
                + "\n\nLog in to respond: http://localhost:5173/login";

        emailService.sendNotificationEmail(recipient.getEmail(), "New message on OtomotUZ!", content);
    }
}
