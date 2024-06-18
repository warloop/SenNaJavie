package org.example.forum.dao;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.ICommentReportDao;

import org.example.forum.entities.Comments_reports;

import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.*;
import java.util.Optional;

public class CommentReportDao implements ICommentReportDao {

        /**
         * Pobiera pojedynczy rekord zgłoszenia komentarza z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator rekordu zgłoszenia komentarza do pobrania.
         * @return Obiekt typu Comments_reports reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Comments_reports get(long id) {
            final String selectSQL = "SELECT * FROM comments_reports WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setLong(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Comments_reports commentsReport = new Comments_reports();
                        commentsReport.setId(resultSet.getLong("id"));
                        commentsReport.setComment_id(resultSet.getLong("comment_id"));
                        commentsReport.setReport_type(resultSet.getInt("report_type"));
                        commentsReport.setUser_reporter_id(resultSet.getInt("user_reporter_id"));
                        commentsReport.setReport_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                        return commentsReport;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda dodająca nowe zgłoszenie komentarza do bazy danych.
         *
         * @param commentsReport Obiekt typu Comments_reports z danymi do dodania.
         * @return Zwraca opcjonalny obiekt Long, który zawiera identyfikator nowo dodanego zgłoszenia komentarza.
         *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Optional<Long> add(Comments_reports commentsReport) {
            final String insertSQL = "INSERT INTO comments_reports (comment_id, report_type, user_reporter_id, add_date) VALUES (?,?,?,?)";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                insertStatement.setLong(1, commentsReport.getComment_id());
                insertStatement.setInt(2, commentsReport.getReport_type());
                insertStatement.setInt(3, commentsReport.getUser_reporter_id());
                insertStatement.setTimestamp(4, Timestamp.valueOf(commentsReport.getReport_date()));

                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        commentsReport.setId(generatedKeys.getLong(1));
                    }
                }

                return Optional.of(commentsReport.getId());

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda aktualizująca zgłoszenie komentarza w bazie danych.
         *
         * @param commentsReport Obiekt typu Comments_reports z nowymi danymi.
         * @return Zwraca {@code true}, jeśli zgłoszenie komentarza zostało pomyślnie zaktualizowane; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean update(Comments_reports commentsReport) {
            final String updateSQL = "UPDATE comments_reports SET comment_id =?, report_type =?, user_reporter_id =?, add_date =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setLong(1, commentsReport.getComment_id());
                updateStatement.setInt(2, commentsReport.getReport_type());
                updateStatement.setInt(3, commentsReport.getUser_reporter_id());
                updateStatement.setTimestamp(4, Timestamp.valueOf(commentsReport.getReport_date()));
                updateStatement.setLong(5, commentsReport.getId());

                int affectedRows = updateStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda usuwająca zgłoszenie komentarza z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator zgłoszenia komentarza do usunięcia.
         * @return Zwraca {@code true}, jeśli zgłoszenie komentarza zostało pomyślnie usunięte; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean delete(long id) {
            final String deleteSQL = "DELETE FROM comments_reports WHERE id =?";

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
