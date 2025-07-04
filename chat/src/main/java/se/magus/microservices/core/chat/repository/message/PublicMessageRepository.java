package se.magus.microservices.core.chat.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import se.magus.microservices.core.chat.models.PublicMessage;

import java.util.UUID;

public interface PublicMessageRepository extends JpaRepository<PublicMessage, UUID> {
}
