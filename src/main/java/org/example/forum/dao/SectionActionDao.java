package org.example.forum.dao;

import org.example.forum.dao.Interfaces.ISectionActionDao;
import org.example.forum.exception.DataAccessException;
import org.example.forum.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SectionActionDao implements ISectionActionDao {

    @Override
    public Boolean add(int action_id, int user_id, long subject_id)
    {
        final String insertSQL = "INSERT INTO section_action (action_type, user_adder_id, section_id) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            //POCZĄTEK TRANSAKCJI
            conn.setAutoCommit(false);

            insertStatement.setInt(1, action_id);
            insertStatement.setInt(2, user_id);
            insertStatement.setLong(3, subject_id);

            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Nie udało się dodać adnotacji o wszczętej akcji wobec sekcji!");
            }

            conn.commit();
            return true;

        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
}
