package se.magus.microservices.core.chat;

import lombok.Data;

@Data
public class PublishMessageRequest {
    private String channelId;
    private String message;
}
