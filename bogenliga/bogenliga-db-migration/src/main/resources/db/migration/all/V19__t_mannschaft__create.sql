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
CREATE SEQUENCE sq_mannschaft_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE mannschaft (
  mannschaft_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_mannschaft_id'), -- DECIMAL(19,0) = unsigned long
  mannschaft_liga_id        DECIMAL(19,0)   NOT NULL,
  mannschaft_disziplin_id        DECIMAL(19,0)   NOT NULL,
  mannschaft_sportjahr           VARCHAR(200)  NOT NULL,
  mannschaft_name           VARCHAR(200)  NOT NULL,
  mannschaft_sportleiter_id  DECIMAL(19,0)   NULL, -- TODO verweist auf Schütze ID, sollte dann not null sein?

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_mannschaft_id PRIMARY KEY (mannschaft_id),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_mannschaft_liga FOREIGN KEY (mannschaft_liga_id) REFERENCES liga (liga_id),
  CONSTRAINT fk_mannschaft_disziplin FOREIGN KEY (mannschaft_disziplin_id) REFERENCES disziplin (disziplin_id)
);