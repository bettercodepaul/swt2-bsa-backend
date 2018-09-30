-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_dsb_mitglied_id START WITH 1000 INCREMENT BY 1;

/**
 * Ein Schütze enthält die notwendigen personenbezogenen Daten.
 * Ein Schütze ist immer einem Verein zugewiesen.
 **/
CREATE TABLE dsb_mitglied (
  dsb_mitglied_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_dsb_mitglied_id'), -- DECIMAL(19,0) = unsigned long
  dsb_mitglied_vorname          VARCHAR(200)    NOT NULL,
  dsb_mitglied_nachname         VARCHAR(200)    NOT NULL,
  dsb_mitglied_geburtsdatum     DATE NOT NULL,
  dsb_mitglied_nationalitaet    VARCHAR(5)      NOT NULL, -- TODO Format in User Store gem. ISO festelegen und prüfen
  dsb_mitglied_mitgliedsnummer  VARCHAR(200)    NOT NULL,
  dsb_mitglied_verein_id        DECIMAL(19,0)   NOT NULL, --Fremdschluessel zum Verein
  dsb_mitglied_benutzer_id      DECIMAL(19,0)   NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_dsb_mitglied_id PRIMARY KEY (dsb_mitglied_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_dsb_mitglied_mitgliedsnummer UNIQUE (dsb_mitglied_mitgliedsnummer),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_dsb_mitglied_benutzer_id FOREIGN KEY (dsb_mitglied_benutzer_id) REFERENCES benutzer (benutzer_id)
    ON DELETE SET NULL,
  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_dsb_mitglied_verein FOREIGN KEY (dsb_mitglied_verein_id) REFERENCES verein (verein_id)
    ON DELETE CASCADE -- das Löschen eines Vereins löscht auch dessen Mitglieder
);
