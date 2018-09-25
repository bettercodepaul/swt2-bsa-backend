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

/**
 * Eine Klasse dient der Defintion von Runden im Wettkampf - sie hat nur eine laufende Nummer als ID
 * wir erwarten entweder 2 (WA-Wettkampf) oder 7 Runden (Liga),
 * der Aspekt Finalrunden ist hier noch nicht abgebildet
 * für Liga wird als Schlüssel zusätzlich das Match definiert max. 7 Matches in einer Liga-Runde
 **/
CREATE TABLE match (
  match_nr                 DECIMAL(1,0) NOT NULL
  match_wettkampf_id       DECIMAL(19,0) NOT NULL, --Fremdschlüsselbezug zum Wettkampf
  match_begegnung          DECIMAL(1,0) NOT NULL,
  match_mannschaft_id      DECIMAL(19,0) NOT NULL, --Fremdschlüsselbezug zum Mannschaft
  match_scheibennummer     DECIMAL(2,0) NOT NULL,
  match_matchpunkte        DECIMAL(1,0) NOT NULL,
  match_satzpunkte         DECIMAL(1,0) NOT NULL,


  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_match PRIMARY KEY (match_nr, runde_wettkampf_id, runde_begegnung),


   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_runde_wettkampf FOREIGN KEY (runde_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
    ON DELETE CASCADE, -- das Löschen eines wettkampfs löscht auch die zugehörigen runden
  CONSTRAINT fk_runde_mannschaft FOREIGN KEY (runde_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
    ON DELETE CASCADE -- das Löschen einer mannschaft löscht auch die zugehörigen runden

);
