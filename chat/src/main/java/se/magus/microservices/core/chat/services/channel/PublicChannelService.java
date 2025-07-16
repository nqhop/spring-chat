package se.magus.microservices.core.chat.services.channel;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import se.magus.microservices.core.chat.data.message.PublicMessageDto;
import se.magus.microservices.core.chat.exception.ChannelDoesNotExist;

import java.util.Set;

public interface PublicChannelService {
    SseEmitter subscribe(String channelId) throws ChannelDoesNotExist;

    void sendToSubscriber(Set<Object> subscribers, PublicMessageDto message);
}
