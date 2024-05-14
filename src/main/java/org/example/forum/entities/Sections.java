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
@Table(name="sections")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Sections{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = "user_adder_id")
    private int user_adder_id;

    @NonNull
    @Column(name = "add_date")
    private LocalDateTime add_date;

    @NonNull
    @Column(name = "article_id")
    private long article_id;

    @NonNull
    @Column(name = "section_text")
    private String section_text;

    @NonNull
    @Column(name = "is_visible")
    private boolean is_visible = true;

    @NonNull
    @Column(name = "is_banned")
    private boolean is_banned = false;

    @NonNull
    @Column(name = "is_deleted")
    private boolean is_deleted = false;

}
