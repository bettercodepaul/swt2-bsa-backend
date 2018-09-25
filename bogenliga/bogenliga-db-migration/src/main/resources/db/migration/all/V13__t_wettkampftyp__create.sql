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
CREATE SEQUENCE sq_wettkampftyp_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Klasse dient der defintion von wettkampftypen - Wettkämpfen mit identischen Regeln
 **/
-- TODO Kann auch Verband heißen. Siehe V4 Testdaten
CREATE TABLE wettkampftyp (
  wettkampftyp_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_wettkampftyp_id'), -- DECIMAL(19,0) = unsigned long
  wettkampftyp_name           VARCHAR(200)  NOT NULL, -- gem. Vorgaben DSB

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_wettkampftyp_id PRIMARY KEY (wettkampftyp_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_wettkampftyp_name UNIQUE (wettkampftyp_name)

);
