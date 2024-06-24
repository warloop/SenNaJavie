package org.example.forum.dao.Interfaces;

import jakarta.transaction.Transactional;
import org.example.forum.entities.Comments;

import java.util.List;
import java.util.Optional;

public interface ICommentDao {

    List<Comments> getCommentsByArticleId(long articleId);

    @Transactional
    List<Comments> getCommentsByUserId(int userId);

    Comments get(long id);

    Optional<Long> add(Comments comment);

    Boolean update(Comments  comment);

    Boolean delete(long id);

}
