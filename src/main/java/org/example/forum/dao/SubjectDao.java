package org.example.forum.dao;

import org.example.forum.dao.Interfaces.ISubjectDao;

import org.example.forum.entities.Subjects;

import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.*;
import java.util.Optional;

public class SubjectDao implements ISubjectDao {

        /**
         * Pobiera pojedynczy rekord przedmiotu z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator rekordu przedmiotu do pobrania.
         * @return Obiekt typu Subjects reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Subjects get(long id) {
            final String selectSQL = "SELECT * FROM subjects WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setLong(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Subjects subject = new Subjects();
                        subject.setId(resultSet.getLong("id"));
                        subject.setUser_adder_id(resultSet.getInt("user_adder_id"));
                        subject.setSubject_text(resultSet.getString("subject_text"));
                        subject.set_banned(resultSet.getBoolean("is_banned"));
                        subject.set_deleted(resultSet.getBoolean("is_deleted"));
                        return subject;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda dodająca nowy przedmiot do bazy danych.
         *
         * @param subject Obiekt typu Subjects z danymi do dodania.
         * @return Zwraca opcjonalny obiekt Long, który zawiera identyfikator nowo dodanego przedmiotu.
         *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Optional<Long> add(Subjects subject) {
            final String insertSQL = "INSERT INTO subjects (user_adder_id, subject_text, is_banned, is_deleted) VALUES (?,?,?,?)";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                insertStatement.setInt(1, subject.getUser_adder_id());
                insertStatement.setString(2, subject.getSubject_text());
                insertStatement.setBoolean(3, subject.is_banned());
                insertStatement.setBoolean(4, subject.is_deleted());

                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        subject.setId(generatedKeys.getLong(1));
                    }
                }

                return Optional.of(subject.getId());

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda aktualizująca przedmiot w bazie danych.
         *
         * @param subject Obiekt typu Subjects z nowymi danymi.
         * @return Zwraca {@code true}, jeśli przedmiot został pomyślnie zaktualizowany; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Boolean update(Subjects subject) {
            final String updateSQL = "UPDATE subjects SET user_adder_id =?, subject_text =?, is_banned =?, is_deleted =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setInt(1, subject.getUser_adder_id());
                updateStatement.setString(2, subject.getSubject_text());
                updateStatement.setBoolean(3, subject.is_banned());
                updateStatement.setBoolean(4, subject.is_deleted());
                updateStatement.setLong(5, subject.getId());

                int affectedRows = updateStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda usuwająca przedmiot z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator przedmiotu do usunięcia.
         * @return Zwraca {@code true}, jeśli przedmiot został pomyślnie usunięty; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Boolean delete(long id) {
            final String deleteSQL = "DELETE FROM subjects WHERE id =?";

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
