package org.example.forum.services.interfaces;

import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.Article.ArticleReportDto;
import org.example.forum.dto.System.InformationReturned;
import org.example.forum.entities.Articles;
import java.util.List;


public interface IArticleService {

    InformationReturned addArticle(ArticleAddDto articleAdd);

    InformationReturned reportArticle(ArticleReportDto article);

    List<Articles> findByUserId(int userId);

    List<Articles> getArticlesBySubjectId(Long subjectId);

    Articles getArticleById(Long articleId);

}
