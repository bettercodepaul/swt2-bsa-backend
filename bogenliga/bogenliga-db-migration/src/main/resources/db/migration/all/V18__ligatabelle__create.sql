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

/**
 * Eine Klasse dient der Dokumentation der LigaTabelle zu Beginn der Liga (manuell) und nach
 * jedem Wettkampftag gem der Match und Satzpunkte
 **/
CREATE TABLE ligatabelle (
  ligatabelle_veranstaltung_id   DECIMAL(19,0) NOT NULL, --bezug zum Sportjahr
  ligatabelle_wettkampf_tag      DECIMAL(1,0) NOT NULL, -- Liga hat 4 Wettkampftage, initiale Ligatabelle für Tag 0
  ligatabelle_mannschaft_id      DECIMAL(19,0) NOT NULL, --bezug zur Mannschaft
  ligatabelle_tabellenplatz          DECIMAL(1,0) NOT NULL, -- resultierender Tabellenplatz nach dem Wettkampf
  ligatabelle_matchpkt               DECIMAL(4,0) NOT NULL, -- Summe aller eigene Matchpunkte
  ligatabelle_matchpkt_gegen         DECIMAL(4,0) NOT NULL, -- Summe aller gegnerischen Matchpunkte
  ligatabelle_satzpkt                DECIMAL(4,0) NOT NULL, -- Summe aller eigenen Satzpunkte
  ligatabelle_satzpkt_gegen          DECIMAL(4,0) NOT NULL, -- Summe aller gegnerischen Satzpunkte


  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
  last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
  last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
  version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,


  CONSTRAINT uc_ligatabelle UNIQUE (ligatabelle_veranstaltung_id, ligatabelle_wettkampf_tag, ligatabelle_mannschaft_id),


   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_ligatabelle_veranstaltung FOREIGN KEY (ligatabelle_veranstaltung_id) REFERENCES veranstaltung (veranstaltung_id)
    ON DELETE CASCADE, -- das Löschen eines wettkampftyps löscht auch die zugehörigen wettkämpfe

  CONSTRAINT fk_ligatabelle_mannschaft FOREIGN KEY (ligatabelle_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
    ON DELETE CASCADE -- das Löschen eines wettkampftyps löscht auch die zugehörigen wettkämpfe
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_ligatabelle_update_version
  BEFORE UPDATE ON ligatabelle
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
