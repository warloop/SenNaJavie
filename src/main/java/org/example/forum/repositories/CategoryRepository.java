package org.example.forum.repositories;

import org.example.forum.entities.Category;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    public Optional<Long> addCategory(String name) {
        final String SQL = "INSERT INTO categories (category_name) VALUES (?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {  // Użycie RETURN_GENERATED_KEYS

            conn.setAutoCommit(false);

            statement.setString(1, name);


            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się dodać kategorii, proces przerwany!");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {  // Pobieranie wygenerowanych kluczy
                if (rs.next()) {
                    conn.commit();
                    long addedCategoryId = rs.getLong(1);  // Pobieranie pierwszej kolumny (wygenerowane ID)
                    return Optional.of(addedCategoryId);
                } else {
                    conn.rollback();
                    return Optional.empty();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new DataAccessException(e);
            }

        } catch (SQLException ex) {
            return Optional.empty();
        }
    }

    public List<Category> getAllCategories() {
        final String SQL = "SELECT * FROM categories";
        List<Category> result = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Category category= new Category();
                    category.setId(rs.getLong("id"));
                    category.setCategory_name(rs.getString("category_name"));
                    result.add(category);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return result;
    }

}
