package org.example.forum.repositories;

import org.example.forum.entities.Role;
import org.example.forum.util.ConnectionFactory;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleRepository {

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        final String sql = "SELECT * FROM roles";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setName(rs.getString("name"));
                roles.add(role);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // lub logowanie błędu
        }

        return roles;
    }

    public boolean assignRoleToUser(Long userId, Long roleId) {
        final String deleteSQL = "DELETE FROM user_roles WHERE user_id = ?";
        final String insertSQL = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL);
             PreparedStatement insertStatement = conn.prepareStatement(insertSQL)) {

            conn.setAutoCommit(false); // Begin transaction

            // Delete existing roles
            deleteStatement.setLong(1, userId);
            deleteStatement.executeUpdate();

            // Insert new role
            insertStatement.setLong(1, userId);
            insertStatement.setLong(2, roleId);
            int affectedRows = insertStatement.executeUpdate();

            conn.commit();

            return affectedRows > 0;

        } catch (SQLException ex) {

            return false;
        }
    }
    public List<Role> getRolesByUserId(int userId) {
        List<Role> roles = new ArrayList<>();
        final String sql = "SELECT r.id, r.name FROM roles r " +
                "INNER JOIN user_roles ur ON r.id = ur.role_id " +
                "WHERE ur.user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setName(rs.getString("name"));
                roles.add(role);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // or log the error
        }

        return roles;
    }

}
