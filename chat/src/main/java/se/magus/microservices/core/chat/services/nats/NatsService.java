package se.magus.microservices.core.chat.services.nats;

import io.nats.client.MessageHandler;
import io.nats.client.Subscription;
import io.nats.client.impl.NatsMessage;
import se.magus.microservices.core.chat.data.message.PublicMessageDto;

public interface NatsService {
    void publish(String subject, PublicMessageDto message);

    Subscription subscribe(String subject, MessageHandler handler);
}
