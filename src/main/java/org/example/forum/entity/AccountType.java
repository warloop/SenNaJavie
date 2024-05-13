package org.example.forum.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="account_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString

public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "description")
    private String description;

}
