package se.magus.microservices.core.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import se.magus.microservices.core.chat.ChatMessage;

@Controller
public class ChatController {


    /**
     *
     * Client sends to /app/news
     */
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }
}
