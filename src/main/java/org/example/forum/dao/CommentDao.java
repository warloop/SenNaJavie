package org.example.forum.dao;

import org.example.forum.dao.Interfaces.ICommentDao;
import org.example.forum.entities.Comments;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.*;
import java.util.Optional;

public class CommentDao implements ICommentDao {

        /**
         * Pobiera pojedynczy rekord komentarza z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator rekordu komentarza do pobrania.
         * @return Obiekt typu Comments reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Comments get(long id) {
            final String selectSQL = "SELECT * FROM comments WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setLong(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Comments comment = new Comments();
                        comment.setId(resultSet.getLong("id"));
                        comment.setArticleId(resultSet.getLong("article_id"));
                        comment.setAnswerToComment(resultSet.getBoolean("is_answer_to_comment"));
                        comment.setCommentId(resultSet.getLong("comment_id"));
                        comment.setUser_adder_id(resultSet.getString("user_adder_id"));
                        comment.setAdd_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                        comment.setComment_text(resultSet.getString("comment_text"));
                        comment.setComment_mark(resultSet.getShort("comment_mark"));
                        comment.setLikes(resultSet.getInt("likes"));
                        comment.setDislikes(resultSet.getInt("dislikes"));
                        comment.setBanned(resultSet.getBoolean("is_banned"));
                        comment.setDeleted(resultSet.getBoolean("is_deleted"));
                        comment.setDeleted_date(resultSet.getTimestamp("deleted_date") != null ? resultSet.getTimestamp("deleted_date").toLocalDateTime() : null);
                        return comment;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda dodająca nowy komentarz do bazy danych.
         *
         * @param comment Obiekt typu Comments z danymi do dodania.
         * @return Zwraca opcjonalny obiekt Long, który zawiera identyfikator nowo dodanego komentarza.
         *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Optional<Long> add(Comments comment) {
            final String insertSQL = "INSERT INTO comments (article_id, is_answer_to_comment, comment_id, user_adder_id, add_date, comment_text, comment_mark, likes, dislikes, is_banned, is_deleted, deleted_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                insertStatement.setLong(1, comment.getArticleId());
                insertStatement.setBoolean(2, comment.isAnswerToComment());
                insertStatement.setLong(3, comment.getCommentId());
                insertStatement.setString(4, comment.getUser_adder_id());
                insertStatement.setTimestamp(5, Timestamp.valueOf(comment.getAdd_date()));
                insertStatement.setString(6, comment.getComment_text());
                insertStatement.setShort(7, comment.getComment_mark());
                insertStatement.setInt(8, comment.getLikes());
                insertStatement.setInt(9, comment.getDislikes());
                insertStatement.setBoolean(10, comment.isBanned());
                insertStatement.setBoolean(11, comment.isDeleted());
                insertStatement.setTimestamp(12, comment.getDeleted_date() != null ? Timestamp.valueOf(comment.getDeleted_date()) : null);

                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        comment.setId(generatedKeys.getLong(1));
                    }
                }

                return Optional.of(comment.getId());

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda aktualizująca komentarz w bazie danych.
         *
         * @param comment Obiekt typu Comments z nowymi danymi.
         * @return Zwraca {@code true}, jeśli komentarz został pomyślnie zaktualizowany; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Boolean update(Comments comment) {
            final String updateSQL = "UPDATE comments SET article_id =?, is_answer_to_comment =?, comment_id =?, user_adder_id =?, add_date =?, comment_text =?, comment_mark =?, likes =?, dislikes =?, is_banned =?, is_deleted =?, deleted_date =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setLong(1, comment.getArticleId());
                updateStatement.setBoolean(2, comment.isAnswerToComment());
                updateStatement.setLong(3, comment.getCommentId());
                updateStatement.setString(4, comment.getUser_adder_id());
                updateStatement.setTimestamp(5, Timestamp.valueOf(comment.getAdd_date()));
                updateStatement.setString(6, comment.getComment_text());
                updateStatement.setShort(7, comment.getComment_mark());
                updateStatement.setInt(8, comment.getLikes());
                updateStatement.setInt(9, comment.getDislikes());
                updateStatement.setBoolean(10, comment.isBanned());
                updateStatement.setBoolean(11, comment.isDeleted());
                updateStatement.setTimestamp(12, comment.getDeleted_date() != null ? Timestamp.valueOf(comment.getDeleted_date()) : null);
                updateStatement.setLong(13, comment.getId());

                int affectedRows = updateStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda usuwająca komentarz z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator komentarza do usunięcia.
         * @return Zwraca {@code true}, jeśli komentarz został pomyślnie usunięty; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Boolean delete(long id) {
            final String deleteSQL = "DELETE FROM comments WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL)) {

                deleteStatement.setLong(1, id);

                int affectedRows = deleteStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

}
