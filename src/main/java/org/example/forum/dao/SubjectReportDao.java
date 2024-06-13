package org.example.forum.dao;

import org.example.forum.dao.Interfaces.ISubjectReportDao;

import org.example.forum.entities.Subjects_reports;

import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class SubjectReportDao implements ISubjectReportDao {

        /**
         * Pobiera pojedynczy rekord zgłoszenia przedmiotu z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator rekordu zgłoszenia przedmiotu do pobrania.
         * @return Obiekt typu Subjects_reports reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Subjects_reports get(int id) {
            final String selectSQL = "SELECT * FROM subjects_reports WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setInt(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Subjects_reports subjectsReport = new Subjects_reports();
                        subjectsReport.setId(resultSet.getInt("id"));
                        subjectsReport.setSubject_id(resultSet.getLong("subject_id"));
                        subjectsReport.setReport_type(resultSet.getInt("report_type"));
                        subjectsReport.setUser_reporter_id(resultSet.getInt("user_reporter_id"));
                        subjectsReport.setReport_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                        return subjectsReport;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda dodająca nowe zgłoszenie przedmiotu do bazy danych.
         *
         * @param subjectsReport Obiekt typu Subjects_reports z danymi do dodania.
         * @return Zwraca opcjonalny obiekt Integer, który zawiera identyfikator nowo dodanego zgłoszenia przedmiotu.
         *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Optional<Integer> add(Subjects_reports subjectsReport) {
            final String insertSQL = "INSERT INTO subjects_reports (subject_id, report_type, user_reporter_id, add_date) VALUES (?,?,?,?)";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                insertStatement.setLong(1, subjectsReport.getSubject_id());
                insertStatement.setInt(2, subjectsReport.getReport_type());
                insertStatement.setInt(3, subjectsReport.getUser_reporter_id());
                insertStatement.setTimestamp(4, Timestamp.valueOf(subjectsReport.getReport_date()));

                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        subjectsReport.setId(generatedKeys.getInt(1));
                    }
                }

                return Optional.of(subjectsReport.getId());

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda aktualizująca zgłoszenie przedmiotu w bazie danych.
         *
         * @param subjectsReport Obiekt typu Subjects_reports z nowymi danymi.
         * @return Zwraca {@code true}, jeśli zgłoszenie przedmiotu zostało pomyślnie zaktualizowane; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Boolean update(Subjects_reports subjectsReport) {
            final String updateSQL = "UPDATE subjects_reports SET subject_id =?, report_type =?, user_reporter_id =?, add_date =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setLong(1, subjectsReport.getSubject_id());
                updateStatement.setInt(2, subjectsReport.getReport_type());
                updateStatement.setInt(3, subjectsReport.getUser_reporter_id());
                updateStatement.setTimestamp(4, Timestamp.valueOf(subjectsReport.getReport_date()));
                updateStatement.setInt(5, subjectsReport.getId());

                int affectedRows = updateStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda usuwająca zgłoszenie przedmiotu z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator zgłoszenia przedmiotu do usunięcia.
         * @return Zwraca {@code true}, jeśli zgłoszenie przedmiotu zostało pomyślnie usunięte; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Boolean delete(int id) {
            final String deleteSQL = "DELETE FROM subjects_reports WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL)) {

                deleteStatement.setInt(1, id);

                int affectedRows = deleteStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

}
