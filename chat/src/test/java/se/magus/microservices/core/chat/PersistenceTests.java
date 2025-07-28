package se.magus.microservices.core.chat;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import se.magus.microservices.core.chat.models.MessageType;
import se.magus.microservices.core.chat.models.PublicChannel;
import se.magus.microservices.core.chat.models.PublicMessage;
import se.magus.microservices.core.chat.models.User;
import se.magus.microservices.core.chat.repository.channel.PublicChannelRepository;
import se.magus.microservices.core.chat.repository.message.PublicMessageRepository;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PersistenceTests extends PostgresTestBase{

    private static final Logger log = LoggerFactory.getLogger(PersistenceTests.class);
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PublicChannelRepository channelRepository;

   @Autowired
   private PublicMessageRepository messageRepository;

    @Test
    void testPersistPublicChannel() {
        PublicChannel channel = new PublicChannel();
        channel.setName("test-channel");
        channel.setVersion(Instant.now());

        PublicChannel savedChannel = entityManager.persistFlushFind(channel);

        assertNotNull(savedChannel.getId());
        assertEquals("test-channel", savedChannel.getName());
        assertNotNull(savedChannel.getVersion());
        assertNotNull(savedChannel.getCreatedAt());
        assertNotNull(savedChannel.getUpdatedAt());
    }

    @Test
    void testPublicChannelNameNotNullConstraint() {
        PublicChannel channel = new PublicChannel();
        channel.setVersion(Instant.now()); // Name is null

        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(channel));
    }

    @Test
    void testPublicChannelUniqueNameConstraint() {
        PublicChannel channel1 = new PublicChannel();
        channel1.setName("unique-channel");
        channel1.setVersion(Instant.now());
        entityManager.persistAndFlush(channel1);

        PublicChannel channel2 = new PublicChannel();
        channel2.setName("unique-channel");
        channel2.setVersion(Instant.now());

        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(channel2));
    }

    @Test
    void testPublicChannelNameLengthConstraint() {
        PublicChannel channel = new PublicChannel();
        channel.setName("a".repeat(129)); // Exceeds length=128
        channel.setVersion(Instant.now());

        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(channel));
    }

    @Test
    void testPersistPublicMessage() {
        User user = new User();
        user.setUsername("test-user");
        user = entityManager.persistAndFlush(user);

        PublicChannel channel = new PublicChannel();
        channel.setName("test-channel");
        channel.setVersion(Instant.now());
        channel = entityManager.persistAndFlush(channel);

        PublicMessage message = new PublicMessage();
        message.setChannel(channel);
        message.setFrom(user);
        message.setContent("Hello, world!");
        message.setMessageType(MessageType.MESSAGE);
        message.setVersion(Instant.now());

        PublicMessage savedMessage = entityManager.persistFlushFind(message);

        assertNotNull(savedMessage.getId());
        assertEquals("Hello, world!", savedMessage.getContent());
        assertEquals(MessageType.MESSAGE, savedMessage.getMessageType());
        assertEquals(channel.getId(), savedMessage.getChannel().getId());
        assertEquals(user.getId(), savedMessage.getFrom().getId());
        assertNotNull(savedMessage.getVersion());
        assertNotNull(savedMessage.getCreatedAt());
        assertNotNull(savedMessage.getUpdatedAt());
    }

    @Test
    void testPublicMessageNotNullConstraints() {
        PublicMessage message = new PublicMessage();
        message.setVersion(Instant.now()); // channel, from, content are null

        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(message));
    }

    @Test
    void testCascadeDeletePublicChannel() {
        User user = new User();
        user.setUsername("test-user");
        user = entityManager.persistAndFlush(user);

        PublicChannel channel = new PublicChannel();
        channel.setName("test-channel");
        channel.setVersion(Instant.now());
        channel = entityManager.persistAndFlush(channel);


        PublicMessage message = new PublicMessage();
        message.setChannel(channel);
        message.setFrom(user);
        message.setContent("Hello, world!");
        message.setMessageType(MessageType.MESSAGE);
        message.setVersion(Instant.now());
        message = entityManager.persistAndFlush(message);
        log.info("Channel info: " + channel.toString());
        log.info("message info: " + message.toString());

        entityManager.remove(channel);
        entityManager.flush();

        assertNull(entityManager.find(PublicChannel.class, channel.getId()));
        assertNull(entityManager.find(PublicMessage.class, message.getId()));
    }

    @Test
    void testOneToManyRelationship() {
        User user = new User();
        user.setUsername("test-user");
        user = entityManager.persistAndFlush(user);

        PublicChannel channel = new PublicChannel();
        channel.setName("test-channel");
        channel.setVersion(Instant.now());
        channel.setMessages(List.of());
        channel = entityManager.persistAndFlush(channel);

        PublicMessage message1 = new PublicMessage();
        message1.setChannel(channel);
        message1.setFrom(user);
        message1.setContent("Message 1");
        message1.setMessageType(MessageType.MESSAGE);
        message1.setVersion(Instant.now());

        PublicMessage message2 = new PublicMessage();
        message2.setChannel(channel);
        message2.setFrom(user);
        message2.setContent("Message 2");
        message2.setMessageType(MessageType.MESSAGE);
        message2.setVersion(Instant.now());

        channel.getMessages().add(message1);
        channel.getMessages().add(message2);
        entityManager.persistAndFlush(message1);
        entityManager.persistAndFlush(message2);

        PublicChannel fetchedChannel = entityManager.find(PublicChannel.class, channel.getId());

        assertEquals(2, fetchedChannel.getMessages().size());
        assertEquals("Message 1", fetchedChannel.getMessages().get(0).getContent());
        assertEquals("Message 2", fetchedChannel.getMessages().get(1).getContent());
    }

    @Test
    void testOrphanRemoval() {
        User user = new User();
        user.setUsername("test-user");
        user = entityManager.persistAndFlush(user);

        PublicChannel channel = new PublicChannel();
        channel.setName("test-channel");
        channel.setVersion(Instant.now());
        channel.setMessages(List.of());
        channel = entityManager.persistAndFlush(channel);

        PublicMessage message = new PublicMessage();
        message.setChannel(channel);
        message.setFrom(user);
        message.setContent("Orphaned message");
        message.setMessageType(MessageType.MESSAGE);
        message.setVersion(Instant.now());
        channel.getMessages().add(message);
        entityManager.persistAndFlush(message);

        channel.getMessages().remove(message);
        entityManager.merge(channel);
        entityManager.flush();

        assertNull(entityManager.find(PublicMessage.class, message.getId()));
    }

    @Test
    void testLazyLoading() {
        User user = new User();
        user.setUsername("test-user");
        user = entityManager.persistAndFlush(user);

        PublicChannel channel = new PublicChannel();
        channel.setName("test-channel");
        channel.setVersion(Instant.now());
        channel = entityManager.persistAndFlush(channel);

        PublicMessage message = new PublicMessage();
        message.setChannel(channel);
        message.setFrom(user);
        message.setContent("Lazy load test");
        message.setMessageType(MessageType.MESSAGE);
        message.setVersion(Instant.now());
        entityManager.persistAndFlush(message);

        entityManager.clear(); // Clear persistence context to ensure lazy loading

        PublicMessage fetchedMessage = entityManager.find(PublicMessage.class, message.getId());
        assertNotNull(fetchedMessage);

        // Accessing lazy-loaded relationships
        assertDoesNotThrow(() -> {
            fetchedMessage.getChannel().getName();
            fetchedMessage.getFrom().getUsername();
        });
    }

    @Test
    void testVersioning() {
        PublicChannel channel = new PublicChannel();
        channel.setName("test-channel");
        channel.setVersion(Instant.now());
        channel = entityManager.persistAndFlush(channel);

        Instant originalVersion = channel.getVersion();
        channel.setName("updated-channel");
        PublicChannel updatedChannel = entityManager.merge(channel);
        entityManager.flush();

        assertTrue(updatedChannel.getVersion().isAfter(originalVersion));
    }

    @Test
    void testBatchSizeForOneToMany() {
        User user = new User();
        user.setUsername("test-user");
        user = entityManager.persistAndFlush(user);

        PublicChannel channel = new PublicChannel();
        channel.setName("test-channel");
        channel.setVersion(Instant.now());
        channel.setMessages(List.of());
        channel = entityManager.persistAndFlush(channel);

        for (int i = 0; i < 150; i++) { // Exceed BatchSize=100
            PublicMessage message = new PublicMessage();
            message.setChannel(channel);
            message.setFrom(user);
            message.setContent("Message " + i);
            message.setMessageType(MessageType.MESSAGE);
            message.setVersion(Instant.now());
            channel.getMessages().add(message);
            entityManager.persistAndFlush(message);
        }

        entityManager.clear();
        PublicChannel fetchedChannel = entityManager.find(PublicChannel.class, channel.getId());
        assertEquals(150, fetchedChannel.getMessages().size());
    }
}
