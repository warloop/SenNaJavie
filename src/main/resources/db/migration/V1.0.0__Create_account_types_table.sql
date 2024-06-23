CREATE TABLE account_type (
                              id SMALLINT PRIMARY KEY AUTO_INCREMENT,
                              name VARCHAR(32) NOT NULL,
                              description VARCHAR(256)
);

INSERT INTO account_type (id, name, description) VALUES (1, "Użytkownik", "Zwykły użytkownik witryny");
INSERT INTO account_type (id, name, description) VALUES (2, "Moderator", "Moderator witryny, może edytować treści oraz je ukrywać");
INSERT INTO account_type (id, name, description) VALUES (3, "Administrator", "Administrator witryny może nadawać uprawnienia innym uzytkownikom witryny, może robić to samo co moderator i użytkownik");