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
CREATE SEQUENCE sq_mannschaftsmitglied_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE mannschaftsmitglied (
  mannschaftsmitglied_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_mannschaftsmitglied_id'), -- DECIMAL(19,0) = unsigned long
  mannschaftsmitglied_mannschaft_id        DECIMAL(19,0)   NOT NULL,
  mannschaftsmitglied_teammitglied_nr        DECIMAL(19,0)   NOT NULL,
  mannschaftsmitglied_schuetze_id        DECIMAL(19,0)   NOT NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_mannschaftsmitglied_id PRIMARY KEY (mannschaftsmitglied_id),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_mannschaftsmitglied_mannschaft FOREIGN KEY (mannschaftsmitglied_mannschaft_id) REFERENCES mannschaft (mannschaft_id),
  CONSTRAINT fk_mannschaftsmitglied_schuetze  FOREIGN KEY (mannschaftsmitglied_schuetze_id) REFERENCES schuetze (schuetze_id)
);