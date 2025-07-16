package se.magus.microservices.core.chat.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class SseUtil {
    public static void sendConnectEvent(SseEmitter sseEmitter) {
        try {
            sseEmitter.send("[]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
