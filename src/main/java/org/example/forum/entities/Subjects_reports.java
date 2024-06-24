package org.example.forum.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @brief Klasa odpowiadajaca, za przechowywanie informacji dotyczących zgłoszenia naruszeń regulaminu.
 *
 *  Klasa odnosi się do naruszeń względem otwartego tematu. Np. nazwa tematu jest wulgarna.
 *
 * @author Artur Leszczak
 */

@Entity
@Table(name="subject_reports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Subjects_reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = "subject_id")
    private long subject_id;

    @NonNull
    @Column(name = "report_type")
    private int report_type;

    @NonNull
    @Column(name = "user_reporter_id")
    private int user_reporter_id;

    @NonNull
    @Column(name = "add_date")
    private LocalDateTime report_date;

    @NonNull
    @Column(name = "is_viewed")
    private boolean is_viewed;

}