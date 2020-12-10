-- delete incorrect authorization value 15 - 'CAN_READ_SPORTJAHR' from user role 'VERANSTALTER'
DELETE
FROM rolle_recht
WHERE rolle_recht_rolle_id = 4
  AND rolle_recht_recht_id = 15;