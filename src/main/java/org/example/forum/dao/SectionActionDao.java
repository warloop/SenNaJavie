package org.example.forum.dao;

import org.example.forum.dao.Interfaces.ISectionActionDao;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Klasa odpowiedzialna za komunikację z tabelą section_action umieszczoną w bazie danych.
 * @author Artur Leszczak
 * @version 1.0.0
 */

public class SectionActionDao implements ISectionActionDao {

    /**
     * Metoda wprowadzająca dane o zdarzeniu na obiekcie Sekcji do bazy danych.
     * @param action_id Id wskazujace na identyfikator akcji która została wykonana na sekcji.
     * @param user_id Id użytkownika który wprowadza zmianę.
     * @param section_id Id sekcji do ktrórego ma być przypisana informacja o zdarzeniu.
     * @return Zwraca true jeżeli dodawanie powiedzie się , w przeciwnym wypadku false.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Boolean add(int action_id, int user_id, long section_id)
    {
        final String insertSQL = "INSERT INTO section_action (action_type, user_adder_id, section_id) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            //POCZĄTEK TRANSAKCJI
            conn.setAutoCommit(false);

            insertStatement.setInt(1, action_id);
            insertStatement.setInt(2, user_id);
            insertStatement.setLong(3, section_id);

            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się dodać adnotacji o wszczętej akcji wobec sekcji!");
            }

            conn.commit();
            return true;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
}
