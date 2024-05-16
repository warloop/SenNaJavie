package org.example.forum.repos;

import org.example.forum.dto.User.UserRegisterDto;
import org.example.forum.entities.AccountType;
import org.example.forum.entities.User;
import org.example.forum.exception.DataAccessException;
import org.example.forum.repos.Interfaces.IUserRepository;
import org.example.forum.util.ConnectionFactory;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;

import static org.aspectj.bridge.MessageUtil.fail;


@Repository
public class UserRepository implements IUserRepository {

    /**
     * @brief Funkcja rejestruje podstawowe dane nowego użytkownika do tabeli Users.
     * @param user - Jako parametr funkcji otrzymuje obiekt UserRegisterDto, który zawiera dane z formularza rejestracji.
     * @return boolean - Zwraca true jeżeli dodawanie użytkownika powiedzie się.
     * @author Artur Leszczak
     * @version 1.0.1
     */
    @Override
    public boolean registerUser(UserRegisterDto user) {
        final String insertUserSQL = "INSERT INTO users (name, surname, email, account_type_id) VALUES (?, ?, ?, 1)";
        final String insertLoginSQL = "INSERT INTO login (user_id, login, password) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement insertUserStatement = conn.prepareStatement(insertUserSQL, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement insertLoginStatement = conn.prepareStatement(insertLoginSQL)) {

            //POCZĄTEK TRANSAKCJI
            conn.setAutoCommit(false);

            insertUserStatement.setString(1, user.getName());
            insertUserStatement.setString(2, user.getSurname());
            insertUserStatement.setString(3, user.getEmail());

            int affectedRows = insertUserStatement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się dodać użytkownika, proces przerwany!");
            }

            try (ResultSet generatedKeys = insertUserStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    insertLoginStatement.setInt(1, userId);
                    insertLoginStatement.setString(2, user.getLogin());
                    insertLoginStatement.setString(3, user.getPassword());

                    affectedRows = insertLoginStatement.executeUpdate();

                    if (affectedRows == 0) {
                        conn.rollback();
                        throw new SQLException("Nie udało się dodać logowania, proces przerwany!");
                    }

                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    throw new SQLException("Nie udało się pobrać wygenerowanego ID użytkownika!");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     *
     * @author Mateusz Fiedosiuk
     * @version 1.0.0
     */
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

    /**
     *
     * @author Mateusz Fiedosiuk
     * @version 1.0.0
     */
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

    /**
     *
     * @author Mateusz Fiedosiuk
     * @version 1.0.0
     */
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

    /**
     *
     * @author Mateusz Fiedosiuk
     * @version 1.0.0
     */
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

    /**
     *
     * @author Mateusz Fiedosiuk
     * @version 1.0.0
     */
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
     * @version 1.0.1
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

    /**
     * Metoda sprawdza czy istnieje użytkownik o przekazanym w parametrze loginie.
     * @param login Login szukanego użytkownika.
     * @return Optional<boolean> Zwraca wynik true jeżeli uzytkownik istnieje oraz false jeżeli nie istnieje, dodatkowo zwraca null w przypadku niepowodzenia w sprawdzeniu.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Boolean> isUserExistsByLogin(String login)
    {
        final String SQL = "SELECT COUNT(id) FROM users WHERE id = (SELECT user_id FROM login WHERE login = ?) AND is_deleted = 0";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, login);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);

                    if(count == 0)
                    {
                        return Optional.of(false);
                    }
                    return Optional.of(true);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return Optional.of(null);
    }

    /**
     * Metoda sprawdza czy istnieje użytkownik o przekazanym w parametrze adresie email.
     * @param email Adres email szukanego użytkownika.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Boolean> isUserExistsByEmail(String email)
    {
        final String SQL = "SELECT COUNT(id) FROM users WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, email);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);

                    if(count == 0)
                    {
                        return Optional.of(false);
                    }
                    return Optional.of(true);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return Optional.of(null);
    }

    /**
     * Metoda wyszukuje Id_użytkownika na podstawie jego loginu.
     * @param login Parametr (String) określający login użytkownika
     * @return Optional<Integer> Zwraca wartość typu int lub null w zależności czy istnieje użytkownik o takim loginie.
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Integer> findUserIdByLogin(String login)
    {
        final String SQL = "SELECT user_id FROM login WHERE login = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, login);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    if(id > 0)
                    {
                        return Optional.of(id);
                    }
                    return Optional.of(null);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
        return Optional.of(null);
    }
}
