package org.example.forum.dao.Interfaces;

import org.example.forum.dto.Article.ArticleAddDto;

import java.util.Optional;

public interface IArticleDao {

    Optional<Long> add(ArticleAddDto data);
}
