package se.magus.microservices.core.chat.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ManyToAny;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@BatchSize(size = 100)
@Table(name = "account_user")
public class User extends TimeStampBase{

    @Column(unique = true, nullable = false, length = 32)
    private String username;

    @Override
    public boolean equals(Object o){
        if(this == o) return  true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(user.id, this.id) && Objects.equals(user.username, this.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
