--update test data in table veranstaltung

UPDATE veranstaltung

-- Landesliga Nord
SET veranstaltung_liga_id = 3
WHERE veranstaltung_id = 1;

-- Landesliga Sued
SET veranstaltung_liga_id = 4
WHERE veranstaltung_id = 2;
