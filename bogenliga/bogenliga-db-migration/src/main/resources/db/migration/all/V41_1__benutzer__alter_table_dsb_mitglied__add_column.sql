-- Füge eine Spalte für die benutzer_id der neuen Tabelle benutzer zur existierenden dsb_mitglied Tabelle hinzu
-- Der default Wert muss zwingend beachtet werden! Falls es ein Pflichtfeld ist und es keinen Defaultwert gibt, muss
-- 1. Die Spalte erst ohne Constraints angelegt werden
-- 2. Sinnvolle Werte per UPDATE eingefügt werden
-- 3. Die Constraints der Spalte aktiviert werden.
ALTER TABLE dsb_mitglied
  ADD COLUMN dsb_mitglied_benutzer_id      DECIMAL(19,0)   NULL,

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  ADD CONSTRAINT fk_dsb_mitglied_benutzer_id FOREIGN KEY (dsb_mitglied_benutzer_id) REFERENCES benutzer (benutzer_id)
    ON DELETE SET NULL
;