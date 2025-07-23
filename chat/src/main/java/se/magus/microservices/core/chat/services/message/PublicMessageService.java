package se.magus.microservices.core.chat.services.message;

import se.magus.microservices.core.chat.data.message.PublicMessageDto;
import se.magus.microservices.core.chat.exception.ChannelDoesNotExist;

public interface PublicMessageService {
    PublicMessageDto createMessage(String fromUserId, String channelId, String message) throws ChannelDoesNotExist;

    void deliverMessage(PublicMessageDto message);
}
