package se.magus.microservices.core.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public class RmeSessionChannelInterceptor implements ChannelInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RmeSessionChannelInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Channel Interceptor..........");
        MessageHeaders headers = message.getHeaders();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        MultiValueMap<String, String> multiValueMap =
                headers.get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);

        for(Map.Entry<String, List<String>> head : multiValueMap.entrySet()) {
            log.info("Header: " + head.getKey() + "#" + head.getValue());
        }
        return message;
    }
}
