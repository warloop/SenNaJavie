package org.example.forum.dao;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.ISubjectReportDao;
import org.example.forum.entities.Subjects_reports;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
        public Subjects_reports get(long id) {
            final String selectSQL = "SELECT * FROM subjects_reports WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setLong(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Subjects_reports subjectsReport = new Subjects_reports();
                        subjectsReport.setId(resultSet.getInt("id"));
                        subjectsReport.setSubject_id(resultSet.getLong("subject_id"));
                        subjectsReport.setReport_type(resultSet.getInt("report_type"));
                        subjectsReport.setUser_reporter_id(resultSet.getInt("user_reporter_id"));
                        subjectsReport.setReport_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                        subjectsReport.set_viewed(resultSet.getBoolean("is_viewed"));
                        return subjectsReport;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        @Override
        public List<Subjects_reports> getAllNotViewed(){
            final String selectSQL = "SELECT * FROM subjects_reports WHERE is_viewed == false";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    List<Subjects_reports> subjects = new ArrayList<Subjects_reports>();
                    while(resultSet.next()) {
                        Subjects_reports subjectsReport = new Subjects_reports();
                        subjectsReport.setId(resultSet.getInt("id"));
                        subjectsReport.setSubject_id(resultSet.getLong("subject_id"));
                        subjectsReport.setReport_type(resultSet.getInt("report_type"));
                        subjectsReport.setUser_reporter_id(resultSet.getInt("user_reporter_id"));
                        subjectsReport.setReport_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                        subjectsReport.set_viewed(resultSet.getBoolean("is_viewed"));

                        subjects.add(subjectsReport);
                    }
                    return subjects;
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }


        @Override
        public List<Subjects_reports> getAll(){
            final String selectSQL = "SELECT * FROM subjects_reports";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    List<Subjects_reports> subjects = new ArrayList<Subjects_reports>();
                    while(resultSet.next()) {
                        Subjects_reports subjectsReport = new Subjects_reports();
                        subjectsReport.setId(resultSet.getInt("id"));
                        subjectsReport.setSubject_id(resultSet.getLong("subject_id"));
                        subjectsReport.setReport_type(resultSet.getInt("report_type"));
                        subjectsReport.setUser_reporter_id(resultSet.getInt("user_reporter_id"));
                        subjectsReport.setReport_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                        subjectsReport.set_viewed(resultSet.getBoolean("is_viewed"));

                        subjects.add(subjectsReport);
                    }
                    return subjects;
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
        @Transactional
        public Optional<Long> add(Subjects_reports subjectsReport) {
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
        @Transactional
        public Boolean update(Subjects_reports subjectsReport) {
            final String updateSQL = "UPDATE subjects_reports SET subject_id =?, report_type =?, user_reporter_id =?, add_date =?, is_viewed =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setLong(1, subjectsReport.getSubject_id());
                updateStatement.setInt(2, subjectsReport.getReport_type());
                updateStatement.setInt(3, subjectsReport.getUser_reporter_id());
                updateStatement.setTimestamp(4, Timestamp.valueOf(subjectsReport.getReport_date()));
                updateStatement.setBoolean(5, subjectsReport.is_viewed());
                updateStatement.setLong(6, subjectsReport.getId());

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
        @Transactional
        public Boolean delete(long id) {
            final String deleteSQL = "DELETE FROM subjects_reports WHERE id =?";

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
