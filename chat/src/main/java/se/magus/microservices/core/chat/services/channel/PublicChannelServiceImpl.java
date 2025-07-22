package se.magus.microservices.core.chat.services.channel;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import se.magus.microservices.core.chat.data.message.PublicMessageDto;
import se.magus.microservices.core.chat.exception.ChannelDoesNotExist;
import se.magus.microservices.core.chat.repository.channel.PublicChannelRepository;
import se.magus.microservices.core.chat.utils.ChannelSubject;
import se.magus.microservices.core.chat.utils.SseUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class PublicChannelServiceImpl implements PublicChannelService {

    private final Map<String, Set<Object>> listeningChannels = new ConcurrentHashMap<>();
    private final MeterRegistry meterRegistry;
    private final PublicChannelRepository channelRepository;
    private Dispatcher dispatcher;
    private final Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(PublicChannelServiceImpl.class);

    public PublicChannelServiceImpl(MeterRegistry meterRegistry, PublicChannelRepository channelRepository,
            Connection connection) {
        this.meterRegistry = meterRegistry;
        this.channelRepository = channelRepository;
        this.connection = connection;
    }

    @PostConstruct
    private void afterInjection() {
        initMetrics(meterRegistry);
        initNats(connection);
    }

    private void initNats(Connection connection) {
        dispatcher = connection.createDispatcher(msg -> {
            try {
                String subject = msg.getSubject();
                String data = new String(msg.getData());
                log.info("📩 Received message on [" + subject + "]: " + data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        dispatcher.subscribe("chat.public");
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

        listeningChannels.compute(
                channelId,
                (key, subscribers) -> {
                    if (subscribers == null) {
                        dispatcher.subscribe(ChannelSubject.publicChannelSubject(channelId));
                        subscribers = Collections.synchronizedSet(new HashSet<>());

                        subscribers.add(subscriber);
                        logger.info(
                                "PublicChannel " + channelId + " now has " + subscribers.size() + " subscribers");
                    }
                    return subscribers;
                });
        return subscriber;
    }

}
