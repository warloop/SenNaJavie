package org.example.forum.services.interfaces;

import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.dto.Subject.SubjectAddDto;
import org.example.forum.dto.System.InformationReturned;

public interface IArticleService {

    InformationReturned addArticle(ArticleAddDto articleAdd);
}
