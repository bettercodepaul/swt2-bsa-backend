/*
 * general conventions:
 * - lower case
 * - use "_"
 * - use table name for column prefixes
 *
 * example:
 * table name = "user"
 * column prefix = "user_"
 */


-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_schuetze_id START WITH 1000 INCREMENT BY 1;

/**
 * Ein Schütze enthält die notwendigen personenbezogenen Daten.
 * Ein Schütze ist immer einem Verein zugewiesen.
 **/
CREATE TABLE schuetze (
  schuetze_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_schuetze_id'), -- DECIMAL(19,0) = unsigned long
  schuetze_vorname          VARCHAR(200)    NOT NULL,
  schuetze_nachname         VARCHAR(200)    NOT NULL,
  schuetze_jahrgang         DECIMAL(4,0)    NOT NULL, -- YYYY allowed
  schuetze_nationalitaet    VARCHAR(5)      NOT NULL, -- TODO Welches Format? ISO XXX?
  schuetze_mitgliedsnummer  VARCHAR(200)    NOT NULL,
  schuetze_email            VARCHAR(200)    NOT NULL, -- TODO Haben wir in der Produktion für alle Schützen die E-Mails? Oder sollte es erstmal Nullable sein?
  schuetze_verein_id        DECIMAL(19,0)   NOT NULL, --Fremdschluessel zum Verein

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_schuetze_id PRIMARY KEY (schuetze_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_schuetze_mitgliedsnummer UNIQUE (schuetze_mitgliedsnummer),
  CONSTRAINT uc_schuetze_email UNIQUE (schuetze_email),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_schuetze_verein FOREIGN KEY (schuetze_verein_id) REFERENCES verein (verein_id)
    ON DELETE CASCADE -- das Löschen eines Vereins löscht auch dessen Mitglieder
);
