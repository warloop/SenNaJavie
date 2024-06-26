package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Comments;

import java.util.List;
import java.util.Optional;

public interface ICommentDao {

    List<Comments> getCommentsByArticleId(long articleId);

    Comments get(long id);

    Optional<Long> add(Comments comment);

    Boolean update(Comments  comment);

    Boolean delete(long id);

}
