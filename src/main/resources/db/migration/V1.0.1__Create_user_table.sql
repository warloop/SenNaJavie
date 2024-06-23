CREATE TABLE users (
           id INT PRIMARY KEY AUTO_INCREMENT,
           name VARCHAR(48) NOT NULL,
           surname VARCHAR(48) NOT NULL,
           email VARCHAR(256) NOT NULL UNIQUE,
           account_type_id SMALLINT NOT NULL,
           register_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
           is_deleted BIT NOT NULL DEFAULT 0,
           delete_date TIMESTAMP DEFAULT NULL,
           FOREIGN KEY (account_type_id) REFERENCES account_type(id) ON DELETE NO ACTION
);

CREATE TABLE login (
           id INT PRIMARY KEY AUTO_INCREMENT,
           user_id INT NOT NULL UNIQUE,
           login VARCHAR(16) NOT NULL UNIQUE,
           password VARCHAR(256) NOT NULL,
           active BIT NOT NULL DEFAULT 1,
           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);