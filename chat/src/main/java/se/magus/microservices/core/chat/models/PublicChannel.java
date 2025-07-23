package se.magus.microservices.core.chat.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.Instant;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@BatchSize(size = 100)
@EqualsAndHashCode(callSuper = false)
@Table(name = "public_channel")
public class PublicChannel extends TimeStampBase {

    @Version
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    private Instant version;

    @Column(nullable = false, unique = true, updatable = false, length = 128)
    String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "channel", orphanRemoval = true)
    @BatchSize(size = 100)
    List<PublicMessage> messages;
}
