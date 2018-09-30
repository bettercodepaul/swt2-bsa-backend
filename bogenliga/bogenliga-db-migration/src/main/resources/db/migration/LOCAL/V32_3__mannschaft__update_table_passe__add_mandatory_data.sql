-- Füge eine Spalte für die match_mannschaft_id der neuen Tabelle mannschaft zur existierenden match Tabelle hinzu.
-- Die match_mannschaft_id ist ein Pflichtfeld und hat wegen der Fremdschlüsselbeziehung keinen default Wert.
-- 1. Die Spalte erst ohne Constraints angelegt werden (V31_2__mannschaft__alter_table_passe__add_column.sql).
-- 2. Sinnvolle Werte per UPDATE eingefügt werden (V32_1__mannschaft__update_table_match__add_mandatory_data.sql)
-- 3. Die Constraints der Spalte aktiviert werden (V32_2__mannschaft__alter_table_match__activate_constraint.sql).

UPDATE passe
  SET passe_mannschaft_id = 101
  WHERE passe_wettkampf_id = 30
    AND (passe_dsb_mitglied_id = 28 OR passe_dsb_mitglied_id = 31 OR passe_dsb_mitglied_id = 47)
;

UPDATE passe
SET passe_mannschaft_id = 102
WHERE passe_wettkampf_id = 30
  AND (passe_dsb_mitglied_id = 441 OR passe_dsb_mitglied_id = 393 OR passe_dsb_mitglied_id = 385)
;

UPDATE passe
SET passe_mannschaft_id = 103
WHERE passe_wettkampf_id = 30
  AND (passe_dsb_mitglied_id = 71 OR passe_dsb_mitglied_id = 80 OR passe_dsb_mitglied_id = 103)
;

UPDATE passe
SET passe_mannschaft_id = 104
WHERE passe_wettkampf_id = 30
  AND (passe_dsb_mitglied_id = 193 OR passe_dsb_mitglied_id = 189 OR passe_dsb_mitglied_id = 249)
;

UPDATE passe
SET passe_mannschaft_id = 105
WHERE passe_wettkampf_id = 30
  AND (passe_dsb_mitglied_id = 45 OR passe_dsb_mitglied_id = 72 OR passe_dsb_mitglied_id = 104)
;

UPDATE passe
SET passe_mannschaft_id = 106
WHERE passe_wettkampf_id = 30
  AND (passe_dsb_mitglied_id = 44 OR passe_dsb_mitglied_id = 78 OR passe_dsb_mitglied_id = 110)
;

UPDATE passe
SET passe_mannschaft_id = 107
WHERE passe_wettkampf_id = 30
  AND (passe_dsb_mitglied_id = 216 OR passe_dsb_mitglied_id = 190 OR passe_dsb_mitglied_id = 240)
;


UPDATE passe
SET passe_mannschaft_id = 108
WHERE passe_wettkampf_id = 30
  AND (passe_dsb_mitglied_id = 180 OR passe_dsb_mitglied_id = 220 OR passe_dsb_mitglied_id = 127)
;
