-- Füge eine Spalte für die match_mannschaft_id der neuen Tabelle mannschaft zur existierenden match Tabelle hinzu.
-- Die match_mannschaft_id ist ein Pflichtfeld und hat wegen der Fremdschlüsselbeziehung keinen default Wert.
-- 1. Die Spalte erst ohne Constraints angelegt werden (V31_1__mannschaft__alter_table_match__add_column.sql).
-- 2. Sinnvolle Werte per UPDATE eingefügt werden (V32_1__mannschaft__update_table_match__add_mandatory_data.sql)
-- 3. Die Constraints der Spalte aktiviert werden (V32_2__mannschaft__alter_table_match__activate_constraint.sql).

ALTER TABLE match
  ALTER COLUMN match_mannschaft_id SET NOT NULL -- add not null constraint
;

-- Firstly, remove PRIMARY KEY attribute of former PRIMARY KEY
ALTER TABLE match
  DROP CONSTRAINT pk_match;

-- Lastly set the new PRIMARY KEY
ALTER TABLE match ADD PRIMARY KEY (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id);
