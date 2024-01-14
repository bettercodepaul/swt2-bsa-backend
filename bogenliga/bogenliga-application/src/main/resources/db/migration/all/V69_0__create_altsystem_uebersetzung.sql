CREATE SEQUENCE  IF NOT EXISTS Uebersetzung_ID START WITH 1 INCREMENT BY 1;

CREATE TABLE altsystem_uebersetzung(
    uebersetzung_id int NOT NULL  PRIMARY KEY DEFAULT nextval('Uebersetzung_ID'),
    kategorie VARCHAR,
    altsystem_id INTEGER,
    bogenliga_id INTEGER
);