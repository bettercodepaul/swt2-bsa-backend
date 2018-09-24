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
CREATE SEQUENCE sq_wettkampfteilnehmer_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE wettkampfteilnehmer (
  wettkampfteilnehmer_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_wettkampfteilnehmer_id'), -- DECIMAL(19,0) = unsigned long
  wettkampfteilnehmer_wettkampf_id        DECIMAL(19,0)   NOT NULL,
  wettkampfteilnehmer_verein_id        DECIMAL(19,0)   NOT NULL,
  wettkampfteilnehmer_mannschaft_id        DECIMAL(19,0)   NOT NULL,
  wettkampfteilnehmer_schuetze_id        DECIMAL(19,0)   NOT NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_wettkampfteilnehmer_id PRIMARY KEY (wettkampfteilnehmer_id),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_wettkampfteilnehmer_wettkampf FOREIGN KEY (wettkampfteilnehmer_wettkampf_id) REFERENCES wettkampf (wettkampf_id),
  CONSTRAINT fk_wettkampfteilnehmer_verein FOREIGN KEY (wettkampfteilnehmer_verein_id) REFERENCES verein (verein_id),
  CONSTRAINT fk_wettkampfteilnehmer_mannschaft FOREIGN KEY (wettkampfteilnehmer_mannschaft_id) REFERENCES mannschaft (mannschaft_id),
  CONSTRAINT fk_wettkampfteilnehmer_schuetze FOREIGN KEY (wettkampfteilnehmer_schuetze_id) REFERENCES schuetze (schuetze_id)
);