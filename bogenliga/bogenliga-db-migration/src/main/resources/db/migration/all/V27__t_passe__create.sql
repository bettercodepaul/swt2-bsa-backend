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
 * Eine Klasse dient der erfassung der Ergebnisse im Wettkampf
 * wir erwarten entweder 2,3 oder 6 Pfeilwerte für einen Schuetzen
 **/
CREATE TABLE passe (
  passe_lfdnr              DECIMAL(4,0),
  passe_wettkampf_id       DECIMAL(19,0) NOT NULL, --Fremdschlüsselbezug zum Wettkampf
  passe_match_nr           DECIMAL(1,0),
  passe_mannschaft_id      DECIMAL(19,0) NOT NULL, --Fremdschlüsselbezug zur mannschaft
  passe_schuetze_id        DECIMAL(19,0) NOT NULL, --Fremdschlüsselbezug zum Schuetzen
  passe_ringzahl_pfeil1    DECIMAL(2,0) -- die geschossenen Ringe
  passe_ringzahl_pfeil2    DECIMAL(2,0) -- die geschossenen Ringe
  passe_ringzahl_pfeil3    DECIMAL(2,0) -- die geschossenen Ringe
  passe_ringzahl_pfeil4    DECIMAL(2,0) -- die geschossenen Ringe
  passe_ringzahl_pfeil5    DECIMAL(2,0) -- die geschossenen Ringe
  passe_ringzahl_pfeil6    DECIMAL(2,0) -- die geschossenen Ringe

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_passe PRIMARY KEY (passe_lfdnr, passe_pfeilnummer, passe_wettkampf_id),


   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_passe_wettkampf FOREIGN KEY (passe_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
    ON DELETE CASCADE, -- das Löschen eines wettkampfs löscht auch die zugehörigen Passen
  CONSTRAINT fk_passe_mannschaft FOREIGN KEY (passe_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
    ON DELETE CASCADE, -- das Löschen einer Mannschaft löscht auch dessen Pfeilwerte
  CONSTRAINT fk_passe_schuetze FOREIGN KEY (passe_schuetze_id) REFERENCES schuetze (schuetze_id)
    ON DELETE CASCADE -- das Löschen eines Schuetzen löscht auch dessen Pfeilwerte
);
