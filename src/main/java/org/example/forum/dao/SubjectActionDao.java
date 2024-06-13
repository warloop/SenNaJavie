package org.example.forum.dao;

import org.example.forum.dao.Interfaces.ISubjectActionDao;
import org.example.forum.entities.Subject_actions;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.*;

/**
 * Klasa odpowiedzialna za komunikację z tabelą subject_action umieszczoną w bazie danych.
 * @author Artur Leszczak
 * @version 1.0.0
 */

public class SubjectActionDao implements ISubjectActionDao {

    /**
     * Metoda pobierająca pojedynczy rekord z tabeli 'ubject_actions' w bazie danych na podstawie podanego identyfikatora.
     *
     * @param id Unikalny identyfikator rekordu do pobrania.
     * @return Obiekt typu Subject_actions reprezentujący pobrany rekord. Jeśli rekord nie zostanie znaleziony, metoda zwraca wartość null.
     * @throws DataAccessException Jeśli wystąpi błąd podczas operacji na bazie danych.
     */
    @Override
    public Subject_actions get(long id) {
        final String selectSQL = "SELECT * FROM subject_actions WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setLong(1, id);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    Subject_actions subjectAction = new Subject_actions();
                    subjectAction.setId(resultSet.getLong("id"));
                    subjectAction.setUser_adder_id(resultSet.getInt("user_adder_id"));
                    subjectAction.setAdd_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                    subjectAction.setSubject_id(resultSet.getLong("subject_id"));
                    subjectAction.setAction_type_id(resultSet.getInt("action_type_id"));
                    return subjectAction;
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Metoda wprowadzająca dane o zdarzeniu na obiekcie Tematu do bazy danych.
     * @param action_id Id wskazujace na identyfikator akcji która została wykonana na temacie.
     * @param user_id Id użytkownika który wprowadza zmianę.
     * @param subject_id Id tematu do ktrórego ma być przypisana informacja o zdarzeniu.
     * @return Zwraca true jeżeli dodawanie powiedzie się , w przeciwnym wypadku false.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Boolean add(int action_id, int user_id, long subject_id)
    {
        final String insertSQL = "INSERT INTO subject_action (action_type, user_adder_id, subject_id) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            //POCZĄTEK TRANSAKCJI
            conn.setAutoCommit(false);

            insertStatement.setInt(1, action_id);
            insertStatement.setInt(2, user_id);
            insertStatement.setLong(3, subject_id);

            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się dodać adnotacji o wszczętej akcji wobec tematu!");
            }

            conn.commit();
            return true;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Metoda aktualizująca dane w rekordzie w tabeli 'ubject_actions' w bazie danych.
     *
     * @param subjectAction Obiekt typu Subject_actions, który zawiera nowe dane do zaktualizowania.
     *                      Obiekt musi posiadać ustawione id rekordu, który ma zostać zaktualizowany.
     *
     * @return Zwraca true, jeśli aktualizacja powiedzie się, w przeciwnym wypadku zwraca false.
     * @throws DataAccessException Jeśli wystąpi błąd podczas operacji na bazie danych.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Boolean update(Subject_actions subjectAction) {
        final String updateSQL = "UPDATE subject_actions SET user_adder_id =?, add_date =?, subject_id =?, action_type_id =? WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

            updateStatement.setInt(1, subjectAction.getUser_adder_id());
            updateStatement.setTimestamp(2, Timestamp.valueOf(subjectAction.getAdd_date()));
            updateStatement.setLong(3, subjectAction.getSubject_id());
            updateStatement.setInt(4, subjectAction.getAction_type_id());
            updateStatement.setLong(5, subjectAction.getId());

            int affectedRows = updateStatement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Metoda usuwająca rekord z tabeli 'ubject_actions' w bazie danych na podstawie podanego identyfikatora.
     *
     * @param id Unikalny identyfikator rekordu do usunięcia.
     * @return Zwraca true, jeśli usunięcie powiedzie się, w przeciwnym wypadku zwraca false.
     * @throws DataAccessException Jeśli wystąpi błąd podczas operacji na bazie danych.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Boolean delete(long id) {
        final String deleteSQL = "DELETE FROM subject_actions WHERE id =?";

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
