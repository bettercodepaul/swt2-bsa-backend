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
CREATE SEQUENCE sq_verein_id START WITH 1000 INCREMENT BY 1;

/**
 * Ein Verein besitzt einen Namen und eine ID beim DSB.
 * Ein Verein ist genau einer Region zugeordnet.
 * Es muss sichergestellt werden, dass der Verein mit einer Region auf Kreis-Ebene verknüpft ist.
 **/
CREATE TABLE verein (
  verein_id             DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_verein_id'), -- DECIMAL(19,0) = unsigned long
  verein_name           VARCHAR(200)    NOT NULL,
  verein_dsb_identifier VARCHAR(200)    NOT NULL, -- fachlicher Schluessel: Identifikator beim DBS
  verein_region_id      DECIMAL(19,0)   NOT NULL, -- Fremdschluessel zur Regionszuordnung auf Ebene "Kreis"

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
  CONSTRAINT pk_verein_id PRIMARY KEY (verein_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_verein_name UNIQUE (verein_name),

  CONSTRAINT uc_verein_dsb_identifier UNIQUE (verein_dsb_identifier),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_verein_region FOREIGN KEY (verein_region_id) REFERENCES region (region_id)
    ON DELETE SET NULL -- das Loeschen einer Region wuerde alle Vereine und deren dsb_mitgliedn entfernen
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_verein_update_version
  BEFORE UPDATE ON verein
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
