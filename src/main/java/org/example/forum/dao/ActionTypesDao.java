package org.example.forum.dao;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.IActionTypesDao;

import org.example.forum.entities.Action_types;

import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class ActionTypesDao implements IActionTypesDao {

        /**
         * Pobiera pojedynczy rekord typu akcji z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator rekordu typu akcji do pobrania.
         * @return Obiekt typu Action_types reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Action_types get(int id) {
            final String selectSQL = "SELECT * FROM action_types WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setInt(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Action_types actionType = new Action_types();
                        actionType.setId(resultSet.getInt("id"));
                        actionType.setType_name(resultSet.getString("type_name"));
                        actionType.setDescription(resultSet.getString("description"));
                        return actionType;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda dodająca nowy typ akcji do bazy danych.
         *
         * @param actionType Obiekt typu akcji z danymi do dodania.
         * @return Zwraca opcjonalny obiekt Integer, który zawiera identyfikator nowo dodanego typu akcji.
         *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Optional<Integer> add(Action_types actionType) {
            final String insertSQL = "INSERT INTO action_types (type_name, description) VALUES (?,?)";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                insertStatement.setString(1, actionType.getType_name());
                insertStatement.setString(2, actionType.getDescription());

                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        actionType.setId(generatedKeys.getInt(1));
                    }
                }

                return Optional.of(actionType.getId());

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda aktualizująca typ akcji w bazie danych.
         *
         * @param actionType Obiekt typu akcji z nowymi danymi.
         * @return Zwraca {@code true}, jeśli typ akcji został pomyślnie zaktualizowany; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean update(Action_types actionType) {
            final String updateSQL = "UPDATE action_types SET type_name =?, description =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setString(1, actionType.getType_name());
                updateStatement.setString(2, actionType.getDescription());
                updateStatement.setInt(3, actionType.getId());

                int affectedRows = updateStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda usuwająca typ akcji z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator typu akcji do usunięcia.
         * @return Zwraca {@code true}, jeśli typ akcji został pomyślnie usunięty; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean delete(int id) {
            final String deleteSQL = "DELETE FROM action_types WHERE id =?";

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
