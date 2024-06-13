package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Comment_likes;

import java.util.Optional;

public interface ICommentLikesDao {

    Comment_likes get(long id);

    Optional<Long> add(Comment_likes commentLike);

    Boolean update( Comment_likes commentLike);

    Boolean delete(long id);

}
