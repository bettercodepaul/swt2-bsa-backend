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

/**
 * Einen Mannschaft legt die gemeldeten Schützen für eine Liga oder einen Wettkampf fest
 * aktuell werden die Anzahl der Schuetzen nicht limitiert
 * Mannschaften bestehen i.d.R. aus Vereinsname und Nummer
 * die Nummer wird durch die Rangfolge der Ligen definiert, in der höchsten Liga ist die Mannschaft 1
 **/

CREATE TABLE mannschaft (
  mannschaft_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_mannschaft_id'), -- DECIMAL(19,0) = unsigned long
  mannschaft_verein_id                 DECIMAL(19,0) NOT NULL,
  mannschaft_nummer         DECIMAL(2,0) NOT NULL,
  mannschaft_sportleiter    VARCHAR(200)  NOT NULL, -- der Ansprechpartner, Meldende der Mannschaft
  mannschaft_sportleiter_email  VARCHAR(200)  NOT NULL, -- email des Ansprechpartners
  mannschaft_veranstaltung_id          DECIMAL(19,0) NOT NULL,


  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_mannschaft_id PRIMARY KEY (mannschaft_id),


  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_mannschaft_verein FOREIGN KEY (mannschaft_verein_id) REFERENCES verein (verein_id)
    ON DELETE CASCADE, -- das Löschen eines Vereins löscht auch die Mannschaften des Vereins

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_mannschaft_veranstaltung FOREIGN KEY (mannschaft_veranstaltung_id) REFERENCES veranstaltung (veranstaltung_id)
    ON DELETE CASCADE -- das Löschen einer Veranstaltung löscht auch die dafür gemeldeten Mannschaften des Vereins

);
