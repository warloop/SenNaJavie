package org.example.forum.repositories.Interfaces;

import org.example.forum.dto.Opinions.CommentAddDto;
import org.example.forum.dto.Opinions.CommentEditDto;
import org.example.forum.dto.Opinions.CommentLikesAndDislikesCounterDto;
import org.example.forum.entities.Comments;

import java.util.List;
import java.util.Optional;

public interface ICommentRepository {



    List<Comments> getCommentsByUserId(int userId);

    List<Comments> getCommentsByArticleId(long articleId);
    Optional<Comments> getCommentById(long commentId);

    CommentLikesAndDislikesCounterDto getCommentLikesAndDislikes(long commentId);

    boolean likeComment(long commentId, int user_id);

    boolean dislikeComment(long commentId, int user_id);

    boolean unlikeComment(long commentId, int user_id);

    boolean undislikeComment(long commentId, int user_id);

    boolean addComment(CommentAddDto comment);

    boolean editComment(CommentEditDto comment);

    boolean deleteComment(long commentId);

}
