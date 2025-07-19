package se.magus.microservices.core.chat.services.message;

import org.springframework.stereotype.Service;
import se.magus.microservices.core.chat.data.message.PublicMessageDto;
import se.magus.microservices.core.chat.models.PublicMessage;
import se.magus.microservices.core.chat.repository.message.PublicMessageRepository;
import se.magus.microservices.core.chat.services.nats.NatsService;
import se.magus.microservices.core.chat.utils.ChannelSubject;

@Service
public class PublicMessageServiceImpl implements PublicMessageService {
    private final NatsService natsService;
    private final PublicMessageRepository messageRepository;

    public PublicMessageServiceImpl(NatsService natsService, PublicMessageRepository publicMessageRepository) {
        this.natsService = natsService;
        this.messageRepository = publicMessageRepository;
    }

    @Override
    public PublicMessageDto createMessage(String fromUserId, String channelId, String message) {
        PublicMessage publicMessage =
                PublicMessage.builder()
                        .content(message)
                        .build();
        messageRepository.save(publicMessage);
        return new PublicMessageDto(publicMessage);
    }

    @Override
    public void deliverMessage(PublicMessageDto message) {
        natsService.publish(
                ChannelSubject.publicChannelSubject(message.getChannel().toString()), message
        );
    }
}
