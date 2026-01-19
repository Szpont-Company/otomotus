package org.otomotus.backend.config;

import lombok.RequiredArgsConstructor;
import org.otomotus.backend.auth.model.AuthUserDetails;
import org.otomotus.backend.repository.ConversationRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class WebSocketSecurityConfig implements ChannelInterceptor {
    private final ConversationRepository conversationRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userId = auth != null && auth.getPrincipal() instanceof AuthUserDetails userDetails ? userDetails.getId().toString() : null;

        if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();

            if(destination.startsWith("/topic/notifications")) {
                String recipientId = destination.replace("topic/notifications/", "");
                if(!recipientId.equals(userId)) {
                    throw new IllegalArgumentException("You don't have permission to access this topic!");
                }
            }
            if(destination.startsWith("/topic/conversations/")) {
                String conversationid = destination.replace("/topic/conversations/", "");
                if(!isUserInConversations(userId, conversationid)) {
                    throw new IllegalArgumentException("You don't have permission to access this topic!");
                }
            }
        }
        return message;
    }

    private boolean isUserInConversations(String userId, String conversationId) {
        try {
            UUID convId = UUID.fromString(conversationId);
            UUID uId = UUID.fromString(userId);
            return conversationRepository.existsByIdAndUser(convId,uId);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
