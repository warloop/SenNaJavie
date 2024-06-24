package org.example.forum.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.expression.spel.ast.NullLiteral;

import java.time.LocalDateTime;

/**
 * @brief Klasa tworząca obiekt zbierający informacje o komentarzach użytkowników dotyczących artykułów lub odpowiedzi na konkretne komentarze
 * @author Artur Leszczak
 */

@Entity
@Table(name="comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = "article_id")
    private long articleId;

    @NonNull
    @Column(name = "is_answer_to_comment")
    private boolean isAnswerToComment = false;

    @Column(name = "comment_id")
    private long commentId;

    @NonNull
    @Column(name = "user_adder_id")
    private int user_adder_id;

    @NonNull
    @Column(name = "add_date")
    private LocalDateTime add_date = LocalDateTime.now();

    @NonNull
    @Column(name = "comment_text")
    private String comment_text;

    @NonNull
    @Column(name = "comment_mark")
    /*from 0-5*/
    private short comment_mark;

    @NonNull
    @Column(name = "likes")
    private int likes;

    @NonNull
    @Column(name = "dislikes")
    private int dislikes;

    @NonNull
    @Column(name = "is_banned")
    private boolean isBanned = false;

    @NonNull
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "deleted_date")
    private LocalDateTime deleted_date = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_adder_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

}
