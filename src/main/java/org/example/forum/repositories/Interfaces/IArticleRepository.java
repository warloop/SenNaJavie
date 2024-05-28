package org.example.forum.repositories.Interfaces;

import org.example.forum.dto.Article.ArticleAddDto;

import java.util.Optional;

public interface IArticleRepository {

     Optional<Long> createArticle(ArticleAddDto newArticle);

}
