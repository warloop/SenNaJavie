CREATE TABLE report_types (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(48) NOT NULL,
    description VARCHAR(256)
);

INSERT INTO report_types (id, name, description) VALUES (1,"Przekleństwa w treści", "Zgłaszany wątek zawiera w swojej treści wulgaryzmy.");
INSERT INTO report_types (id, name, description) VALUES (2,"Groźby karalne", "Zgłaszany wątek zawiera w swojej treści groźby wystosunkowane do konkretnej osoby.");
INSERT INTO report_types (id, name, description) VALUES (3,"Nękanie", "Zgłaszany wątek nakierowany jest na nękanie konkretnej osoby / grupy.");