package se.magus.microservices.core.chat.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
@BatchSize(size = 100)
@Table(name = "public_channel")
public class PublicChannel extends TimeStampBase{

    @Version
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    private Instant version;

    @Column(nullable = false, unique = true, updatable = false, length = 128)
    String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channel", orphanRemoval = true)
    @BatchSize(size = 100)
    List<PublicMessage> messages;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (!(o instanceof PublicChannel channel)) return false;
        return Objects.equals(id, channel.id) && Objects.equals(name, channel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public void setMessages(List<PublicMessage> messages) {
        this.messages = messages;
    }
}
