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
 * Zuordnung von Schptzen zu Mannnschaften
 * es ist zu unterscheiden ob ein Schuetze für eine Mannschaft gemeldet wurde
 * und ob er in dieser Mannschaft wirklich eingesetz wurde
 * ein Schuetze kann in mehreren Mannschaften gemeldet sein und auch in diesen eingesetzt werden.
 * genauere Prüfung erfolgt fachlich bei der Zuordnung
 **/

CREATE TABLE mannschaftsmitglied (
  mannschaftsmitglied_mannschaft_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_lizenz_id'), -- DECIMAL(19,0) = unsigned long
  mannschaftsmitglied_schuetze_id               DECIMAL(19,0) NOT NULL,
  mannschaftsmitglied_schuetze_eingesetzt       DECIMAL (1,0) NOT NULL, -- 0= kein Einsatz, 1= Einsatz/ hat geschossen

   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_mannschaftsmitglied_mannschaft FOREIGN KEY (mannschaftsmitglied_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
    ON DELETE CASCADE, -- das Löschen einer Mannschaft löscht auch die Zuordnung der Schuetzen zur Mannschaft

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_mannschaftsmitglied_schuetze FOREIGN KEY (mannschaftsmitglied_schuetze_id) REFERENCES schuetze (schuetze_id)
    ON DELETE CASCADE -- das Löschen eines Schuetzen löscht auch die Zuordnung des Schuetzen zu Mannschaften

);

