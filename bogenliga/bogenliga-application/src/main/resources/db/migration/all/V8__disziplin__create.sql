-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_disziplin_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Disziplin dient der Defintion von Bogentypen für die die gleichen Anforderung gelten.
 **/
CREATE TABLE disziplin (
  disziplin_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_disziplin_id'), -- DECIMAL(19,0) = unsigned long
  disziplin_name           VARCHAR(200)  NOT NULL, -- gem. Vorgaben DSB

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
  CONSTRAINT pk_disziplin_id PRIMARY KEY (disziplin_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_disziplin_name UNIQUE (disziplin_name)
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_disziplin_update_version
  BEFORE UPDATE ON disziplin
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
