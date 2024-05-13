package org.example.forum;


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
public class UserDaoJdbcImpl implements UserDao {
    @Override
    public void save(User user) {
        final String SQL = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void update(User user) {
        final String SQL = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void delete(User user) {
        final String SQL = "DELETE FROM users WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        final String SQL = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    return Optional.of(user);
                }
            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        final String SQL = "SELECT * FROM users";
        List<User> result = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL);
             ResultSet rs = statement.executeQuery();) {
            while (rs.next()) {
                result.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"))
                );
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return result;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        final String SQL = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return null;
    }
}
