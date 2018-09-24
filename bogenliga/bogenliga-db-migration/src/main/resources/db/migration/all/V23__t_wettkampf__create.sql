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
CREATE SEQUENCE sq_wettkampf_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE wettkampf (
  wettkampf_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_wettkampf_id'), -- DECIMAL(19,0) = unsigned long
  wettkampf_veranstalter         VARCHAR(200)    NOT NULL,
  wettkampf_ausrichter_id        DECIMAL(19,0)   NOT NULL,
  wettkampf_disziplin_id        DECIMAL(19,0)   NOT NULL,
  wettkampf_liga_id        DECIMAL(19,0)   NOT NULL,
  wettkampf_termin         VARCHAR(200)    NOT NULL, -- TODO format

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_wettkampf_id PRIMARY KEY (wettkampf_id),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_wettkampf_verein FOREIGN KEY (wettkampf_ausrichter_id) REFERENCES verein (verein_id),
  CONSTRAINT fk_wettkampf_disziplin FOREIGN KEY (wettkampf_disziplin_id) REFERENCES disziplin (disziplin_id),
  CONSTRAINT fk_wettkampf_liga_id FOREIGN KEY (wettkampf_liga_id) REFERENCES liga (liga_id)
);