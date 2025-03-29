package com.eldritch.chat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat/{sessionId}")
    @SendTo("/topic/chat/{sessionId}")
    public ChatMessage handleChatMessage(@DestinationVariable String sessionId, ChatMessage message) {
        return message; // Broadcast the message to all subscribers
    }
}