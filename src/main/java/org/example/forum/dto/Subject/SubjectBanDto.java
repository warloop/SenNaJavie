package org.example.forum.dto.Subject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectBanDto {
    private long subjectId;
    private boolean ban_value;
    private int user_id;

    public SubjectBanDto(long subjectId,int user_id, boolean ban_value) {
        this.subjectId = subjectId;
        this.user_id = user_id;
        this.ban_value = ban_value;
    }
}
