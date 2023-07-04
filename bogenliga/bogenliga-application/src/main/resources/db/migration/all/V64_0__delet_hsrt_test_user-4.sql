DELETE FROM benutzer
WHERE benutzer_id = 13
AND EXISTS (
  SELECT 1 FROM benutzer WHERE benutzer_id = 13
);

DELETE FROM benutzer_rolle
WHERE benutzer_rolle_benutzer_id = 13 AND benutzer_rolle_rolle_id = 2
AND EXISTS (
  SELECT 1 FROM benutzer_rolle WHERE benutzer_rolle_benutzer_id = 13 AND benutzer_rolle_rolle_id = 2
);
