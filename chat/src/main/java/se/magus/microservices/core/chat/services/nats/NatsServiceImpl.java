package se.magus.microservices.core.chat.services.nats;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;
import io.nats.client.Subscription;
import io.nats.client.impl.NatsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.magus.microservices.core.chat.data.message.PublicMessageDto;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class NatsServiceImpl implements NatsService {
    private final Connection natConnection;
    private final ObjectMapper objectMapper;
    private final Dispatcher dispatcher;

    public NatsServiceImpl(Connection natConnection, ObjectMapper objectMapper, Dispatcher dispatcher) {
        this.natConnection = natConnection;
        this.objectMapper = objectMapper;
        this.dispatcher = dispatcher;
    }

    public void publish(String subject, String message){
        natConnection.publish(subject, message.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void publish(String subject, PublicMessageDto message) {
        try{
            publish(subject, objectMapper.writeValueAsString(message));
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }

    @Override
    public Subscription subscribe(String subject, MessageHandler handler) {
        return dispatcher.subscribe(subject, handler);
    }
}
