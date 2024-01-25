-- Add value and unique combination to table
ALTER TABLE altsystem_uebersetzung
    ADD COLUMN value VARCHAR,
    ADD CONSTRAINT unique_combination UNIQUE (kategorie, altsystem_id);