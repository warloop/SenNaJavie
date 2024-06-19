package org.example.forum.dao;

import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.ISectionsDao;
import org.example.forum.entities.Sections;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Klasa zawierająca podstawowe metody CRUDA pozwalające na komunikację z Bazą Danych.
 *
 * @author Artur Leszczak
 * @version 1.0.0
 */
public class SectionsDao implements ISectionsDao {

    /**
     * Pobiera pojedynczą sekcję z bazy danych na podstawie podanego identyfikatora sekcji.
     *
     * @param sectionId Unikalny identyfikator sekcji do pobrania.
     * @return Obiekt Optional zawierający pobraną sekcję, jeśli została znaleziona, w przeciwnym razie pusty Optional.
     * @throws DataAccessException Jeśli wystąpi błąd podczas uzyskiwania dostępu do bazy danych.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Optional<Sections> get(long sectionId)
    {
        final String selectSQL = "SELECT * FROM sections WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setLong(1, sectionId);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {

                    Sections section = new Sections();

                    section.setId(resultSet.getLong("id"));
                    section.setUser_adder_id(resultSet.getInt("user_adder_id"));
                    section.setSection_text(resultSet.getString("section_text"));
                    section.setArticle_id(resultSet.getLong("article_id"));
                    section.set_visible(resultSet.getBoolean("is_visible"));
                    section.set_banned(resultSet.getBoolean("is_banned"));
                    section.set_deleted(resultSet.getBoolean("is_deleted"));

                    return Optional.of(section);
                }
            }

        }catch (SQLException e) {
            throw new DataAccessException(e);
        }

        return Optional.empty();
    }

    /**
     * Pobiera wszystkie sekcje z bazy danych, które są powiązane z określonym artykułem.
     *
     * @param articleId Unikalny identyfikator artykułu, dla którego mają zostać pobrane sekcje.
     * @return Lista sekcji, które są powiązane z określonym artykułem.
     * @throws DataAccessException Jeśli wystąpi błąd podczas uzyskiwania dostępu do bazy danych.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public List<Sections> getSectionsAllInArticle(long articleId)
    {
        final String selectSQL = "SELECT * FROM sections WHERE article_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

            selectStatement.setLong(1, articleId);

            try (ResultSet resultSet = selectStatement.executeQuery()) {

                List<Sections> sections = new ArrayList<>();

                while (resultSet.next()) {

                    Sections section = new Sections();

                    section.setId(resultSet.getLong("id"));
                    section.setUser_adder_id(resultSet.getInt("user_adder_id"));
                    section.setSection_text(resultSet.getString("section_text"));
                    section.setArticle_id(resultSet.getLong("article_id"));
                    section.set_visible(resultSet.getBoolean("is_visible"));
                    section.set_banned(resultSet.getBoolean("is_banned"));
                    section.set_deleted(resultSet.getBoolean("is_deleted"));

                    sections.add(section);
                }
                return sections;
            }

        }catch (SQLException e) {
            throw new DataAccessException(e);
        }

    }

    /**
     * Dodaje nową sekcję do bazy danych.
     *
     * @param section Obiekt sekcji do dodania.
     * @return Optional zawierający identyfikator nowo dodanej sekcji, jeśli operacja się powiedzie, w przeciwnym razie pusty Optional.
     * @throws DataAccessException Jeśli wystąpi błąd podczas uzyskiwania dostępu do bazy danych.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Optional<Long> add(Sections section)
    {
        final String insertSQL = "INSERT INTO sections (user_adder_id, article_id, section_text, is_visible, is_banned, is_deleted) VALUES (?,?,?,?,?,?) ";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(insertSQL)) {

             insertStatement.setInt(1, section.getUser_adder_id());
             insertStatement.setLong(2,section.getArticle_id());
             insertStatement.setString(3,section.getSection_text());
             insertStatement.setBoolean(4,section.is_visible());
             insertStatement.setBoolean(5,false); //is banned
             insertStatement.setBoolean(6,false); //is deleted

            int affectedRows = insertStatement.executeUpdate();

            if(affectedRows == 0) throw new SQLException("Nie udało się dodać sekcjii!");

            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if(generatedKeys.next()) section.setId(generatedKeys.getLong(1));
            }

            return Optional.of(section.getId());

        }catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    /**
     * Aktualizuje istniejącą sekcję w bazie danych.
     *
     * @param section Obiekt sekcji do zaktualizowania.
     * @return Wartość logiczna wskazująca, czy operacja aktualizacji zakończyła się powodzeniem.
     * @throws DataAccessException Jeśli wystąpi błąd podczas uzyskiwania dostępu do bazy danych.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Boolean update(Sections section)
    {
        final String updateSQL = "UPDATE sections SET user_adder_id = ?, article_id =?, section_text=?, is_visible=?, is_banned=?, is_deleted=? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

            updateStatement.setInt(1, section.getUser_adder_id());
            updateStatement.setLong(2,section.getArticle_id());
            updateStatement.setString(3,section.getSection_text());
            updateStatement.setBoolean(4,section.is_visible());
            updateStatement.setBoolean(5,section.is_banned());
            updateStatement.setBoolean(6,section.is_deleted());

            int affectedRows = updateStatement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    /**
     * Usuwa sekcję z bazy danych poprzez ustawienie flagi 'is_deleted' na true.
     *
     * @param sectionId Unikalny identyfikator sekcji do usunięcia.
     * @return Wartość logiczna wskazująca, czy operacja usuwania zakończyła się powodzeniem.
     * @throws DataAccessException Jeśli wystąpi błąd podczas uzyskiwania dostępu do bazy danych.
     *
     * @author Artur Leszczak
     * @version 1.0.0
     */
    @Override
    @Transactional
    public Boolean delete(long sectionId)
    {
        final String deleteSQL = "UPDATE sections SET is_deleted = true WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL)) {

            deleteStatement.setLong(1, sectionId);

            int affectedRows = deleteStatement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

}
