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
 * Zuordnung von Schützen zu Mannnschaften

 * Es ist zu unterscheiden ob ein dsb_mitglied für eine Mannschaft gemeldet wurde
 * und ob er in dieser Mannschaft wirklich eingesetzt wurde.
 * ein dsb_mitglied kann in mehreren Mannschaften gemeldet sein und auch in diesen eingesetzt werden.
 * genauere Prüfung erfolgt fachlich bei der Zuordnung

 Wichtig: jedes Mannschaftsmitglied bekommt eine Nummer (auch eine "Startnummer" zum Befestigen an der Kleidung)
 diese Nummer ergibt sich aus der Reihenfolge der Mannschaftsmitglieder - es wird einfach durchnummeriert...
 daher sollten keine Mannschaftsmitglieder gelöscht werden - es gibt ja auch keine Grenze der Anzahl...
 Lesen der Mannschaftmitlgieder für Tabellen sollte daher als "Sort by" mannschaftsmitglied_id erfolgen.


 **/
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE mannschaftsmitglied_id START WITH 1000 INCREMENT BY 1;


CREATE TABLE mannschaftsmitglied (
  mannschaftsmitglied_id              DECIMAL(19, 0) NOT NULL    DEFAULT nextval(
      'mannschaftsmitglied_id'), -- DECIMAL(19,0) = unsigned long
  mannschaftsmitglied_mannschaft_id                 DECIMAL(19,0) NOT NULL,
  mannschaftsmitglied_dsb_mitglied_id               DECIMAL(19,0) NOT NULL,
  mannschaftsmitglied_dsb_mitglied_eingesetzt       INTEGER       NOT NULL, -- 0= kein Einsatz, >0 #Einsatze


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
  CONSTRAINT pk_mannschaftsmitglied_id PRIMARY KEY (mannschaftsmitglied_id),

 -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_mannschaftsmitglied_mannschaft FOREIGN KEY (mannschaftsmitglied_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
    ON DELETE CASCADE, -- das Löschen einer Mannschaft löscht auch die Zuordnung der dsb_mitgliedn zur Mannschaft

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_mannschaftsmitglied_dsb_mitglied FOREIGN KEY (mannschaftsmitglied_dsb_mitglied_id) REFERENCES dsb_mitglied (dsb_mitglied_id)
    ON DELETE CASCADE -- das Löschen eines dsb_mitglieds löscht auch die Zuordnung des dsb_mitglieds zu Mannschaften

);


-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_mannschaftsmitglied_update_version
  BEFORE UPDATE ON mannschaftsmitglied
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
