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
CREATE SEQUENCE sq_veranstaltung_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Klasse dient der defintion von Veranstaltungen im Sportjahr
 * für die Ligen könnte auf dieser Ebenbe bereits die Diaziplin festgelegt werden
 * allgemein geht dies aber erst auf der Ebene Wettkampf.
 **/
CREATE TABLE veranstaltung (
  veranstaltung_id                 DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_veranstaltung_id'), -- DECIMAL(19,0) = unsigned long
  veranstaltung_wettkampftyp_id      DECIMAL(19,0) NOT NULL, --bezug zu den Regeln
  veranstaltung_name                VARCHAR(200)  NOT NULL,
  veranstaltung_sportjahr           DECIMAL(4,0) NOT NULL, --Okt 2018 bis Okt 2019 wird als 2019 erfasst
  veranstaltung_ligaleiter          VARCHAR(200), -- bei Liga-Wettkämpfen der Koordinator aller Wettkämpfe des Sportjahres
  veranstaltung_ligaleiter_email      VARCHAR(200),
  veranstaltung_meldedeadline       DATE NOT NULL, --Termin zu dem die Anmeldungen abgeschlossen wird
  veranstaltung_dauer               DECIMAL (1,0), -- Anzahl Tage der Veranstaltung (Bsp: Liga: 4; WA: 2, Kreismeisterschaften 1, ..)
  veranstaltung_wettkampfdauer      NUMERIC (2,1), -- Dauer eines Wettkampf, i.d.R. 0,5
  veranstaltung_kampfrichter_anzahl    DECIMAL(2,0), -- benötigte Kampfrichter im Wettkampf
  veranstaltung_hoehere         DECIMAL(19,0), -- Aufstiegsliga oder Weitermeldung in Folgeveranstaltung
-- TODO Referenz zum User statt "ligaleiter-texte"


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
  CONSTRAINT pk_veranstaltung_id PRIMARY KEY (veranstaltung_id),

   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_veranstaltung_wettkampftyp FOREIGN KEY (veranstaltung_wettkampftyp_id) REFERENCES wettkampftyp (wettkampftyp_id)
    ON DELETE CASCADE, -- das Löschen eines wettkampftyps löscht auch die zugehörigen veranstaltungen

  CONSTRAINT fk_veranstaltung_veranstaltung FOREIGN KEY (veranstaltung_id) REFERENCES veranstaltung (veranstaltung_id)
   ON DELETE SET NULL -- das Loeschen einer Region wuerde alle Vereine und deren dsb_mitgliedn entfernen

);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V0__row_version_update.sql
CREATE TRIGGER tr_veranstaltung_update_version
  BEFORE UPDATE ON veranstaltung
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
