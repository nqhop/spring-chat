package se.magus.microservices.core.chat.data.message;

import se.magus.microservices.core.chat.models.MessageType;
import se.magus.microservices.core.chat.models.PublicMessage;

import java.util.UUID;

public class PublicMessageDto extends MessageDto {

    public PublicMessageDto(PublicMessage message) {
        super(
                message.getId(),
                message.getVersion(),
                message.getContent(),
                message.getChanel().getId(),
                message.getMessageType(),
                message.getCreatedAt().toString(),
                message.getCreatedAt().toString());
    }
}
