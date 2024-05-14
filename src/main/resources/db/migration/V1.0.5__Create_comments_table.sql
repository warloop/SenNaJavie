CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    artice_id BIGINT NOT NULL,
    is_answer_to_comment BIT NOT NULL DEFAULT 0,
    comment_id BIGINT DEFAULT NULL,
    user_adder_id INT NOT NULL,
    add_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    comment_text VARCHAR(256) NOT NULL,
    comment_mark SMALLINT(1) NOT NULL,
    likes INT NOT NULL DEFAULT 0,
    dislikes INT NOT NULL DEFAULT 0,
    is_banned BIT NOT NULL DEFAULT 0,
    is_deleted BIT NOT NULL DEFAULT 0,
    deleted_date TIMESTAMP DEFAULT NULL,
    FOREIGN KEY (artice_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    FOREIGN KEY (user_adder_id) REFERENCES users(id) ON DELETE CASCADE
);