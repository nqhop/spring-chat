package se.magus.microservices.core.chat.services.channel;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import se.magus.microservices.core.chat.data.message.PublicMessageDto;
import se.magus.microservices.core.chat.exception.ChannelDoesNotExist;
import se.magus.microservices.core.chat.repository.channel.PublicChannelRepository;
import se.magus.microservices.core.chat.utils.SseUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PublicChannelServiceImpl implements PublicChannelService {

    private final Map<String, Set<Object>> listeningChannels = new ConcurrentHashMap<>();
    private final MeterRegistry meterRegistry;
    private final PublicChannelRepository channelRepository;


    public PublicChannelServiceImpl(MeterRegistry meterRegistry, PublicChannelRepository channelRepository) {
        this.meterRegistry = meterRegistry;
        this.channelRepository = channelRepository;
    }

    @PostConstruct
    private void afterInjection() {
        initMetrics(meterRegistry);
    }

    private void initMetrics(MeterRegistry meterRegistry) {
            Gauge.builder(
                    "name.public.channel.online.users",
                    listeningChannels,
                    l -> l.values().stream().mapToDouble(Set::size).sum())
                    .register(meterRegistry);
    }

    @Override
    public SseEmitter subscribe(String channelId) throws ChannelDoesNotExist {
        getChannelById(channelId);
        SseEmitter subscriber = createChannelSubscriber(channelId);
        SseUtil.sendConnectEvent(subscriber);
        return subscriber;
    }

    private void getChannelById(String channelId) throws ChannelDoesNotExist {
        channelRepository
                .findById(UUID.fromString(channelId))
                .orElseThrow(() -> new ChannelDoesNotExist("Channel with id=%s does not exist !".formatted(channelId)));
    }

    @Override
    public void sendToSubscriber(Set<Object> subscribers, PublicMessageDto message) {

    }

    private SseEmitter createChannelSubscriber(String channelId) {
        SseEmitter subscriber = new SseEmitter(120000L);
        return subscriber;
    }


}
