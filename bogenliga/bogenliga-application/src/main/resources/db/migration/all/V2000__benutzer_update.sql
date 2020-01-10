-- active attribute makes one user active or inactive
-- change email constraint to be unique to the combination of email and active

ALTER TABLE benutzer
    ADD COLUMN
        benutzer_active       BOOLEAN          NOT NULL    DEFAULT TRUE,
    DROP CONSTRAINT uc_benutzer_email;
    CREATE UNIQUE INDEX uc_benutzer_email_active ON benutzer (benutzer_email) WHERE (benutzer_active = TRUE)

;
