package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Comment_dislikes;

import java.util.Optional;

public interface ICommentDislikesDao {

    Comment_dislikes get(long id);

    boolean isUserDislikeComment(int user_id, long comment_id);

    int countCommentDislikesByCommentId(long commentId);

    Optional<Long> add(Comment_dislikes commentDislike);

    Boolean update(Comment_dislikes commentDislike);

    Boolean delete(long id);

}
