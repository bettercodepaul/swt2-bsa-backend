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
CREATE SEQUENCE sq_region_id START WITH 1000 INCREMENT BY 1;


/**
 * Eine Region dient der räumlichen Einteilung des Deutscher Schützenbundes (DSB).
 * Regionen sind hierarisch in einer Baumstruktur eingeordnet.
 * Der DSB bildet den Root-Knoten.
 **/
CREATE TABLE region (
  region_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_region_id'), -- DECIMAL(19,0) = unsigned long
  region_name           VARCHAR(200)  NOT NULL,
  region_kuerzel        VARCHAR(10)   NOT NULL,
  region_typ            VARCHAR(200)  NOT NULL,
  region_uebergeordnet  DECIMAL(19,0) NULL,  -- Verweis auf die uebergeordnete Region - bei DSB (ganz oben) Bezug leer

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
  CONSTRAINT pk_region_id PRIMARY KEY (region_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_region_name UNIQUE (region_name),
  CONSTRAINT uc_region_kuerzel UNIQUE (region_kuerzel),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_region_region FOREIGN KEY (region_uebergeordnet) REFERENCES region (region_id)
    ON DELETE SET NULL -- das Löschen einer Region würde alle untergeordneten Regionen, Vereine und Schützen entfernen
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_region_update_version
  BEFORE UPDATE ON region
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
