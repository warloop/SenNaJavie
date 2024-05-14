package org.example.forum.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @brief Klasa odpowiadajaca, za przechowywanie informacji dotyczących zgłoszenia naruszeń regulaminu.
 *
 *  Klasa odnosi się do naruszeń względem odpowiedzi użytkownika
 *
 * @author Artur Leszczak
 */

@Entity
@Table(name="opinions_reports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString

public class Opinions_reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "opinion_id")
    private long opinion_id;

    @NonNull
    @Column(name = "report_type")
    private int report_type;

    @NonNull
    @Column(name = "user_reporter_id")
    private int user_reporter_id;

    @NonNull
    @Column(name = "add_date")
    private LocalDateTime report_date;

}
