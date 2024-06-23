package org.example.forum.dto.Article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDto {

    private long id;
    private long title_id;
    private boolean is_visible;
    private boolean is_banned;
    private boolean is_deleted;
    private int user_adder_id;

}
