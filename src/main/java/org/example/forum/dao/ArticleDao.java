package org.example.forum.dao;

import org.example.forum.dao.Interfaces.IArticleDao;
import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Klasa odpowiedzialna za komunikację z tabelą articles umieszczoną w bazie danych.
 * @author Artur Leszczak
 * @version 1.0.0
 */

public class ArticleDao implements IArticleDao {

    /**
     * Metoda wproawdza dane nowego artykułu do bazy danych.
     * @param data Obeikt typu Dto zawiera podstawowe informacje ptrzebne w celu utworzenia nowego artykułu.
     * @return Optional<Long> zwraca Long w przypadku poprawnego utworzenia, w przypadku błedu empty.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Long> add(ArticleAddDto data) {
        final String insertSQL = "INSERT INTO articles (subject_id, user_adder_id, article_title, is_visible) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            //POCZĄTEK TRANSAKCJI
            conn.setAutoCommit(false);

            insertStatement.setLong(1, data.getSubjectId());
            insertStatement.setInt(2, data.getUserAdderId());
            insertStatement.setString(3, data.getTitle());
            insertStatement.setBoolean(4, data.is_visible());

            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się dodać artykułu!");
            }

            try (ResultSet rs = insertStatement.getGeneratedKeys()) {

                if (rs.next()) {
                    conn.commit();
                    long addedArticleId = rs.getLong(1);
                    return Optional.of(addedArticleId);
                } else {
                    conn.rollback();
                    return Optional.empty();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new DataAccessException(e);

            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);

        }

    }
}
