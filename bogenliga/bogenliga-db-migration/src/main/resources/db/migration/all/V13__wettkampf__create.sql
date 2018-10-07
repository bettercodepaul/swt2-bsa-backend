-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_wettkampf_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Klasse dient der Defintion von Wettkämpfen d.h. der konkreten Durchführung einer Veranstaltung
 **/
CREATE TABLE wettkampf (
  wettkampf_id                  DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_wettkampf_id'), -- DECIMAL(19,0) = unsigned long
  wettkampf_veranstaltung_id    DECIMAL(19,0) NOT NULL, --bezug zum Sportjahr
  wettkampf_datum               DATE NOT NULL, -- Termin der Durchführung
  wettkampf_ort                 VARCHAR(200), -- Anschrift, Name der Sporthalle, mehrere Zeilen
  wettkampf_beginn              VARCHAR(5) NOT NULL, -- Uhrzeit im Format hh:mm
  wettkampf_tag                 DECIMAL(1,0) NOT NULL, -- Liga hat 4 Wettkampftage, sonst 1
  wettkampf_disziplin_id        DECIMAL(19,0) NOT NULL,
  wettkampf_wettkampftyp_id     DECIMAL(19,0) NOT NULL,


  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
  last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
  last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
  version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,


  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_wettkampf_id PRIMARY KEY (wettkampf_id),


   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_wettkampf_wettkampftyp FOREIGN KEY (wettkampf_wettkampftyp_id ) REFERENCES wettkampftyp (wettkampftyp_id)
    ON DELETE CASCADE, -- das Löschen eines wettkampftyps löscht auch die zugehörigen wettkämpfe

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_wettkampf_disziplin FOREIGN KEY (wettkampf_disziplin_id) REFERENCES disziplin (disziplin_id)
    ON DELETE CASCADE -- das Löschen einer Disziplin löscht auch die Lizenzen
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_wettkampf_update_version
  BEFORE UPDATE ON wettkampf
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
