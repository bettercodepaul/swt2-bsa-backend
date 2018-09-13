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
CREATE SEQUENCE sq_klasse_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Klasse dient der defintion von Gruppen gleichen Alters - relevant ist der Jahrgang
 * und daraus resultierend das Alter des Schützen.
 **/
-- TODO Kann auch Verband heißen. Siehe V4 Testdaten
CREATE TABLE klasse (
  klasse_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_region_id'), -- DECIMAL(19,0) = unsigned long
  klasse_name           VARCHAR(200)  NOT NULL, -- gem. Vorgaben DSB
  klasse_alter_min      DECIMAL(2,0)  NOT NULL,
  klasse_alter_max      DECIMAL(2,0)  NOT NULL,
  klasse_nr             DECIMAL(2,0)  NOT NULL, -- Nummer der Klasse im DSB

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_region_id PRIMARY KEY (region_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_klasse_name UNIQUE (klasse_name),
  CONSTRAINT uc_klasse_nr UNIQUE (klasse_nr)

);
