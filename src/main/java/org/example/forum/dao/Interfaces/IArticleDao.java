package org.example.forum.dao.Interfaces;

import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.entities.Articles;

import java.util.List;
import java.util.Optional;

public interface IArticleDao {

    Optional<Long> add(ArticleAddDto data);

    List<Articles> findByUserAdderId(int userAdderId);

}
