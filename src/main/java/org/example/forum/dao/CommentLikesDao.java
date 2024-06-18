package org.example.forum.dao;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.ICommentLikesDao;

import org.example.forum.entities.Comment_likes;

import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.*;
import java.util.Optional;

public class CommentLikesDao implements ICommentLikesDao {

        /**
         * Pobiera pojedynczy rekord polubienia komentarza z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator rekordu polubienia komentarza do pobrania.
         * @return Obiekt typu Comment_likes reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Comment_likes get(long id) {
            final String selectSQL = "SELECT * FROM comment_likes WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setLong(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Comment_likes commentLike = new Comment_likes();
                        commentLike.setId(resultSet.getLong("id"));
                        commentLike.setComment_id(resultSet.getLong("comment_id"));
                        commentLike.setUser_id(resultSet.getLong("user_adder_id"));
                        commentLike.setAdd_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                        commentLike.set_deleted(resultSet.getBoolean("is_deleted"));
                        commentLike.setDeleted_date(resultSet.getTimestamp("deleted_date") != null ? resultSet.getTimestamp("deleted_date").toLocalDateTime() : null);
                        return commentLike;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda dodająca nowe polubienie komentarza do bazy danych.
         *
         * @param commentLike Obiekt typu Comment_likes z danymi do dodania.
         * @return Zwraca opcjonalny obiekt Long, który zawiera identyfikator nowo dodanego polubienia komentarza.
         *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Optional<Long> add(Comment_likes commentLike) {
            final String insertSQL = "INSERT INTO comment_likes (comment_id, user_adder_id, add_date, is_deleted, deleted_date) VALUES (?,?,?,?,?)";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                insertStatement.setLong(1, commentLike.getComment_id());
                insertStatement.setLong(2, commentLike.getUser_id());
                insertStatement.setTimestamp(3, Timestamp.valueOf(commentLike.getAdd_date()));
                insertStatement.setBoolean(4, commentLike.is_deleted());
                insertStatement.setTimestamp(5, commentLike.getDeleted_date() != null ? Timestamp.valueOf(commentLike.getDeleted_date()) : null);

                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        commentLike.setId(generatedKeys.getLong(1));
                    }
                }

                return Optional.of(commentLike.getId());

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda aktualizująca polubienie komentarza w bazie danych.
         *
         * @param commentLike Obiekt typu Comment_likes z nowymi danymi.
         * @return Zwraca {@code true}, jeśli polubienie komentarza zostało pomyślnie zaktualizowane; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean update(Comment_likes commentLike) {
            final String updateSQL = "UPDATE comment_likes SET comment_id =?, user_adder_id =?, add_date =?, is_deleted =?, deleted_date =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setLong(1, commentLike.getComment_id());
                updateStatement.setLong(2, commentLike.getUser_id());
                updateStatement.setTimestamp(3, Timestamp.valueOf(commentLike.getAdd_date()));
                updateStatement.setBoolean(4, commentLike.is_deleted());
                updateStatement.setTimestamp(5, commentLike.getDeleted_date() != null ? Timestamp.valueOf(commentLike.getDeleted_date()) : null);
                updateStatement.setLong(6, commentLike.getId());

                int affectedRows = updateStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda usuwająca polubienie komentarza z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator polubienia komentarza do usunięcia.
         * @return Zwraca {@code true}, jeśli polubienie komentarza zostało pomyślnie usunięte; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean delete(long id) {
            final String deleteSQL = "DELETE FROM comment_likes WHERE id =?";

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
