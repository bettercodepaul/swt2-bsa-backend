
-- Assuming the primary key for the 'benutzer' table is 'benutzer_id'
DELETE FROM benutzer WHERE benutzer_id = 13;

-- Assuming the primary key for the 'benutzer_rolle' table is 'benutzer_rolle_benutzer_id' and 'benutzer_rolle_rolle_id'
DELETE FROM benutzer_rolle WHERE benutzer_rolle_benutzer_id = 13 AND benutzer_rolle_rolle_id = 2;
