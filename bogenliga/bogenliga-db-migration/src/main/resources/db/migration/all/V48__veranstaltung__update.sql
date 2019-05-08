--add new column veranstaltung_liga_id in TABLE veranstaltung with default value that every entry has a value and column is not null
ALTER TABLE veranstaltung
ADD COLUMN veranstaltung_liga_id DECIMAL(19,0) NOT NULL DEFAULT '2',
ADD CONSTRAINT fk_veranstaltung_liga_id FOREIGN KEY (veranstaltung_liga_id) REFERENCES liga (liga_id)
ON DELETE CASCADE; -- on deleting an entry in liga, also deletes all dependent veranstaltung

--remove the default value from the column
ALTER TABLE veranstaltung
ALTER COLUMN veranstaltung_liga_id
DROP DEFAULT;