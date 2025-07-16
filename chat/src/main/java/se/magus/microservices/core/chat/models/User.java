package se.magus.microservices.core.chat.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ManyToAny;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@BatchSize(size = 100)
@Table(name = "account_user")
public class User {
    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private String id;

    @Column(unique = true, nullable = false, length = 32)
    private String username;

}
