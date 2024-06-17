package org.example.forum.dao.Interfaces;

import org.example.forum.entities.Comment_dislikes;

import java.util.Optional;

public interface ICommentDislikesDao {

    Comment_dislikes get(long id);

    Optional<Long> add(Comment_dislikes commentDislike);

    Boolean update(Comment_dislikes commentDislike);

    Boolean delete(long id);

}
