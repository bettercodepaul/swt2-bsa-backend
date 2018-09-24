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
CREATE SEQUENCE sq_passe_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE passe (
  passe_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_passe_id'), -- DECIMAL(19,0) = unsigned long
  passe_satz_id       DECIMAL(19,0)   NULL, -- TODO null constraints checken
  passe_mannschaftsmitglied_id       DECIMAL(19,0)   NULL,
  passe_summe        DECIMAL(19,0)  NULL,
  passe_pfeil_1        DECIMAL(19,0)  NULL,
  passe_pfeil_2        DECIMAL(19,0)  NULL,
  passe_pfeil_3        DECIMAL(19,0)  NULL,
  passe_pfeil_4        DECIMAL(19,0)  NULL,
  passe_pfeil_5        DECIMAL(19,0)  NULL,
  passe_pfeil_6        DECIMAL(19,0)  NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_passe_id PRIMARY KEY (passe_id),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_passe_satz FOREIGN KEY (passe_satz_id) REFERENCES satz (satz_id),
  CONSTRAINT fk_passe_mannschaftsmitglied FOREIGN KEY (passe_mannschaftsmitglied_id) REFERENCES mannschaftsmitglied (mannschaftsmitglied_id)
);