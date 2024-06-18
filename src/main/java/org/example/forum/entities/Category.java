package org.example.forum.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    @Column(name = "category_name")
    private String category_name;
}
