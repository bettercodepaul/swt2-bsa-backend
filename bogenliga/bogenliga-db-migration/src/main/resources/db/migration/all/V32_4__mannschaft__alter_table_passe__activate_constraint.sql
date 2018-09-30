-- Füge eine Spalte für die match_mannschaft_id der neuen Tabelle mannschaft zur existierenden match Tabelle hinzu.
-- Die match_mannschaft_id ist ein Pflichtfeld und hat wegen der Fremdschlüsselbeziehung keinen default Wert.
-- 1. Die Spalte erst ohne Constraints angelegt werden (V31_2__mannschaft__alter_table_passe__add_column.sql).
-- 2. Sinnvolle Werte per UPDATE eingefügt werden (V32_1__mannschaft__update_table_match__add_mandatory_data.sql)
-- 3. Die Constraints der Spalte aktiviert werden (V32_2__mannschaft__alter_table_match__activate_constraint.sql).

ALTER TABLE passe
  ALTER COLUMN passe_mannschaft_id SET NOT NULL -- add not null constraint
;

-- Remove PRIMARY KEY attribute of former PRIMARY KEY
ALTER TABLE passe
  DROP CONSTRAINT pk_passe;

-- Set the new PRIMARY KEY
ALTER TABLE passe
  ADD PRIMARY KEY (passe_wettkampf_id, passe_match_nr, passe_mannschaft_id, passe_lfdnr, passe_dsb_mitglied_id);

-- Drop old primary key
ALTER TABLE passe
  DROP COLUMN passe_id;

-- Drop unused sequence
DROP SEQUENCE sq_passe_id;