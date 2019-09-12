/**
 * Eine Klasse dient der Defintion von Runden im Wettkampf - sie hat nur eine laufende Nummer als ID
 * wir erwarten entweder 2 (WA-Wettkampf) oder 7 Runden (Liga),
 *
 * Der Aspekt Finalrunden ist hier noch nicht abgebildet
 * für Liga wird als Schlüssel zusätzlich das Match definiert max. 7 Matches in einer Liga-Runde
 **/

CREATE TABLE match (
  -- composite key aus mehreren Fremd- und fachlichen Schlüsseln
  match_wettkampf_id   DECIMAL(19, 0) NOT NULL, --Fremdschlüsselbezug zum Wettkampf
  match_nr             DECIMAL(1, 0)  NOT NULL,
  match_begegnung      DECIMAL(1, 0)  NOT NULL,
  match_mannschaft_id  DECIMAL(19, 0) NULL, --Fremdschlüsselbezug zum Mannschaft

  match_scheibennummer DECIMAL(2, 0)  NOT NULL,
  match_matchpunkte    DECIMAL(1, 0)  NULL,
  match_satzpunkte     DECIMAL(1, 0)  NULL,
  match_strafpunkte_satz_1 DECIMAL(2, 0) NULL,
  match_strafpunkte_satz_2 DECIMAL(2, 0) NULL,
  match_strafpunkte_satz_3 DECIMAL(2, 0) NULL,
  match_strafpunkte_satz_4 DECIMAL(2, 0) NULL,
  match_strafpunkte_satz_5 DECIMAL(2, 0) NULL,

  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc       TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by           DECIMAL(19, 0) NOT NULL    DEFAULT 0,
  last_modified_at_utc TIMESTAMP      NULL        DEFAULT NULL,
  last_modified_by     DECIMAL(19, 0) NULL        DEFAULT NULL,
  version              DECIMAL(19, 0) NOT NULL    DEFAULT 0,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_match PRIMARY KEY (match_wettkampf_id, match_nr, match_begegnung, match_scheibennummer, match_mannschaft_id),

   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_match_wettkampf FOREIGN KEY (match_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
  ON DELETE CASCADE, -- das Löschen eines wettkampfs löscht auch die zugehörigen matches

  CONSTRAINT fk_match_mannschaft FOREIGN KEY (match_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
  ON DELETE CASCADE -- das Löschen einer mannschaft löscht auch die zugehörigen matches

);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_match_update_version
  BEFORE UPDATE ON match
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
