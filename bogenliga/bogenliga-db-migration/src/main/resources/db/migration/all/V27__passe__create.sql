-- !!!ACHTUNG: Beispiel für ein Refactoring einer existierenden Tabelle!!!
CREATE SEQUENCE sq_passe_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Passe dient der Erfassung der Ergebnisse im Wettkampf
 * wir erwarten entweder 2, 3 oder 6 Pfeilwerte für ein dsb_mitglied
 * Die Tabelle ist daher dünn besetzt.
 *
 * Die Passe ist eine schwache Entität und basiert auf dem Wettkampf, dem Match und dem Mannschaftsmitglied.
 * Die Spalten sind denormalisiert, um den Zugriff zu beschleunigen (weniger Joins)
 **/
CREATE TABLE passe (

  -- !!!ACHTUNG: Beispiel für ein Refactoring einer existierenden Tabelle!!!
  -- Es wird gezeigt, wie nachträglich DB-Änderungen in ein produktiv betriebenes System eingespielt werden können.
  -- Dies ist Teil des "Continious Integration" Prozesses.
  -- Die Tabelle entspricht nach der Datenmigration nicht mehr diesem CREATE TABLE Skript.
  -- Bitte das Ergebnis in der DB anschauen.
  -- V31_2__mannschaft__alter_table_passe__add_column.sql).
  -- V32_1__mannschaft__update_table_match__add_mandatory_data.sql)
  -- V32_2__mannschaft__alter_table_match__activate_constraint.sql).
  passe_id                 DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_passe_id'),


  -- denormalisiert
  passe_wettkampf_id       DECIMAL(19,0) NOT NULL, --Fremdschlüsselbezug zum wettkampf
  passe_match_nr           DECIMAL(1,0)  NOT NULL,
  passe_lfdnr              DECIMAL(4,0)  NOT NULL,
  passe_dsb_mitglied_id    DECIMAL(19,0) NOT NULL, --Fremdschlüsselbezug zum dsb_mitglied

  passe_ringzahl_pfeil1    DECIMAL(2,0), -- die geschossenen Ringe. Werte: [0, 10]
  passe_ringzahl_pfeil2    DECIMAL(2,0), -- die geschossenen Ringe. Werte: [0, 10]
  passe_ringzahl_pfeil3    DECIMAL(2,0), -- die geschossenen Ringe. Werte: [0, 10]
  passe_ringzahl_pfeil4    DECIMAL(2,0), -- die geschossenen Ringe. Werte: [0, 10]
  passe_ringzahl_pfeil5    DECIMAL(2,0), -- die geschossenen Ringe. Werte: [0, 10]
  passe_ringzahl_pfeil6    DECIMAL(2,0), -- die geschossenen Ringe. Werte: [0, 10]


  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
  last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
  last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
  version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,


  -- !!!ACHTUNG: Beispiel für ein Refactoring einer existierenden Tabelle!!!
  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_passe PRIMARY KEY (passe_id),

   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_passe_wettkampf FOREIGN KEY (passe_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
    ON DELETE CASCADE, -- das Löschen eines Wettkampfs löscht auch die zugehörigen Passen
  CONSTRAINT fk_passe_dsb_mitglied FOREIGN KEY (passe_dsb_mitglied_id) REFERENCES dsb_mitglied (dsb_mitglied_id)
    ON DELETE CASCADE -- das Löschen eines dsb_mitglieds löscht auch dessen Pfeilwerte
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V0__row_version_update.sql
CREATE TRIGGER tr_passe_update_version
  BEFORE UPDATE ON passe
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
