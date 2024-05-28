CREATE TABLE action_types (
        id INT PRIMARY KEY AUTO_INCREMENT,
        type_name VARCHAR(64) NOT NULL,
        description VARCHAR(256) DEFAULT NULL
);

INSERT INTO action_types (id,type_name,description) VALUES (1,"Dodano treść", "Informacja została dodana do bazy");
INSERT INTO action_types (id,type_name,description) VALUES (2,"Wykonano zmianę treści", "Informacja została zmieniona / lub podjęto taką próbę.");
INSERT INTO action_types (id,type_name,description) VALUES (3,"Udostępniono widoczność treści", "Informacja została udostępniona przez właściciela");
INSERT INTO action_types (id,type_name,description) VALUES (4,"Ukryto treść", "Informacja została ukryta przez właściciela");
INSERT INTO action_types (id,type_name,description) VALUES (5,"Usunieto treść", "Informacja została usunięta przez właściciela");
INSERT INTO action_types (id,type_name,description) VALUES (6,"Zablokowano treść", "Informacja została zablokowana przez moderatora");
INSERT INTO action_types (id,type_name,description) VALUES (7,"Odblokowano treść", "Informacja została odblokowana przez moderatora");
INSERT INTO action_types (id,type_name,description) VALUES (8,"Usunięto treść", "Informacja została usunięta przez moderatora");