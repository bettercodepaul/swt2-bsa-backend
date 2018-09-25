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
CREATE SEQUENCE sq_disziplin_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Klasse dient der defintion Typen für welche die gleichen Anforderung bzgl. der eingesetzen Sportgeräte gelten.
 **/
-- TODO Kann auch Verband heißen. Siehe V4 Testdaten
CREATE TABLE disziplin (
  disziplin_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_disziplin_id'), -- DECIMAL(19,0) = unsigned long
  disziplin_name           VARCHAR(200)  NOT NULL, -- gem. Vorgaben DSB

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_disziplin_id PRIMARY KEY (disziplin_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_disziplin_name UNIQUE (disziplin_name)

);
