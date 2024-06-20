package org.example.forum.repositories;

import org.example.forum.dao.Interfaces.ICommentDao;
import org.example.forum.dao.Interfaces.ICommentDislikesDao;
import org.example.forum.dao.Interfaces.ICommentLikesDao;
import org.example.forum.dto.Opinions.CommentAddDto;
import org.example.forum.dto.Opinions.CommentEditDto;
import org.example.forum.dto.Opinions.CommentLikesAndDislikesCounterDto;
import org.example.forum.entities.Comment_dislikes;
import org.example.forum.entities.Comment_likes;
import org.example.forum.entities.Comments;
import org.example.forum.enums.UserCommentStatus;
import org.example.forum.repositories.Interfaces.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Klasa repozytorium służąca do zarządzania komentarzami w forum.
 *
 * @author Artur Leszczak
 * @version 1.0.0
 */
@Repository
public class CommentRepository implements ICommentRepository {

    /**
     * Interfejs obiektu DAO, komentarze;
     */
    private final ICommentDao commentDao;
    /**
     * Interfejs obiektu DAO, komentarze-polubienia
     */
    private final ICommentLikesDao commentLikesDao;
    /**
     * Interfejs obiektu DAO, komentarze-nie-polubienia
     */
    private final ICommentDislikesDao commentDislikesDao;

    /**
     * Konstruktor i wstrzykiwanie zależności.
     */
    @Autowired
    public CommentRepository(ICommentDao commentDao, ICommentLikesDao commentLikesDao, ICommentDislikesDao commentDislikesDao) {
        this.commentDao = commentDao;
        this.commentLikesDao = commentLikesDao;
        this.commentDislikesDao = commentDislikesDao;
    }

    /**
     * Method to retrieve a list of comments associated with a specific article.
     *
     * @param articleId The unique identifier of the article.
     * @return A list of Comments objects associated with the given articleId.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public List<Comments> getCommentsByArticleId(long articleId) {
        return this.commentDao.getCommentsByArticleId(articleId);
    }

    /**
     * Metoda służąca do pobrania komentarza po jego unikalnym identyfikatorze.
     *
     * @param commentId Unikalny identyfikator komentarza.
     * @return Obiekt Optional zawierający komentarz, jeśli został znaleziony, w przeciwnym razie pusty Optional.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Comments> getCommentById(long commentId) {
        return Optional.of(this.commentDao.get(commentId));
    }

    /**
     * Metoda dodająca nowy komentarz do bazy danych.
     *
     * @param comment Obiekt CommentAddDto zawierający niezbędne dane do dodania nowego komentarza.
     * @return Prawda, jeśli komentarz został pomyślnie dodany, fałsz w przeciwnym przypadku.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public boolean addComment(CommentAddDto comment)
    {
        Comments comments = new Comments();

        comments.setArticleId(comment.getArticle_id());
        comments.setUser_adder_id(comment.getUser_adder_id());
        comments.setComment_text(comment.getComment_text());
        comments.setAnswerToComment(false);
        comments.setLikes(0);
        comments.setDislikes(0);

        Optional<Long> idComent =  this.commentDao.add(comments);

        if(idComent.isPresent()) return true;

        return false;
    }

    @Override
    public boolean editComment(CommentEditDto comment)
    {
        //TODO EDYCJA KOMENTARZA
        return false;
    }

    @Override
    public boolean deleteComment(long commentId, int user_id)
    {
        //TODO USUNIECIE KOMENTARZA
        return false;
    }

    /**
     * Metoda pozwalająca użytkownikowi polubić komentarz.
     * Jeśli użytkownik nie wybrał żadnej z opcji (polubienie/nie polubienie)
     * dodaje nowy rekord polubienia do bazy danych.
     * Jeśli użytkownik już polubił lub nie polubił komentarza,
     * nie wykonuje żadnych działań i zwraca false.
     *
     * @param commentId Unikalny identyfikator komentarza.
     * @param user_id Unikalny identyfikator użytkownika.
     * @return Prawda, jeśli użytkownik pomyślnie polubił komentarz, fałsz w przeciwnym przypadku.
     */
    @Override
    public boolean likeComment(long commentId, int user_id){

        if(this.getUserCommentStatus(commentId,user_id).equals(UserCommentStatus.NOTHING)){
            Comment_likes cl = new Comment_likes();
            cl.setComment_id(commentId);
            cl.setUser_id(user_id);
            this.commentLikesDao.add(cl);
            return true;
        }

        return false; //użytkownik już się określił.
    }

    /**
     * Metoda służąca do dodawania nie polubień o konkretnym komentarzu.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public boolean dislikeComment(long commentId, int user_id){
        if(this.getUserCommentStatus(commentId,user_id).equals(UserCommentStatus.NOTHING)){
            Comment_dislikes cd = new Comment_dislikes();
            cd.setComment_id(commentId);
            cd.setUser_id(user_id);
            this.commentDislikesDao.add(cd);
            return true;
        }

        return false; //użytkownik już się określił.
    }

    @Override
    public boolean unlikeComment(long commentId, int user_id)
    {
        //TODO ODLIKOWANIE KOMENTARZA
        return false;
    }

    @Override
    public boolean undislikeComment(long commentId, int user_id)
    {
        //TODO ODDISLIKOWANIE KOMENTARZA
        return false;
    }

    /**
     * Metoda sprawdza stan interakcji użytkownika z określonym komentarzem (polubienie lub nie polubienie lub brak interakcji).
     *
     * @param commentId Unikalny identyfikator komentarza.
     * @param user_id Unikalny identyfikator użytkownika.
     * @return Stan interakcji użytkownika z komentarzem (polubienie, nie polubienie, nic).
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    private UserCommentStatus getUserCommentStatus(long commentId, int user_id){

        if(this.commentLikesDao.isUserLikeComment(user_id, commentId)){
            return UserCommentStatus.LIKE;
        }

        if(this.commentDislikesDao.isUserDislikeComment(user_id, commentId)){
            return UserCommentStatus.DISLIKE;
        }

        return UserCommentStatus.NOTHING;

    }

    /**
     *  Metoda zwraca obiekt DTO zawierający informację o liczbie like oraz dislike konkretnego komentarza.
     * @param commentId
     * @return CommentLikesAndDislikesCounterDto Obiekt DTO reprezentujący informację.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public CommentLikesAndDislikesCounterDto getCommentLikesAndDislikes(long commentId)
    {
        CommentLikesAndDislikesCounterDto commentLikesAndDislikesCounterDto = new CommentLikesAndDislikesCounterDto();
        commentLikesAndDislikesCounterDto.setComent_id(commentId);
        commentLikesAndDislikesCounterDto.setLikes(this.countCommentLikesByCommentId(commentId));
        commentLikesAndDislikesCounterDto.setDislikes(this.countCommentDislikesByCommentId(commentId));

        return commentLikesAndDislikesCounterDto;

    }

    /**
     * Metoda prywatna zlicza liczbę polubień konkretnego komentarza,
     * NIE ZLICZA REKORDÓW Z FLAGĄ is_deleted = true
     *
     * @param commentId Identyfikator konkretnego komentarza.
     * @return integer Zwraca typ liczbowy całkowity.
     * @author Artur Leszczak
     * @version 1.0.0
     */

    private int countCommentLikesByCommentId(long commentId) {
        return this.commentLikesDao.countCommentLikesByCommentId(commentId);
    }

    /**
     * Metoda prywatna zlicza liczbę nie polubień konkretnego komentarza,
     * NIE ZLICZA REKORDÓW Z FLAGĄ is_deleted = true
     *
     * @param commentId Identyfikator konkretnego komentarza.
     * @return integer Zwraca typ liczbowy całkowity.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    private int countCommentDislikesByCommentId(long commentId) {
        return this.commentDislikesDao.countCommentDislikesByCommentId(commentId);
    }


}
