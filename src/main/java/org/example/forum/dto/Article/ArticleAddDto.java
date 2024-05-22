package org.example.forum.dto.Article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleAddDto {

    private int UserAdderId;
    private long SubjectId;
    private String Title;
    private boolean is_visible;
    private boolean is_banned;
    private boolean is_deleted;

    public ArticleAddDto(int UserAdderId, long SubjectId, String Title, boolean is_visible, boolean is_banned, boolean is_deleted) {
        this.UserAdderId = UserAdderId;
        this.SubjectId = SubjectId;
        this.Title = Title;
        this.is_visible = is_visible;
        this.is_banned = is_banned;
        this.is_deleted = is_deleted;
    }

}
