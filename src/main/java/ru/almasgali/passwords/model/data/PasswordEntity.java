package ru.almasgali.passwords.model.data;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "password", schema = "public")
@Audited
public class PasswordEntity {
    @Id
    @NotAudited
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String comment;
    private String password;
    private LocalDateTime updatedAt;
}
