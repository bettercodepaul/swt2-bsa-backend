-- Löscht die Tabelle, falls sie bereits existiert
DROP TABLE IF EXISTS altsystem_uebersetzung;


CREATE SEQUENCE IF NOT EXISTS Uebersetzung_ID START WITH 1 INCREMENT BY 1;

-- Erstellt die neue Tabelle
CREATE TABLE altsystem_uebersetzung(
    uebersetzung_id int NOT NULL PRIMARY KEY DEFAULT nextval('Uebersetzung_ID'),
    kategorie VARCHAR,
    altsystem_id INTEGER,
    bogenliga_id INTEGER,
    value VARCHAR,
    UNIQUE(kategorie, altsystem_id, bogenliga_id, value)
);