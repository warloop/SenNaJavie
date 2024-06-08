package org.example.forum.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.expression.spel.ast.NullLiteral;

import java.time.LocalDateTime;

/**
 * @brief
 * @author Artur Leszczak
 */

@Entity
@Table(name="articles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Articles{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = "user_adder_id")
    private int user_adder_id;

    @NonNull
    @Column(name = "subject_id")
    private long subject_id;

    @NonNull
    @Column(name = "article_title")
    private String article_title;

    @NonNull
    @Column(name = "is_visible")
    private boolean visible = false;

    @NonNull
    @Column(name = "is_banned")
    private boolean banned = false;

    @NonNull
    @Column(name = "is_deleted")
    private boolean deleted = false;

}
