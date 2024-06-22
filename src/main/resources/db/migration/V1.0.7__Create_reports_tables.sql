CREATE TABLE subject_reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject_id BIGINT NOT NULL,
    report_type INT NOT NULL,
    user_reporter_id INT NOT NULL,
    report_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_viewed BIT NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_reporter_id) REFERENCES users(id) ON DELETE NO ACTION,
    FOREIGN KEY (report_type) REFERENCES report_types(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

CREATE TABLE article_reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    article_id BIGINT NOT NULL,
    report_type INT NOT NULL,
    user_reporter_id INT NOT NULL,
    report_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_viewed BIT NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_reporter_id) REFERENCES users(id) ON DELETE NO ACTION,
    FOREIGN KEY (report_type) REFERENCES report_types(id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

CREATE TABLE comments_reports (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        comment_id BIGINT NOT NULL,
        report_type INT NOT NULL,
        user_reporter_id INT NOT NULL,
        report_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        is_viewed BIT NOT NULL DEFAULT FALSE,
        FOREIGN KEY (user_reporter_id) REFERENCES users(id) ON DELETE NO ACTION,
        FOREIGN KEY (report_type) REFERENCES report_types(id) ON DELETE CASCADE,
        FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE
);