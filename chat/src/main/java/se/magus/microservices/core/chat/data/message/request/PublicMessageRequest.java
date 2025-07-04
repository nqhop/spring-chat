package se.magus.microservices.core.chat.data.message.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicMessageRequest {
    private String channelId;
    private String message;
}
