package org.example.forum.repositories.Interfaces;

import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.Article.ArticleDto;
import org.example.forum.dto.Article.ArticleEditDto;
import org.example.forum.entities.Articles;
import java.util.List;
import java.util.Optional;

public interface IArticleRepository {

     Optional<Long> createArticle(ArticleAddDto newArticle);

     Optional<Articles> findById(long id);

     List<Articles> findByUserAdderId(int userAdderId);

     List<Articles> findBySubjectId(long subjectId);

     Articles getArticleById(Long articleId);

     boolean editArticleText(ArticleEditDto articleEditDto);

     boolean deleteArticle(long articleId, int user_id, boolean by_owner);

}
