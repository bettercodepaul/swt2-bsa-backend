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
CREATE SEQUENCE sq_ergebnis_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE ergebnis (
  ergebnis_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_ergebnis_id'), -- DECIMAL(19,0) = unsigned long
  ergebnis_wettkampf_id        DECIMAL(19,0)   NOT NULL,
  ergebnis_verein_id        DECIMAL(19,0)   NOT NULL,
  ergebnis_mannschaft_id        DECIMAL(19,0)   NOT NULL,
  ergebnis_schuetze_id        DECIMAL(19,0)   NOT NULL,
  ergebnis_summe       DECIMAL(19,0)   NULL,
  ergebnis_punkte        DECIMAL(19,0)   NULL,
  ergebnis_xer        DECIMAL(19,0)   NULL,
  ergebnis_10er        DECIMAL(19,0)   NULL,
  ergebnis_9er        DECIMAL(19,0)   NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_ergebnis_id PRIMARY KEY (ergebnis_id),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_ergebnis_wettkampf FOREIGN KEY (ergebnis_wettkampf_id) REFERENCES wettkampf (wettkampf_id),
  CONSTRAINT fk_ergebnis_verein FOREIGN KEY (ergebnis_verein_id) REFERENCES verein (verein_id),
  CONSTRAINT fk_ergebnis_mannschaft FOREIGN KEY (ergebnis_mannschaft_id) REFERENCES mannschaft (mannschaft_id),
  CONSTRAINT fk_ergebnis_schuetze FOREIGN KEY (ergebnis_schuetze_id) REFERENCES schuetze (schuetze_id)
);