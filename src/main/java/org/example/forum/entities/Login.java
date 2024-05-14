package org.example.forum.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="login")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user_id;

    @NonNull
    @Column(name = "login", nullable = false)
    private String login;

    @NonNull
    @Column(name = "password", nullable = false)
    private String password;

    @NonNull
    @Column(name = "active")
    private boolean active = true;
}

