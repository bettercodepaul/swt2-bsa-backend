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
CREATE SEQUENCE sq_match_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE match (
  match_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_match_id'), -- DECIMAL(19,0) = unsigned long
  match_runde        DECIMAL(19,0)   NULL, -- TODO null constraints checken
  match_nummer       DECIMAL(19,0)   NULL,
  match_wettkampf_id        DECIMAL(19,0)  NULL,
  match_ergebnis_id        DECIMAL(19,0)  NULL,
  match_mannschaft_id        DECIMAL(19,0) NULL,
  match_gegnermannschaft_id        DECIMAL(19,0)  NULL,
  match_punkte       DECIMAL(19,0)   NULL,
  match_summe        DECIMAL(19,0)   NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_match_id PRIMARY KEY (match_id),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_match_wettkampf FOREIGN KEY (match_wettkampf_id) REFERENCES wettkampf (wettkampf_id),
  CONSTRAINT fk_match_ergebnis FOREIGN KEY (match_ergebnis_id) REFERENCES ergebnis (ergebnis_id),
  CONSTRAINT fk_match_mannschaft FOREIGN KEY (match_mannschaft_id) REFERENCES mannschaft (mannschaft_id),
  CONSTRAINT fk_match_gegnermannschaft FOREIGN KEY (match_gegnermannschaft_id) REFERENCES mannschaft (mannschaft_id)
);