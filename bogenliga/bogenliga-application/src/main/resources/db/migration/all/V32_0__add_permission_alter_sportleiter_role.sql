INSERT INTO recht (recht_id, recht_name)
VALUES (36, 'CAN_READ_SYSTEMDATEN_SPORTLEITER');

UPDATE rolle_recht SET rolle_recht_recht_id=36
WHERE rolle_recht_recht_id=4 AND rolle_recht_rolle_id=5;