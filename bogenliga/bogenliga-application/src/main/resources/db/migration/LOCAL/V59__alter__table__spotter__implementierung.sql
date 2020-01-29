ALTER TABLE tablet_session ADD COLUMN access_token DECIMAL(19,0) DEFAULT 0;

INSERT INTO rolle_recht (rolle_recht_rolle_id, rolle_recht_recht_id) VALUES (9, 7), (9, 8), (9, 17);