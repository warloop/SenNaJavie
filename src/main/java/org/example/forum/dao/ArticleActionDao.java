package org.example.forum.dao;

import org.example.forum.dao.Interfaces.IArticleActionDao;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Klasa odpowiedzialna za komunikację z tabelą article_action umieszczoną w bazie danych.
 * @author Artur Leszczak
 * @version 1.0.0
 */

public class ArticleActionDao implements IArticleActionDao {

    /**
     * Metoda wprowadzająca dane o zdarzeniu na obiekcie artykułu do bazy danych.
     * @param action_id Id wskazujace na identyfikator akcji która została wykonana na artykule.
     * @param user_id Id użytkownika który wprowadza zmianę.
     * @param article_id Id artykułu do ktrórego ma być przypisana informacja o zdarzeniu.
     * @return Zwraca true jeżeli dodawanie powiedzie się , w przeciwnym wypadku false.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Boolean add(int action_id, int user_id, long article_id)
    {
        final String insertSQL = "INSERT INTO article_action (action_type, user_adder_id, article_id) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            //POCZĄTEK TRANSAKCJI
            conn.setAutoCommit(false);

            insertStatement.setInt(1, action_id);
            insertStatement.setInt(2, user_id);
            insertStatement.setLong(3, article_id);

            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się dodać adnotacji o wszczętej akcji wobec artykułu!");
            }

            conn.commit();
            return true;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
}
