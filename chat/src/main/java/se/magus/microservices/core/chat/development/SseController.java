package se.magus.microservices.core.chat.development;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Controller
@RequestMapping("/sse")
@CrossOrigin(origins = "*")
public class SseController {

    /*
    http://localhost:8081/sse/events
     */
    @GetMapping(path = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents(){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                for(int i = 0; i < 10; i++){
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .name("message")
                            .id(String.valueOf(i))
                            .data("Event #" + i);

                    log.info("SSE event #{}", i);
                        emitter.send(event);
                        Thread.sleep(1000);
                    }
                emitter.complete();
            }
            catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
    /*
        Handling Multiple clients
     */
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(path = "/subscribe/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String clientId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(clientId, emitter);

        emitter.onCompletion(() -> emitters.remove(clientId));
        emitter.onTimeout(() -> emitters.remove(clientId));
        emitter.onError((e) -> emitters.remove(clientId));

        return emitter;
    }


    /*
    http://localhost:8081/sse/subscribe0
     */
    @GetMapping(path = "/subscribe0", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe0() {
        String clientId = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
//        emitters.put(clientId, emitter);

//        emitter.onCompletion(() -> emitters.remove(clientId));
//        emitter.onTimeout(() -> emitters.remove(clientId));
//        emitter.onError((e) -> emitters.remove(clientId));

        return emitter;
    }

    @PostMapping("/broadcast")
    public void broadcast(@RequestBody String message) {
        emitters.forEach((clientId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("message").data(message));
            } catch (IOException e) {
                emitters.remove(clientId);
                emitter.completeWithError(e);
            }
        });
    }
}


/*
note: Using security for CrossOrigin
 */