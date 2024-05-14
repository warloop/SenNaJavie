CREATE TABLE articles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject_id BIGINT NOT NULL,
    user_adder_id INT NOT NULL,
    add_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    article_title VARCHAR(256) NOT NULL,
    is_visible BIT NOT NULL DEFAULT 0,
    is_banned BIT NOT NULL DEFAULT 0,
    is_deleted BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (user_adder_id) REFERENCES users(id) ON DELETE NO ACTION,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);