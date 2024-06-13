package org.example.forum.dao;

import org.example.forum.dao.Interfaces.IAccountTypeDao;
import org.example.forum.dao.Interfaces.IUserDao;
import org.example.forum.entities.User;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserDao implements IUserDao {

    IAccountTypeDao accountTypeDao;

    /**
     * Dodaje nowego użytkownika do bazy danych.
     *
     * @param user Obiekt użytkownika do dodania.
     * @return Obiekt Optional zawierający unikalny identyfikator nowo dodanego użytkownika, jeśli operacja zakończyła się powodzeniem, w przeciwnym razie pusty Optional.
     * @throws DataAccessException Jeśli wystąpi błąd podczas wykonywania instrukcji SQL.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Optional<Integer> add(User user) {
        final String insertSQL = "INSERT INTO users (name, surname, email, account_type_id) VALUES (?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, user.getName());
            insertStatement.setString(2, user.getSurname());
            insertStatement.setString(3, user.getEmail());
            insertStatement.setInt(4, user.getAccountType().getId());

            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Nie udało się dodać użytkownika!");
            }

            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }

            return Optional.of(user.getId());

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Pobiera użytkownika z bazy danych na podstawie podanego unikalnego identyfikatora.
     *
     * @param id Unikalny identyfikator użytkownika do pobrania.
     * @return Obiekt Optional zawierający pobrany obiekt User, jeśli został znaleziony, w przeciwnym razie pusty Optional.
     * @throws DataAccessException Jeśli wystąpi błąd podczas wykonywania instrukcji SQL.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public User get(int id) {
        final String selectSQL = "SELECT * FROM users WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setInt(1, id);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setEmail(resultSet.getString("email"));
                    user.setAccountType(accountTypeDao.get(resultSet.getInt("account_type_id")));
                    user.setRegister_date(resultSet.getTimestamp("register_date").toLocalDateTime());
                    user.set_deleted(resultSet.getBoolean("is_deleted"));
                    user.setDelete_date(resultSet.getTimestamp("delete_date")!= null? resultSet.getTimestamp("delete_date").toLocalDateTime() : null);
                    return user;
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Aktualizuje istniejącego użytkownika w bazie danych.
     *
     * @param user Obiekt użytkownika zawierający zaktualizowane informacje.
     * @return {@code true}, jeśli użytkownik został pomyślnie zaktualizowany, w przeciwnym razie {@code false}.
     * @throws DataAccessException Jeśli wystąpi błąd podczas wykonywania instrukcji SQL.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Boolean update(User user) {
        final String updateSQL = "UPDATE users SET name =?, surname =?, email =?, account_type_id =? WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

            updateStatement.setString(1, user.getName());
            updateStatement.setString(2, user.getSurname());
            updateStatement.setString(3, user.getEmail());
            updateStatement.setInt(4, user.getAccountType().getId());
            updateStatement.setInt(5, user.getId());

            int affectedRows = updateStatement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Usuwa użytkownika z bazy danych poprzez ustawienie flagi 'is_deleted' na true oraz 'delete_date' na bieżącą datę i czas.
     *
     * @param id Unikalny identyfikator użytkownika do usunięcia.
     * @return {@code true}, jeśli użytkownik został pomyślnie usunięty, w przeciwnym razie {@code false}.
     * @throws DataAccessException Jeśli wystąpi błąd podczas wykonywania instrukcji SQL.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    public Boolean delete(int id) {
        final String deleteSQL = "UPDATE users SET is_deleted = true, delete_date =? WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL)) {

            deleteStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            deleteStatement.setInt(2, id);

            int affectedRows = deleteStatement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }



}
