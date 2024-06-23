CREATE TABLE roles (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(20) UNIQUE NOT NULL
);


INSERT INTO roles (name) VALUES ('admin'), ('moderator'), ('user'), ('helpdesk');
