package se.magus.microservices.core.chat.services.message;

import se.magus.microservices.core.chat.data.message.PublicMessageDto;

public interface PublicMessageService {
    PublicMessageDto createMessage(String fromUserId, String channelId, String message);

    void deliverMessage(PublicMessageDto message);
}
