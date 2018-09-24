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
CREATE SEQUENCE sq_satz_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE satz (
  satz_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_satz_id'), -- DECIMAL(19,0) = unsigned long
  satz_match_id       DECIMAL(19,0)   NULL, -- TODO null constraints checken
  satz_nummer       DECIMAL(19,0)   NULL,
  satz_punkte      DECIMAL(19,0)  NULL,
  satz_summe        DECIMAL(19,0)  NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_satz_id PRIMARY KEY (satz_id),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_satz_match FOREIGN KEY (satz_match_id) REFERENCES match (match_id)
);