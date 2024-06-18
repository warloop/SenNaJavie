package org.example.forum.dao;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.IAccountTypeDao;
import org.example.forum.entities.AccountType;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.*;

import java.util.Optional;

public class AccountTypeDao implements IAccountTypeDao {

    /**
     * Pobiera pojedynczy rekord typu konta z bazy danych na podstawie podanego identyfikatora.
     *
     * @param id Unikalny identyfikator rekordu typu konta do pobrania.
     * @return Obiekt typu AccountType reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
     * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public AccountType get(int id) {
        final String selectSQL = "SELECT * FROM account_types WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setInt(1, id);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    AccountType accountType = new AccountType();
                    accountType.setId(resultSet.getInt("id"));
                    accountType.setName(resultSet.getString("name"));
                    accountType.setDescription(resultSet.getString("description"));
                    return accountType;
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Metoda dodająca nowy typ konta do bazy danych.
     *
     * @param accountType Obiekt typu konta z danymi do dodania.
     * @return Zwraca opcjonalny obiekt Integer, który zawiera identyfikator nowo dodanego typu konta.
     *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
     * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Optional<Integer> add(AccountType accountType) {
        final String insertSQL = "INSERT INTO account_type (name, description) VALUES (?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, accountType.getName());
            insertStatement.setString(2, accountType.getDescription());

            insertStatement.executeUpdate();

            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    accountType.setId(generatedKeys.getInt(1));
                }
            }

            return Optional.of(accountType.getId());

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Metoda aktualizująca typ konta w bazie danych.
     *
     * @param accountType Obiekt typu konta z nowymi danymi.
     * @return Zwraca {@code true}, jeśli typ konta został pomyślnie zaktualizowany; {@code false} w przeciwnym przypadku.
     * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Boolean update(AccountType accountType) {
        final String updateSQL = "UPDATE account_type SET name =?, description =? WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

            updateStatement.setString(1, accountType.getName());
            updateStatement.setString(2, accountType.getDescription());
            updateStatement.setInt(3, accountType.getId());

            int affectedRows = updateStatement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Metoda usuwająca typ konta z bazy danych na podstawie podanego identyfikatora.
     *
     * @param id Unikalny identyfikator typu konta do usunięcia.
     * @return Zwraca {@code true}, jeśli typ konta został pomyślnie usunięty; {@code false} w przeciwnym przypadku.
     * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Boolean delete(int id) {
        final String deleteSQL = "DELETE FROM account_type WHERE id =?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL)) {

            deleteStatement.setInt(1, id);

            int affectedRows = deleteStatement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

}
