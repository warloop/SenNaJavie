CREATE TABLE subject_action (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        user_adder_id INT NOT NULL,
        add_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        subject_id BIGINT NOT NULL,
        action_type INT NOT NULL,
        FOREIGN KEY (user_adder_id) REFERENCES users(id) ON DELETE NO ACTION,

        FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE NO ACTION,

        FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,

        FOREIGN KEY (action_type) REFERENCES action_types(id) ON DELETE NO ACTION
);

CREATE TABLE article_action (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        user_adder_id INT NOT NULL,
        add_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        article_id BIGINT NOT NULL,
        action_type INT NOT NULL,
        FOREIGN KEY (user_adder_id) REFERENCES users(id) ON DELETE NO ACTION,

        FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,

        FOREIGN KEY (action_type) REFERENCES action_types(id) ON DELETE NO ACTION
);

CREATE TABLE section_action (
        id BIGINT PRIMARY KEY AUTO_INCREMENT,
        user_adder_id INT NOT NULL,
        add_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        section_id BIGINT NOT NULL,
        action_type INT NOT NULL,
        FOREIGN KEY (user_adder_id) REFERENCES users(id) ON DELETE NO ACTION,
        FOREIGN KEY (section_id) REFERENCES sections(id) ON DELETE NO ACTION,
        FOREIGN KEY (action_type) REFERENCES action_types(id) ON DELETE NO ACTION
);