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
@Table(name="article_actions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Article_actions {

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
    @Column(name = "action_type_id")
    private int action_type_id;

}
