package se.magus.microservices.core.chat.services.channel;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class PublicChannelServiceImpl implements PublicChannelService {
    @Override
    public SseEmitter subscribe(String channelId) {
        SseEmitter subscriber = createChannelSubscriber(channelId);

        return null;
    }

    private SseEmitter createChannelSubscriber(String channelId) {
        SseEmitter emitter = new SseEmitter(120000L);
        return emitter;
    }
}
