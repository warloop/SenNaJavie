package org.example.forum.dao;

import jakarta.transaction.Transactional;
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
    @Transactional
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
     * @param id The unique identifier of the article to retrieve.
     * @return An Optional containing the retrieved Article object if found, otherwise an empty Optional.
     * @throws DataAccessException If an error occurs while accessing the database.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Articles getById(long id) {
        final String selectSQL = "SELECT * FROM articles WHERE id =? AND is_deleted = false";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setLong(1, id);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    Articles article = new Articles();
                    article.setId(resultSet.getLong("id"));
                    article.setUser_adder_id(resultSet.getInt("user_adder_id"));
                    article.setSubject_id(resultSet.getLong("subject_id"));
                    article.setArticle_title(resultSet.getString("article_title"));
                    article.setVisible(resultSet.getBoolean("is_visible"));
                    article.setBanned(resultSet.getBoolean("is_banned"));
                    article.setDeleted(resultSet.getBoolean("is_deleted"));
                    return article;
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<Articles> findByUserAdderId(int userAdderId) {
        final String selectSQL = "SELECT * FROM articles WHERE user_adder_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setInt(1, userAdderId);

            try (ResultSet rs = selectStatement.executeQuery()) {
                List<Articles> articles = new ArrayList<>();
                while (rs.next()) {
                    // Create an Article object from the ResultSet and return it wrapped in an Optional
                    Articles article = new Articles(
                            rs.getLong("id"),
                            rs.getInt("user_adder_id"),
                            rs.getLong("subject_id"),
                            rs.getString("article_title"),
                            rs.getBoolean("is_visible"),
                            rs.getBoolean("is_banned"),
                            rs.getBoolean("is_deleted"));

                    articles.add(article);
                }

                return articles;

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
     * @since 1.0.0
     */
    @Override
    @Transactional
    public List<Articles> getAll(int start, int limit) {
        final String selectSQL = "SELECT * FROM articles WHERE id >= ? LIMIT ?";

        if(start < 0) start = 0;
        if(limit < 25) limit = 25;
        if(limit > 150) limit = 150;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setInt(1, start);
            selectStatement.setInt(2, limit);

            List<Articles> articles = new ArrayList<>();

            try (ResultSet rs = selectStatement.executeQuery()) {

                while (rs.next()) {
                    Articles article = new Articles();
                    article.setId(rs.getLong("id"));
                    article.setUser_adder_id(rs.getInt("user_adder_id"));
                    article.setSubject_id(rs.getLong("subject_id"));
                    article.setArticle_title(rs.getString("article_title"));
                    article.setVisible(rs.getBoolean("is_visible"));
                    article.setBanned(rs.getBoolean("is_banned"));
                    article.setDeleted(rs.getBoolean("is_deleted"));

                    articles.add(article);
                }

                return articles;
            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }


    @Override
    @Transactional
    public Boolean update(Articles articles) {
        final String updateSQL = "UPDATE articles SET user_adder_id =?, article_title =?, is_banned =?, is_deleted =? WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

            updateStatement.setInt(1, articles.getUser_adder_id());
            updateStatement.setString(2, articles.getArticle_title());
            updateStatement.setBoolean(3, articles.isBanned());
            updateStatement.setBoolean(4, articles.isDeleted());
            updateStatement.setLong(5, articles.getId());

            int affectedRows = updateStatement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    @Transactional
    public boolean delete(long articleId) {
        final String deleteSQL = "DELETE FROM articles WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL)) {

            deleteStatement.setLong(1, articleId);

            int affectedRows = deleteStatement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public List<Articles> findBySubjectId(long subjectId) {
        final String selectSQL = "SELECT * FROM articles WHERE subject_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setLong(1, subjectId);

            try (ResultSet rs = selectStatement.executeQuery()) {
                List<Articles> articles = new ArrayList<>();

                while (rs.next()) {
                    Articles article = new Articles();
                    article.setId(rs.getLong("id"));
                    article.setUser_adder_id(rs.getInt("user_adder_id"));
                    article.setSubject_id(rs.getLong("subject_id"));
                    article.setArticle_title(rs.getString("article_title"));
                    article.setVisible(rs.getBoolean("is_visible"));
                    article.setBanned(rs.getBoolean("is_banned"));
                    article.setDeleted(rs.getBoolean("is_deleted"));

                    articles.add(article);
                }

                return articles;
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
}
