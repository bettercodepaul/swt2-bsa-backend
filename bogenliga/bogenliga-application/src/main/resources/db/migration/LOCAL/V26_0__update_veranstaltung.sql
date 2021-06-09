-- ckeck and improve test table "veranstaltung" with the correct "veranstaltung_liga_id"

UPDATE veranstaltung
SET veranstaltung_liga_id = 9
where veranstaltung_id = 3;

UPDATE veranstaltung
SET veranstaltung_liga_id = 10
where veranstaltung_id = 4;

UPDATE veranstaltung
SET veranstaltung_liga_id = 11
where veranstaltung_id = 5;

UPDATE veranstaltung
SET veranstaltung_liga_id = 13
where veranstaltung_id = 6;

UPDATE veranstaltung
SET veranstaltung_liga_id = 12
where veranstaltung_id = 7;

UPDATE veranstaltung
SET veranstaltung_liga_id = 14
where veranstaltung_id = 8;

UPDATE veranstaltung
SET veranstaltung_liga_id = 15
where veranstaltung_id = 9;

UPDATE veranstaltung
SET veranstaltung_liga_id = 19
where veranstaltung_id = 20;

UPDATE veranstaltung
SET veranstaltung_liga_id = 16
where veranstaltung_id = 21;

UPDATE veranstaltung
SET veranstaltung_liga_id = 17
where veranstaltung_id = 22;

UPDATE veranstaltung
SET veranstaltung_liga_id = 18
where veranstaltung_id = 23;


delete
from veranstaltung
where veranstaltung_id = 1000
and veranstaltung_id = 1001
and veranstaltung_id = 1002;
