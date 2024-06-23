CREATE TABLE subjects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_adder_id INT NOT NULL,
    subject_text VARCHAR(128) NOT NULL,
    is_banned BIT NOT NULL DEFAULT 0,
    is_deleted BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (user_adder_id) REFERENCES users(id) ON DELETE NO ACTION
);