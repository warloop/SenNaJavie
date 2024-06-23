package org.example.forum.dto.Section;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionEditDto {
    private long id;
    private int user_adder_id;
    private long article_id;
    private String section_text;
    private boolean is_visible;
    private boolean is_banned;
    private boolean is_deleted;

    public SectionEditDto(long id, long article_id, String section_text) {
        this.id = id;
        this.article_id = article_id;
        this.section_text = section_text;
    }
}
