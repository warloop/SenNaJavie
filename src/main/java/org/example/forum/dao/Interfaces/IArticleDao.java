package org.example.forum.dao.Interfaces;

import jakarta.transaction.Transactional;
import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.entities.Articles;

import java.util.List;
import java.util.Optional;

public interface IArticleDao {

    Optional<Long> add(ArticleAddDto data);

    Articles getById(long articleId);

    List<Articles> getAll(int start, int limit);

    List<Articles> findByUserAdderId(int userAdderId);

    Boolean update(Articles articles);

    boolean delete(long articleId);

    List<Articles> findBySubjectId(long subjectId);

}
