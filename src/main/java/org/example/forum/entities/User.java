package org.example.forum.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "surname")
    private String surname;

    @NonNull
    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_type_id", referencedColumnName = "id")
    private AccountType accountType;

    @NonNull
    @Column(name = "register_date", insertable = false, updatable = false)
    private LocalDateTime register_date = LocalDateTime.now();

    @NonNull
    @Column(name = "is_deleted")
    private boolean is_deleted = false;

    @Column(name = "delete_date")
    private LocalDateTime delete_date = null;

    public boolean getIs_deleted(){
        return this.is_deleted;
    }
}
