package se.magus.microservices.core.chat.services.channel;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface PublicChannelService {
    SseEmitter subscribe(String channelId);
}
