package org.example.forum.repos;

import org.example.forum.dao.UserDao;
import org.example.forum.entity.AccountType;
import org.example.forum.entity.Login;
import org.example.forum.entity.User;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class UserDaoJdbcImpl implements UserDao {

    /**
     *
     * @param user
     * @param login
     * @return boolean - Zwraca true jeżeli dodawanie użytkownika powiedzie się.
     * @author Artur Leszczak
     */

    @Override
    public boolean registerUser(User user, Login login)
    {
        final String SQL = "INSERT INTO users (name, surname, email, account_type_id) VALUES (?, ?, ?, 1)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);

            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się dodać użytkownika, proces przerwany!");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {

                    int id = rs.getInt(1);
                    user.setId(id);
                    login.setUser_id(user);

                    //TWORZY LOGIN DLA UŻYTKOWNIKA
                    if(this.createLogin(login)){
                        conn.commit();
                        return true;
                    }else {
                        conn.rollback();
                        return false;
                    }
                }
            } catch (SQLException ex) {
                conn.rollback();
                throw new DataAccessException(ex);
            }

        }catch (SQLException ex)
        {
            throw new DataAccessException(ex);
        }

        return false;
    }

    /**
     *
     * @param login
     * @return BOOLEAN - Zwraca true, jeżeli poprawnie doda wartości do tabeli.
     * @author Artur Leszczak
     */
    private boolean createLogin(Login login)
    {
        final String SQL = "INSERT INTO login (user_id, login, password) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

             conn.setAutoCommit(false);

             statement.setInt(1, login.getUser_id().getId());
             statement.setString(2, login.getLogin());
             statement.setString(3, login.getPassword());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się dodać logowania, proces przerwany!");
            }

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    conn.commit();
                    return true;
                }
            } catch (SQLException ex) {
                conn.rollback();
                throw new DataAccessException(ex);
            }

        }catch (SQLException ex)
        {
            throw new DataAccessException(ex);
        }
        return false;
    }

    @Override
    public void save(User user) {
        final String SQL = "INSERT INTO users (name, surname, email, account_type, register_date, is_deleted, delete_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());
            statement.setInt(4, 1); //ustawienie id określającego typ konta na użytkownik
            statement.setTimestamp(5, Timestamp.valueOf(user.getRegister_date()));
            statement.setBoolean(6, user.getIs_deleted());
            statement.setTimestamp(7, Timestamp.valueOf(user.getDelete_date()));
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
        final String SQL = "UPDATE users SET name = ?, surname = ? email = ?, account_type = ?, is_deleted = ?, delete_date = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());
            statement.setInt(4, 1); /* ustawia statycznie na wartość odpowiadającą typowi konta -> użytkownik */
            statement.setBoolean(5, user.getIs_deleted());
            statement.setTimestamp(6, Timestamp.valueOf(user.getDelete_date()));
            statement.setInt(6, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void delete(User user) {
        final String SQL = "UPDATE users SET is_deleted = ?, deleted_date = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setBoolean(1, user.getIs_deleted());
            statement.setTimestamp(2, Timestamp.valueOf(user.getDelete_date()));
            statement.setInt(3, user.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        final String SQL = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));

                    AccountType accountType = new AccountType();
                    accountType.setId(1);
                    accountType.setName("Użytkownik");

                    user.setAccountType(accountType);
                    user.setEmail(rs.getString("email"));
                    user.setRegister_date(rs.getTimestamp("register_date").toLocalDateTime());
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
                User user = new User();

                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setEmail(rs.getString("email"));
                user.setRegister_date(rs.getTimestamp("register_date").toLocalDateTime());
                user.set_deleted(rs.getBoolean("is_deleted"));
                user.setDelete_date(rs.getTimestamp("delete_date") != null ? rs.getTimestamp("delete_date").toLocalDateTime() : null);
                result.add(user);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return result;
    }

    /**
     *
     * @param login - Wartość tekstowa reprezentująca login
     * @param password - Wartość tekstowa reprezentująca hasło
     * @return  User | null - Zwraca użytkownika lub null w przypadku niepowodzenia
     * @author Artur Leszczak
     */
     @Override
     public Optional<User> findByLoginAndPass(String login, String password)
     {
         final String SQL = "SELECT user_id FROM login WHERE login = ? AND password = ?";

         try (Connection conn = ConnectionFactory.getConnection();
              PreparedStatement statement = conn.prepareStatement(SQL)) {
             statement.setString(1, login);
             statement.setString(2, password);

             try (ResultSet rs = statement.executeQuery()) {
                 if (rs.next()) {

                     return this.findById(rs.getInt("user_id"));

                 }
             }
         } catch (SQLException ex) {
             throw new DataAccessException(ex);
         }
         return null;

     }
}
