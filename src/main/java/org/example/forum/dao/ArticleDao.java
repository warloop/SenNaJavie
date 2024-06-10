package org.example.forum.dao;

import org.example.forum.dao.Interfaces.IArticleDao;
import org.example.forum.dto.Article.ArticleAddDto;
import org.example.forum.entities.Articles;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Klasa odpowiedzialna za komunikację z tabelą articles umieszczoną w bazie danych.
 * @author Artur Leszczak
 * @version 1.0.0
 */

public class ArticleDao implements IArticleDao {

    /**
     * Metoda wproawdza dane nowego artykułu do bazy danych.
     *
     * @param data Obeikt typu Dto zawiera podstawowe informacje ptrzebne w celu utworzenia nowego artykułu.
     * @return Optional<Long> zwraca Long w przypadku poprawnego utworzenia, w przypadku błedu empty.
     * @throws DataAccessException Jeśli wystąpi błąd podczas komunikacji z bazą danych.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Long> add(ArticleAddDto data) {
        final String insertSQL = "INSERT INTO articles (subject_id, user_adder_id, article_title, is_visible) VALUES (?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Początek transakcji
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

    /**
     * Retrieves an article from the database based on its ID.
     *
     * @param articleId The unique identifier of the article to retrieve.
     * @return An Optional containing the retrieved Article object if found, otherwise an empty Optional.
     * @throws DataAccessException If an error occurs while accessing the database.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Articles> getById(long articleId) {
        final String selectSQL = "SELECT * FROM articles WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setLong(1, articleId);

            try (ResultSet rs = selectStatement.executeQuery()) {

                if (rs.next()) {
                    // Create an Article object from the ResultSet and return it wrapped in an Optional
                    Articles article = new Articles(
                            rs.getLong("id"),
                            rs.getInt("user_adder_id"),
                            rs.getLong("subject_id"),
                            rs.getString("article_title"),
                            rs.getBoolean("is_visible"),
                            rs.getBoolean("is_banned"),
                            rs.getBoolean("is_deleted")
                    );
                    return Optional.of(article);
                } else {
                    // If no article is found with the given ID, return an empty Optional
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            // Handle any SQL exceptions
            throw new DataAccessException(ex);
        }
    }

    /**
     * Retrieves a list of articles from the database based on the provided start and limit parameters.
     *
     * @param start The starting index for retrieving articles. If start is less than 0, it will be set to 0.
     * @param limit The maximum number of articles to retrieve. If limit is less than 25, it will be set to 25. If limit is greater than 150, it will be set to 150.
     * @return A List of Articles retrieved from the database based on the provided start and limit parameters.
     * @throws DataAccessException If an error occurs while accessing the database.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public List<Articles> getAll(int start, int limit) {
        if(start < 0) start = 0;
        if(limit < 25) limit = 25;
        if(limit > 150) limit = 150;

        List<Articles> articles = new ArrayList<>();

        // Loop through the database starting from the provided start index and retrieve the specified limit of articles
        for(int i = start; i < start + limit; i++) {
            // If no article is found with the current ID, break the loop
            if(!this.getById(i).isPresent()) break;

            // Add the retrieved article to the list
            articles.add(this.getById(i).get());
        }
        // Return the list of retrieved articles
        return articles;
    }

    @Override
    public Optional<Articles> update(long articleId, Articles data) {
        //TODO Implement the logic to update an article in the database based on its ID and the provided data
        // Return an Optional containing the updated Article object if successful, otherwise return an empty Optional
        return Optional.empty();
    }

    @Override
    public void delete(long articleId) {
        //TODO Implement the logic to delete an article from the database based on its ID
    }
}
