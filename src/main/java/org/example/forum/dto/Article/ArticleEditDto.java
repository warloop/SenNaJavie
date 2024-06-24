package org.example.forum.dto.Article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleEditDto {

    private long id;
    private int user_changer_id;
    private String article_new_text;

    public ArticleEditDto(long id, int user_changer_id, String article_new_text) {
        this.id = id;
        this.user_changer_id = user_changer_id;
        this.article_new_text = article_new_text;
    }

}
