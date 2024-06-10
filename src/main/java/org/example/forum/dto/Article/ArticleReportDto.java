package org.example.forum.dto.Article;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
@Getter
@Setter
public class ArticleReportDto {

    private long articleId;
    private int reporterId;
    private int reasonId;
    private Optional<String> reason;

}
