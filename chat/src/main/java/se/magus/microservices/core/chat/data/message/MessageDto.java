package se.magus.microservices.core.chat.data.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.magus.microservices.core.chat.models.MessageType;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private UUID id;

    private Instant version;

    private String content;

    private UUID channel;

    private MessageType messageType;

    private String createAt;

    private String updateAt;
}
