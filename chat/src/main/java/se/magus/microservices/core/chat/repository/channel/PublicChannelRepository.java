package se.magus.microservices.core.chat.repository.channel;

import org.springframework.data.jpa.repository.JpaRepository;
import se.magus.microservices.core.chat.models.PublicChanel;

import java.util.UUID;

public interface PublicChannelRepository extends JpaRepository<PublicChanel, UUID> {
}
