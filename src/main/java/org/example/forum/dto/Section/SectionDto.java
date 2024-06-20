package org.example.forum.dto.Section;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionDto {
    private long id;
    private int user_adder_id;
    private long article_id;
    private String section_text;
    private boolean is_visible;
    private boolean is_banned;
    private boolean is_deleted;
}
