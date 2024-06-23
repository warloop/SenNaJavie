package org.example.forum.dto.Subject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectAddDto {

    private int user_adder_id;
    private String subject_text;

    public SubjectAddDto(int user_adder_id, String subject_text) {
        this.user_adder_id = user_adder_id;
        this.subject_text = subject_text;
    }


}
