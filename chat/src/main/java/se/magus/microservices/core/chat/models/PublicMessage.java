package se.magus.microservices.core.chat.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.Objects;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "public_message")
public class PublicMessage extends TimeStampBase {

    @Version
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
    private Instant version;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PublicChannel channel;

    @Column(length = 32, nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageType messageType = MessageType.MESSAGE;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User from;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    public void setChannel(PublicChannel channel){
        this.channel = channel;
        channel.setMessages(this);
    }
}
