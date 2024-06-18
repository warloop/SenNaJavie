package org.example.forum.dao;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.IArticleActionDao;
import org.example.forum.entities.Article_actions;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.*;

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
    @Transactional
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

    /**
     * Pobiera pojedynczy rekord akcji artykułu z bazy danych na podstawie podanego identyfikatora.
     *
     * @param id Unikalny identyfikator rekordu akcji artykułu do pobrania.
     * @return Rekord akcji artykułu z podanym identyfikatorem lub null, jeśli nie znaleziono rekordu.
     * @throws DataAccessException Wyjątek zgłaszany w przypadku błędu podczas komunikacji z bazą danych.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Article_actions get(long id) {
        final String selectSQL = "SELECT * FROM article_actions WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setLong(1, id);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    Article_actions articleAction = new Article_actions();
                    articleAction.setId(resultSet.getLong("id"));
                    articleAction.setUser_adder_id(resultSet.getInt("user_adder_id"));
                    articleAction.setAdd_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                    articleAction.setArticle_id(resultSet.getLong("article_id"));
                    articleAction.setAction_type_id(resultSet.getInt("action_type_id"));
                    return articleAction;
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Metoda aktualizująca rekord w bazie danych odpowiadający podanemu obiektom artykułu.
     *
     * @param articleAction Obiekt artykułu z nowymi wartościami, które mają zostać zaktualizowane w bazie danych.
     * @return Zwraca true, jeśli operacja aktualizacji zakończy się powodzeniem, w przeciwnym razie zwraca false.
     * @throws DataAccessException Wyjątek zgłaszany w przypadku błędu podczas komunikacji z bazą danych.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Boolean update(Article_actions articleAction) {
        final String updateSQL = "UPDATE article_actions SET user_adder_id =?, add_date =?, article_id =?, action_type_id =? WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

            updateStatement.setInt(1, articleAction.getUser_adder_id());
            updateStatement.setTimestamp(2, Timestamp.valueOf(articleAction.getAdd_date()));
            updateStatement.setLong(3, articleAction.getArticle_id());
            updateStatement.setInt(4, articleAction.getAction_type_id());
            updateStatement.setLong(5, articleAction.getId());

            int affectedRows = updateStatement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Metoda usuwająca rekord z bazy danych o podanym id.
     *
     * @param id Identyfikator rekordu do usunięcia.
     * @return Zwraca true jeśli operacja usunięcia zakończy się powodzeniem, w przeciwnym wypadku zwraca false.
     * @throws DataAccessException Wyjątek zgłaszany w przypadku błędu podczas komunikacji z bazą danych.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Boolean delete(long id) {
        final String deleteSQL = "DELETE FROM article_actions WHERE id =?";

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
