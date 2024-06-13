package org.example.forum.dao;

import org.example.forum.dao.Interfaces.ICommentDislikesDao;

import org.example.forum.entities.Comment_dislikes;

import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.*;
import java.util.Optional;

public class CommentDislikesDao implements ICommentDislikesDao {

        /**
         * Pobiera pojedynczy rekord niechęci do komentarza z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator rekordu niechęci do komentarza do pobrania.
         * @return Obiekt typu Comment_dislikes reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Comment_dislikes get(long id) {
            final String selectSQL = "SELECT * FROM comment_dislikes WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setLong(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Comment_dislikes commentDislike = new Comment_dislikes();
                        commentDislike.setId(resultSet.getLong("id"));
                        commentDislike.setComment_id(resultSet.getLong("comment_id"));
                        commentDislike.setUser_id(resultSet.getLong("user_adder_id"));
                        commentDislike.setAdd_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                        commentDislike.set_deleted(resultSet.getBoolean("is_deleted"));
                        commentDislike.setDeleted_date(resultSet.getTimestamp("deleted_date") != null ? resultSet.getTimestamp("deleted_date").toLocalDateTime() : null);
                        return commentDislike;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda dodająca nową niechęć do komentarza do bazy danych.
         *
         * @param commentDislike Obiekt typu Comment_dislikes z danymi do dodania.
         * @return Zwraca opcjonalny obiekt Long, który zawiera identyfikator nowo dodanej niechęci do komentarza.
         *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Optional<Long> add(Comment_dislikes commentDislike) {
            final String insertSQL = "INSERT INTO comment_dislikes (comment_id, user_adder_id, add_date, is_deleted, deleted_date) VALUES (?,?,?,?,?)";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                insertStatement.setLong(1, commentDislike.getComment_id());
                insertStatement.setLong(2, commentDislike.getUser_id());
                insertStatement.setTimestamp(3, Timestamp.valueOf(commentDislike.getAdd_date()));
                insertStatement.setBoolean(4, commentDislike.is_deleted());
                insertStatement.setTimestamp(5, commentDislike.getDeleted_date() != null ? Timestamp.valueOf(commentDislike.getDeleted_date()) : null);

                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        commentDislike.setId(generatedKeys.getLong(1));
                    }
                }

                return Optional.of(commentDislike.getId());

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda aktualizująca niechęć do komentarza w bazie danych.
         *
         * @param commentDislike Obiekt typu Comment_dislikes z nowymi danymi.
         * @return Zwraca {@code true}, jeśli niechęć do komentarza została pomyślnie zaktualizowana; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Boolean update(Comment_dislikes commentDislike) {
            final String updateSQL = "UPDATE comment_dislikes SET comment_id =?, user_adder_id =?, add_date =?, is_deleted =?, deleted_date =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setLong(1, commentDislike.getComment_id());
                updateStatement.setLong(2, commentDislike.getUser_id());
                updateStatement.setTimestamp(3, Timestamp.valueOf(commentDislike.getAdd_date()));
                updateStatement.setBoolean(4, commentDislike.is_deleted());
                updateStatement.setTimestamp(5, commentDislike.getDeleted_date() != null ? Timestamp.valueOf(commentDislike.getDeleted_date()) : null);
                updateStatement.setLong(6, commentDislike.getId());

                int affectedRows = updateStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda usuwająca niechęć do komentarza z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator niechęci do komentarza do usunięcia.
         * @return Zwraca {@code true}, jeśli niechęć do komentarza została pomyślnie usunięta; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Boolean delete(long id) {
            final String deleteSQL = "DELETE FROM comment_dislikes WHERE id =?";

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
