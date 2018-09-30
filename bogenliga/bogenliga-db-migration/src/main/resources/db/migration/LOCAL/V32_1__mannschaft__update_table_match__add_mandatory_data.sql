-- Füge eine Spalte für die match_mannschaft_id der neuen Tabelle mannschaft zur existierenden match Tabelle hinzu.
-- Die match_mannschaft_id ist ein Pflichtfeld und hat wegen der Fremdschlüsselbeziehung keinen default Wert.
-- 1. Die Spalte erst ohne Constraints angelegt werden (V31_1__mannschaft__alter_table_match__add_column.sql).
-- 2. Sinnvolle Werte per UPDATE eingefügt werden (V32_1__mannschaft__update_table_match__add_mandatory_data.sql)
-- 3. Die Constraints der Spalte aktiviert werden (V32_2__mannschaft__alter_table_match__activate_constraint.sql).

UPDATE match
  SET match_mannschaft_id = 101
  WHERE match_wettkampf_id = 30 AND
    (match_nr = 1 AND match_scheibennummer = 1)
    OR
    (match_nr = 2 AND match_scheibennummer = 4)
    OR
    (match_nr = 3 AND match_scheibennummer = 1)
    OR
    (match_nr = 4 AND match_scheibennummer = 6)
;

UPDATE match
SET match_mannschaft_id = 102
WHERE match_wettkampf_id = 30 AND
      (match_nr = 1 AND match_scheibennummer = 2)
   OR
      (match_nr = 2 AND match_scheibennummer = 1)
   OR
      (match_nr = 3 AND match_scheibennummer = 4)
   OR
      (match_nr = 4 AND match_scheibennummer = 1)
;

UPDATE match
SET match_mannschaft_id = 103
WHERE match_wettkampf_id = 30 AND
      (match_nr = 1 AND match_scheibennummer = 3)
   OR
      (match_nr = 2 AND match_scheibennummer = 6)
   OR
      (match_nr = 3 AND match_scheibennummer = 2)
   OR
      (match_nr = 4 AND match_scheibennummer = 8)
;


UPDATE match
SET match_mannschaft_id = 104
WHERE match_wettkampf_id = 30 AND
      (match_nr = 1 AND match_scheibennummer = 4)
   OR
      (match_nr = 2 AND match_scheibennummer = 8)
   OR
      (match_nr = 3 AND match_scheibennummer = 3)
   OR
      (match_nr = 4 AND match_scheibennummer = 4)
;

UPDATE match
SET match_mannschaft_id = 105
WHERE match_wettkampf_id = 30 AND
      (match_nr = 1 AND match_scheibennummer = 5)
   OR
      (match_nr = 2 AND match_scheibennummer = 5)
   OR
      (match_nr = 3 AND match_scheibennummer = 6)
   OR
      (match_nr = 4 AND match_scheibennummer = 2)
;

UPDATE match
SET match_mannschaft_id = 106
WHERE match_wettkampf_id = 30 AND
      (match_nr = 1 AND match_scheibennummer = 6)
   OR
      (match_nr = 2 AND match_scheibennummer = 3)
   OR
      (match_nr = 3 AND match_scheibennummer = 8)
   OR
      (match_nr = 4 AND match_scheibennummer = 3)
;

UPDATE match
SET match_mannschaft_id = 107
WHERE match_wettkampf_id = 30 AND
      (match_nr = 1 AND match_scheibennummer = 7)
   OR
      (match_nr = 2 AND match_scheibennummer = 2)
   OR
      (match_nr = 3 AND match_scheibennummer = 5)
   OR
      (match_nr = 4 AND match_scheibennummer = 5)
;

UPDATE match
SET match_mannschaft_id = 108
WHERE match_wettkampf_id = 30 AND
      (match_nr = 1 AND match_scheibennummer = 8)
   OR
      (match_nr = 2 AND match_scheibennummer = 7)
   OR
      (match_nr = 3 AND match_scheibennummer = 7)
   OR
      (match_nr = 4 AND match_scheibennummer = 7)
;

