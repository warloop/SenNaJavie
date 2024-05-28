package org.example.forum.dto.Subject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectEditDto {

    private long id;
    private int user_changer_id;
    private String subject_new_text;

    public SubjectEditDto(long id, int user_changer_id, String subject_new_text) {
        this.id = id;
        this.user_changer_id = user_changer_id;
        this.subject_new_text = subject_new_text;
    }

}
