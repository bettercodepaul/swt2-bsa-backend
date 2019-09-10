
SET search_path = 'prod'
;

DELETE from rolle_recht;
DELETE from recht;

INSERT INTO recht(
recht_id,
recht_name
)
VALUES (0,'CAN_READ_DEFAULT'),
       (1,'CAN_READ_STAMMDATEN'),
       (2,'CAN_MODIFY_STAMMDATEN'),
       (3,'CAN_DELETE_STAMMDATEN'),
       (4,'CAN_READ_SYSTEMDATEN'),
       (5,'CAN_MODIFY_SYSTEMDATEN'),
       (6,'CAN_DELETE_SYSTEMDATEN'),
       (7,'CAN_READ_WETTKAMPF'),
       (8,'CAN_MODIFY_WETTKAMPF'),
       (9,'CAN_READ_MY_VEREIN'),
       (10,'CAN_MODIFY_MY_VEREIN'),
       (11,'CAN_READ_MY_VERANSTALTUNG'),
       (12,'CAN_MODIFY_MY_VERANSTALTUNG'),
       (13,'CAN_READ_MY_ORT'),
       (14,'CAN_MODIFY_MY_ORT'),
       (15,'CAN_READ_SPORTJAHR'),
       (16,'CAN_MODIFY_SPORTJAHR'),
       (17,'CAN_OPERATE_SPOTTING')
;