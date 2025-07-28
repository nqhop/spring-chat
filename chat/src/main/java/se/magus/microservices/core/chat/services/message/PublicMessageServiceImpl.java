package se.magus.microservices.core.chat.services.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.magus.microservices.core.chat.data.message.PublicMessageDto;
import se.magus.microservices.core.chat.exception.ChannelDoesNotExist;
import se.magus.microservices.core.chat.models.PublicChannel;
import se.magus.microservices.core.chat.models.PublicMessage;
import se.magus.microservices.core.chat.repository.channel.PublicChannelRepository;
import se.magus.microservices.core.chat.repository.message.PublicMessageRepository;
import se.magus.microservices.core.chat.services.nats.NatsService;
import se.magus.microservices.core.chat.utils.ChannelSubject;

import java.util.UUID;

@Slf4j
@Service
public class PublicMessageServiceImpl implements PublicMessageService {
    private final NatsService natsService;
    private final PublicMessageRepository messageRepository;
    private final PublicChannelRepository channelRepository;

    public PublicMessageServiceImpl(NatsService natsService, PublicMessageRepository publicMessageRepository, PublicChannelRepository channelRepository) {
        this.natsService = natsService;
        this.messageRepository = publicMessageRepository;
        this.channelRepository = channelRepository;
    }

    private PublicChannel getChannelById(String channelId) throws ChannelDoesNotExist {
        return channelRepository
                .findById(UUID.fromString(channelId))
                .orElseThrow(() ->
                    new ChannelDoesNotExist(
                            "channel with id=%s does not exist !".formatted(channelId)
                    ));
    }

    @Override
    public PublicMessageDto createMessage(String fromUserId, String channelId, String message) throws ChannelDoesNotExist {
        PublicChannel channel = getChannelById(channelId);
        log.info("getChannelById for createMessage: " + channel.getName());
        PublicMessage publicMessage =
                PublicMessage.builder()
                        .channel(channel)
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
