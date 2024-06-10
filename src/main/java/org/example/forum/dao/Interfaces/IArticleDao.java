package org.example.forum.dao.Interfaces;

import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.entities.Articles;

import java.util.List;
import java.util.Optional;

public interface IArticleDao {

    Optional<Long> add(ArticleAddDto data);

    Optional<Articles> getById(long articleId);

    List<Articles> getAll(int start, int limit);

    Optional<Articles> update(long articleId, Articles data);

    void delete(long articleId);
}
