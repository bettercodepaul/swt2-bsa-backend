-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_liga_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE liga (
  liga_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_liga_id'), -- DECIMAL(19,0) = unsigned long
  liga_region_id        DECIMAL(19,0)   NOT NULL, --Fremdschluessel zur Region
  liga_name             VARCHAR(200)    NOT NULL,
  liga_uebergeordnet    DECIMAL(19,0)   NULL,  -- Verweis auf die uebergeordnete Liga - bei Bundesliga (ganz oben) leer


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
  CONSTRAINT pk_liga_id PRIMARY KEY (liga_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_liga_name UNIQUE (liga_name),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_liga_region FOREIGN KEY (liga_region_id) REFERENCES region (region_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_liga_liga FOREIGN KEY (liga_uebergeordnet) REFERENCES liga (liga_id)
    ON DELETE CASCADE
);


-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_liga_update_version
  BEFORE UPDATE ON liga
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
