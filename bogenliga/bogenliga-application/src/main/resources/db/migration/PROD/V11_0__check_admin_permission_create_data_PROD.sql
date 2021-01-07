DO
$do$
BEGIN
--check if admin has permission CAN_CREATE_SYSTEMDATEN
   IF NOT EXISTS (SELECT FROM rolle_recht WHERE rolle_recht_rolle_id = 1 AND rolle_recht_recht_id = 19) THEN
        --insert permission for creating and inserting new data
        INSERT INTO rolle_recht (rolle_recht_rolle_id, rolle_recht_recht_id)
        VALUES (1,19);
   END IF;
END
$do$
