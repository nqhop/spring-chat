package se.magus.microservices.core.chat.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "public_message")
public class PublicMessage extends TimeStampBase {

    @Version
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    private Instant version;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PublicChanel chanel;

    @Column(length = 32, nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType messageType = MessageType.MESSAGE;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User from;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}
