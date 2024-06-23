package org.example.forum.dao;


import jakarta.transaction.Transactional;
import org.example.forum.dao.Interfaces.IReportTypesDao;
import org.example.forum.entities.Report_types;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;
import java.sql.*;
import java.util.Optional;

public class ReportTypesDao implements IReportTypesDao {

        /**
         * Pobiera pojedynczy rekord typu zgłoszenia z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator rekordu typu zgłoszenia do pobrania.
         * @return Obiekt typu Report_types reprezentujący pobrany rekord lub null, jeśli nie znaleziono żadnego rekordu.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Report_types get(int id) {
            final String selectSQL = "SELECT * FROM report_types WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement selectStatement = conn.prepareStatement(selectSQL)) {

                selectStatement.setInt(1, id);

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Report_types reportType = new Report_types();
                        reportType.setId(resultSet.getInt("id"));
                        reportType.setName(resultSet.getString("name"));
                        reportType.setDescription(resultSet.getString("description"));
                        return reportType;
                    } else {
                        return null;
                    }
                }

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda dodająca nowy typ zgłoszenia do bazy danych.
         *
         * @param reportType Obiekt typu Report_types z danymi do dodania.
         * @return Zwraca opcjonalny obiekt Integer, który zawiera identyfikator nowo dodanego typu zgłoszenia.
         *         Jeśli dodanie nie powiodło się, zwraca pusty Optional.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Optional<Integer> add(Report_types reportType) {
            final String insertSQL = "INSERT INTO report_types (name, description) VALUES (?,?)";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement insertStatement = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

                insertStatement.setString(1, reportType.getName());
                insertStatement.setString(2, reportType.getDescription());

                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reportType.setId(generatedKeys.getInt(1));
                    }
                }

                return Optional.of(reportType.getId());

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda aktualizująca typ zgłoszenia w bazie danych.
         *
         * @param reportType Obiekt typu Report_types z nowymi danymi.
         * @return Zwraca {@code true}, jeśli typ zgłoszenia został pomyślnie zaktualizowany; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean update(Report_types reportType) {
            final String updateSQL = "UPDATE report_types SET name =?, description =? WHERE id =?";

            try (Connection conn = ConnectionFactory.getConnection();
                 PreparedStatement updateStatement = conn.prepareStatement(updateSQL)) {

                updateStatement.setString(1, reportType.getName());
                updateStatement.setString(2, reportType.getDescription());
                updateStatement.setInt(3, reportType.getId());

                int affectedRows = updateStatement.executeUpdate();

                return affectedRows > 0;

            } catch (SQLException ex) {
                throw new DataAccessException(ex);
            }
        }

        /**
         * Metoda usuwająca typ zgłoszenia z bazy danych na podstawie podanego identyfikatora.
         *
         * @param id Unikalny identyfikator typu zgłoszenia do usunięcia.
         * @return Zwraca {@code true}, jeśli typ zgłoszenia został pomyślnie usunięty; {@code false} w przeciwnym przypadku.
         * @throws DataAccessException Jeśli wystąpi błąd podczas interakcji z bazą danych.
         *
         * @author Artur Leszczak
         * @version 1.0.0
         */
        @Override
        @Transactional
        public Boolean delete(int id) {
            final String deleteSQL = "DELETE FROM report_types WHERE id =?";

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
