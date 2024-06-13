package org.example.forum.util;

import org.example.forum.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class ConnectionFactory {

    private static final String PORT = "12832";
    private static final String DATABASE_NAME = "forum";
    private static final String URL = "jdbc:mysql://localhost:"+PORT+"/"+DATABASE_NAME;
    private static final String USER = "forumaplication";
    private static final String PASSWORD = "tajnehaslo";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
}