package org.example.forum.dao;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.ILoginDao;

import org.example.forum.dao.Interfaces.IUserDao;
import org.example.forum.entities.Login;

import org.example.forum.entities.User;
import org.example.forum.exception.DataAccessException;
import org.example.forum.exception.UserIsNotExistsException;
import org.example.forum.util.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.Optional;

public class LoginDao implements ILoginDao {

        @Autowired
        private IUserDao userDao;

        public LoginDao(IUserDao userDao) {
            this.userDao = userDao;
        }

        /**
         * Pobiera pojedynczy rekord logowania z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator rekordu logowania do pobrania.
         * @return Obiekt typu Login reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Login get(int id) {
            final String selectSQL = "SELECT * FROM login WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setInt(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Login login = new Login();

                        if(this.getUserById(resultSet.getInt("user_id")).isEmpty()) return null;

                        login.setId(resultSet.getInt("id"));
                        login.setUser_id(this.getUserById(resultSet.getInt("user_id")).get());
                        login.setLogin(resultSet.getString("login"));
                        login.setPassword(resultSet.getString("password"));
                        login.setActive(resultSet.getBoolean("active"));

                        return login;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Pobiera pojedynczy rekord użytkownika z bazy danych na podstawie podanego identyfikatora użytkownika.
         *
         * @param userId Unikalny identyfikator użytkownika, dla którego należy pobrać rekord.
         * @return Obiekt Optional zawierający obiekt User reprezentujący pobrany rekord,
         *         lub pusty Optional, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        private Optional<User> getUserById(int userId) {
            User foundedUser;
            foundedUser = this.userDao.get(userId);

            if(foundedUser!= null) return Optional.of(foundedUser);

            return Optional.empty();
        }

        /**
         * Metoda dodająca nowe logowanie do bazy danych.
         *
         * @param login Obiekt typu Login z danymi do dodania.
         * @return Zwraca opcjonalny obiekt Integer, który zawiera identyfikator nowo dodanego logowania.
         *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        public Optional<Integer> add(Login login) {
            final String insertSQL = "INSERT INTO login (user_id, login, password, active) VALUES (?,?,?,?)";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                insertStatement.setInt(1, login.getUser_id().getId());
                insertStatement.setString(2, login.getLogin());
                insertStatement.setString(3, login.getPassword());
                insertStatement.setBoolean(4, login.isActive());

                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        login.setId(generatedKeys.getInt(1));
                    }
                }

                return Optional.of(login.getId());

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda aktualizująca logowanie w bazie danych.
         *
         * @param login Obiekt typu Login z nowymi danymi.
         * @return Zwraca {@code true}, jeśli logowanie zostało pomyślnie zaktualizowane; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean update(Login login) {
            final String updateSQL = "UPDATE login SET user_id =?, login =?, password =?, active =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setInt(1, login.getUser_id().getId());
                updateStatement.setString(2, login.getLogin());
                updateStatement.setString(3, login.getPassword());
                updateStatement.setBoolean(4, login.isActive());
                updateStatement.setInt(5, login.getId());

                int affectedRows = updateStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda usuwająca logowanie z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator logowania do usunięcia.
         * @return Zwraca {@code true}, jeśli logowanie zostało pomyślnie usunięte; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean delete(int id) {
            final String deleteSQL = "DELETE FROM login WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL)) {

                deleteStatement.setInt(1, id);

                int affectedRows = deleteStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        @Override
        @Transactional
        public Optional<Login> getLoginObjectByLogin(String login)
        {
            final String sqlSyntax = "SELECT * FROM login WHERE login=?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(sqlSyntax)) {

                selectStatement.setString(1, login);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {

                        Login login1 = new Login();

                        login1.setId(resultSet.getInt("id"));

                        User user1 = null;
                        Optional<Integer> userId = Optional.of(resultSet.getInt("user_id"));
                        if(userId.get() <= 0) { throw new UserIsNotExistsException(404,"Nie znaleziono użytkownika");}
                        user1 = this.userDao.get(userId.get());

                        login1.setUser_id(user1);
                        login1.setLogin(resultSet.getString("login"));
                        login1.setPassword(resultSet.getString("password"));
                        login1.setActive(resultSet.getBoolean("active"));

                        return Optional.of(login1);
                    }

                    return Optional.empty();
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }catch (UserIsNotExistsException e) {
                return Optional.empty();
            }

        }

}
