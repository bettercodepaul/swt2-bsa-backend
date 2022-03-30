--update test data in table veranstaltung

-- Wuerttembergliga
UPDATE veranstaltung
SET veranstaltung_liga_id = 2
WHERE veranstaltung_id = 0;

-- Landesliga Nord
UPDATE veranstaltung
SET veranstaltung_liga_id = 3
WHERE veranstaltung_id = 1;

-- Landesliga Sued
UPDATE veranstaltung
SET veranstaltung_liga_id = 4
WHERE veranstaltung_id = 2;

-- Relegation Landesliga Nord
UPDATE veranstaltung
SET veranstaltung_liga_id = 3,
    veranstaltung_wettkampftyp_id = 0
WHERE veranstaltung_id = 3;

-- Relegation Landesliga Sued
UPDATE veranstaltung
SET veranstaltung_liga_id = 4,
    veranstaltung_wettkampftyp_id = 0
WHERE veranstaltung_id = 4;

-- Bezirksoberliga Neckar
UPDATE veranstaltung
SET veranstaltung_liga_id = 5,
    veranstaltung_wettkampftyp_id = 0
WHERE veranstaltung_id = 5;

-- Bezirksliga A Neckar
UPDATE veranstaltung
SET veranstaltung_liga_id = 6,
    veranstaltung_wettkampftyp_id = 1
WHERE veranstaltung_id = 6;

-- Bezirksoberliga Oberschwaben
UPDATE veranstaltung
SET veranstaltung_liga_id = 5,
    veranstaltung_wettkampftyp_id = 1
WHERE veranstaltung_id = 7;

-- Bezirksliga A Oberschwaben
UPDATE veranstaltung
SET veranstaltung_liga_id = 6,
    veranstaltung_wettkampftyp_id = 0
WHERE veranstaltung_id = 8;

-- Bezirksliga B Oberschwaben
UPDATE veranstaltung
SET veranstaltung_liga_id = 7
WHERE veranstaltung_id = 9;

-- BaWue Compound Finale
UPDATE veranstaltung
SET veranstaltung_liga_id = 1
WHERE veranstaltung_id = 20;

-- Wuerttembergliga Compound
UPDATE veranstaltung
SET veranstaltung_liga_id = 2,
    veranstaltung_wettkampftyp_id = 0
WHERE veranstaltung_id = 21;

-- Landesliga A Compound
UPDATE veranstaltung
SET veranstaltung_liga_id = 3,
    veranstaltung_wettkampftyp_id = 0,
    veranstaltung_sportjahr = 2016,
    veranstaltung_meldedeadline = '2015-10-31'
WHERE veranstaltung_id = 22;

-- Landesliga B Compound
UPDATE veranstaltung
SET veranstaltung_liga_id = 4,
    veranstaltung_wettkampftyp_id = 0,
    veranstaltung_sportjahr = 2016,
    veranstaltung_meldedeadline = '2015-10-31'
WHERE veranstaltung_id = 23;

INSERT INTO veranstaltung (
                           veranstaltung_wettkampftyp_id,
                           veranstaltung_name,
                           veranstaltung_sportjahr,
                           veranstaltung_ligaleiter_id,
                           veranstaltung_meldedeadline,
                           version,
                           veranstaltung_liga_id)
VALUES
(1, 'Würtembergliga', 2019, 2, '2018-10-31', 1, 2);
