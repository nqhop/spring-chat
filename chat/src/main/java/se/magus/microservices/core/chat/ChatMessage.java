package se.magus.microservices.core.chat;

import lombok.Data;

@Data
public class ChatMessage {
    private String content;
    private String sender;
}
