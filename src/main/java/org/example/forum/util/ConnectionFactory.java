package org.example.forum.util;

import org.example.forum.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class ConnectionFactory {

    @Value("${spring.datasource.url}")
    private static String url;

    @Value("${spring.datasource.username}")
    private static String username;

    @Value("${spring.datasource.password}")
    private static String password;
    public ConnectionFactory(@Value("${spring.datasource.url}") String url,
                             @Value("${spring.datasource.username}") String username,
                             @Value("${spring.datasource.password}") String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }
}