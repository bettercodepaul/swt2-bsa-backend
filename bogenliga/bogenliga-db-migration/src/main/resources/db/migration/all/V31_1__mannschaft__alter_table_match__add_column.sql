-- Füge eine Spalte für die match_mannschaft_id der neuen Tabelle mannschaft zur existierenden match Tabelle hinzu.
-- Die match_mannschaft_id ist ein Pflichtfeld und hat wegen der Fremdschlüsselbeziehung keinen default Wert.
-- 1. Die Spalte erst ohne Constraints angelegt werden (V31_1__mannschaft__alter_table_match__add_column.sql).
-- 2. Sinnvolle Werte per UPDATE eingefügt werden (V32_1__mannschaft__update_table_match__add_mandatory_data.sql)
-- 3. Die Constraints der Spalte aktiviert werden (V32_2__mannschaft__alter_table_match__activate_constraint.sql).

ALTER TABLE match
  ADD COLUMN match_mannschaft_id      DECIMAL(19,0)  NULL, --Fremdschlüsselbezug zum Mannschaft

  ADD CONSTRAINT fk_match_mannschaft FOREIGN KEY (match_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
    ON DELETE CASCADE -- das Löschen einer mannschaft löscht auch die zugehörigen matches
;

