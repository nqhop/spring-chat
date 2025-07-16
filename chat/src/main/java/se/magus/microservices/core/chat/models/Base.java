package se.magus.microservices.core.chat.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@MappedSuperclass
public class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    UUID id;
}
