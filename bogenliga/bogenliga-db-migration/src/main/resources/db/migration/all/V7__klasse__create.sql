-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_klasse_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Klasse dient der Defintion von Gruppen gleichen Alters
 * Relevant ist der Jahrgang und das daraus resultierende Alter des Schützen.
 **/
CREATE TABLE klasse (
  klasse_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_klasse_id'), -- DECIMAL(19,0) = unsigned long
  klasse_name           VARCHAR(200)  NOT NULL, -- gem. Vorgaben DSB
  klasse_alter_min      DECIMAL(2,0)  NOT NULL,
  klasse_alter_max      DECIMAL(3,0)  NOT NULL,
  klasse_nr             DECIMAL(2,0)  NOT NULL, -- Nummer der Klasse im DSB


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
  CONSTRAINT pk_klasse_id PRIMARY KEY (klasse_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_klasse_name UNIQUE (klasse_name),
  CONSTRAINT uc_klasse_nr UNIQUE (klasse_nr)

);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_klasse_update_version
  BEFORE UPDATE ON klasse
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
