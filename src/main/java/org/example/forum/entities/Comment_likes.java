package org.example.forum.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @brief Klasa tworząca obiekt zbierający informacje o polubieniach na temat komentarzy użytkowników
 * @author Artur Leszczak
 */

@Entity
@Table(name="comment_likes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Comment_likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = "comment_id")
    private long comment_id;

    @NonNull
    @Column(name = "user_adder_id")
    private long user_id;

    @NonNull
    @Column(name = "add_date")
    private LocalDateTime add_date;

    @NonNull
    @Column(name = "is_deleted")
    private boolean is_deleted = false;

    @Column(name = "deleted_date")
    private LocalDateTime deleted_date;

}
