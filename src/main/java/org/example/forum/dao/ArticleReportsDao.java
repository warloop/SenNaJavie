package org.example.forum.dao;


import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.IArticleReportsDao;
import org.example.forum.entities.Article_reports;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;
import java.sql.*;
import java.util.Optional;

public class ArticleReportsDao implements IArticleReportsDao {

        /**
         * Pobiera pojedynczy rekord raportu artykułu z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator rekordu raportu artykułu do pobrania.
         * @return Obiekt typu Article_reports reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Article_reports get(long id) {
            final String selectSQL = "SELECT * FROM article_reports WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setLong(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Article_reports articleReport = new Article_reports();
                        articleReport.setId(resultSet.getLong("id"));
                        articleReport.setArticle_id(resultSet.getLong("article_id"));
                        articleReport.setReport_type(resultSet.getInt("report_type"));
                        articleReport.setUser_reporter_id(resultSet.getInt("user_reporter_id"));
                        articleReport.setReport_date(resultSet.getTimestamp("add_date").toLocalDateTime());
                        return articleReport;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda dodająca nowy raport artykułu do bazy danych.
         *
         * @param articleReport Obiekt typu raportu artykułu z danymi do dodania.
         * @return Zwraca opcjonalny obiekt Long, który zawiera identyfikator nowo dodanego raportu artykułu.
         *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Optional<Long> add(Article_reports articleReport) {
            final String insertSQL = "INSERT INTO article_reports (article_id, report_type, user_reporter_id, add_date) VALUES (?,?,?,?)";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                insertStatement.setLong(1, articleReport.getArticle_id());
                insertStatement.setInt(2, articleReport.getReport_type());
                insertStatement.setInt(3, articleReport.getUser_reporter_id());
                insertStatement.setTimestamp(4, Timestamp.valueOf(articleReport.getReport_date()));

                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        articleReport.setId(generatedKeys.getLong(1));
                    }
                }

                return Optional.of(articleReport.getId());

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda aktualizująca raport artykułu w bazie danych.
         *
         * @param articleReport Obiekt typu raportu artykułu z nowymi danymi.
         * @return Zwraca {@code true}, jeśli raport artykułu został pomyślnie zaktualizowany; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean update(Article_reports articleReport) {
            final String updateSQL = "UPDATE article_reports SET article_id =?, report_type =?, user_reporter_id =?, add_date =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setLong(1, articleReport.getArticle_id());
                updateStatement.setInt(2, articleReport.getReport_type());
                updateStatement.setInt(3, articleReport.getUser_reporter_id());
                updateStatement.setTimestamp(4, Timestamp.valueOf(articleReport.getReport_date()));
                updateStatement.setLong(5, articleReport.getId());

                int affectedRows = updateStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda usuwająca raport artykułu z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator raportu artykułu do usunięcia.
         * @return Zwraca {@code true}, jeśli raport artykułu został pomyślnie usunięty; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean delete(long id) {
            final String deleteSQL = "DELETE FROM article_reports WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL)) {

                deleteStatement.setLong(1, id);

                int affectedRows = deleteStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

}
