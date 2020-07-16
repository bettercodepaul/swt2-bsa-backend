-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_mannschaft_id START WITH 1000 INCREMENT BY 1;

/**
 * Einen Mannschaft legt die gemeldeten Schützen für eine Liga oder einen Wettkampf fest
 * aktuell werden die Anzahl der dsb_mitgliedn nicht limitiert
 * Mannschaften bestehen i.d.R. aus Vereinsname und Nummer
 * die Nummer wird durch die Rangfolge der Ligen definiert, in der höchsten Liga ist die Mannschaft 1
 **/

CREATE TABLE mannschaft (
  mannschaft_id               DECIMAL(19, 0) NOT NULL    DEFAULT nextval('sq_mannschaft_id'),
  mannschaft_verein_id        DECIMAL(19, 0) NOT NULL,
  mannschaft_nummer           DECIMAL(2, 0)  NOT NULL,
  mannschaft_benutzer_id      DECIMAL(19, 0) NOT NULL, -- der Ansprechpartner, Meldende der Mannschaft
  mannschaft_veranstaltung_id DECIMAL(19, 0) NOT NULL,

  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc              TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by                  DECIMAL(19, 0) NOT NULL    DEFAULT 0,
  last_modified_at_utc        TIMESTAMP      NULL        DEFAULT NULL,
  last_modified_by            DECIMAL(19, 0) NULL        DEFAULT NULL,
  version                     DECIMAL(19, 0) NOT NULL    DEFAULT 0,


  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_mannschaft_id PRIMARY KEY (mannschaft_id),


  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_mannschaft_verein FOREIGN KEY (mannschaft_verein_id) REFERENCES verein (verein_id)
    ON DELETE CASCADE, -- das Löschen eines Vereins löscht auch die Mannschaften des Vereins

  CONSTRAINT fk_mannschaft_veranstaltung FOREIGN KEY (mannschaft_veranstaltung_id) REFERENCES veranstaltung (veranstaltung_id)
  ON DELETE CASCADE, -- das Löschen einer Veranstaltung löscht auch die dafür gemeldeten Mannschaften des Vereins

  CONSTRAINT fk_mannschaft_benutzer FOREIGN KEY (mannschaft_benutzer_id) REFERENCES benutzer (benutzer_id)
  ON DELETE SET NULL -- das Löschen eines Benutzers lentfernt den Verweis
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_mannschaft_update_version
  BEFORE UPDATE ON mannschaft
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
