--create new permissions specific for role 'LIGALEITER' use only
INSERT INTO recht (recht_id, recht_name)
VALUES(31, 'CAN_MODIFY_SYSTEMDATEN_LIGALEITER');

INSERT INTO recht (recht_id, recht_name)
VALUES(32, 'CAN_CREATE_SYSTEMDATEN_LIGALEITER');

INSERT INTO recht (recht_id, recht_name)
VALUES(33, 'CAN_CREATE_STAMMDATEN_LIGALEITER');

INSERT INTO recht (recht_id, recht_name)
VALUES(34, 'CAN_MODIFY_STAMMDATEN_LIGALEITER');

--assign new permissions to role 'LIGALEITER'
INSERT INTO rolle_recht (rolle_recht_rolle_id, rolle_recht_recht_id)
VALUES (2, 31);

INSERT INTO rolle_recht (rolle_recht_rolle_id, rolle_recht_recht_id)
VALUES (2, 32);

INSERT INTO rolle_recht (rolle_recht_rolle_id, rolle_recht_recht_id)
VALUES (2, 33);

INSERT INTO rolle_recht (rolle_recht_rolle_id, rolle_recht_recht_id)
VALUES (2, 34);
