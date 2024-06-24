package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Article_reports;
import java.util.List;
import java.util.Optional;

public interface IArticleReportsDao {

    Article_reports get(long id);

    List<Article_reports> getAllNotViewed();

    List<Article_reports> getAll();

    Optional<Long> add(Article_reports articleReport);

    Boolean update(Article_reports articleReport);

    Boolean delete(long id);

}
