CREATE TABLE users (
           id BIGINT PRIMARY KEY AUTO_INCREMENT,
           name VARCHAR(48) NOT NULL,
           surname VARCHAR(48) NOT NULL,
           email VARCHAR(256) NOT NULL,
           account_type_id SMALLINT NOT NULL,
           register_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
           is_deleted BIT NOT NULL DEFAULT 0,
           delete_date TIMESTAMP DEFAULT NULL,
           FOREIGN KEY (account_type_id) REFERENCES account_type(id) ON DELETE NO ACTION
);

INSERT INTO users (id, name, surname, email, account_type_id) VALUES (1, "Admin", "Administracyjny", "admin@example.com", 3);
INSERT INTO users (id, name, surname, email, account_type_id) VALUES (2, "Moderator", "Moderacyjny", "moderator@example.com", 2);