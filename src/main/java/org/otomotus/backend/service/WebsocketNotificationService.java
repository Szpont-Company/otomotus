package org.otomotus.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebsocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendTestMessage(String message) {
        messagingTemplate.convertAndSend("/topic", message);
    }
}
