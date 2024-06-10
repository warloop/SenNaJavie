package org.example.forum.repositories.Interfaces;

import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.Article.ArticleDto;

import java.util.Optional;

public interface IArticleRepository {

     Optional<Long> createArticle(ArticleAddDto newArticle);

     Optional<ArticleDto> findById(long id);

}
