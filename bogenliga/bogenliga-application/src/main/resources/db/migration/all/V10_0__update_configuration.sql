ALTER TABLE configuration ADD COLUMN configuration_id INTEGER;
DROP SEQUENCE IF EXISTS configuration_1;
CREATE SEQUENCE configuration_1 START 1;
ALTER TABLE configuration ALTER COLUMN configuration_id SET DEFAULT nextval('configuration_1');
UPDATE configuration SET configuration_id = nextval('configuration_1');

alter table configuration alter column configuration_id set not null;

create unique index configuration_configuration_id_uindex
    on configuration (configuration_id);

alter table configuration drop constraint pk_configuration_key;

create unique index configuration_key
	on configuration (configuration_key);


alter table configuration
    add constraint pk_configuration_id
        primary key (configuration_id);



ALTER TABLE IF EXISTS configuration
ADD created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc');
ALTER TABLE IF EXISTS configuration
ADD created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0;
ALTER TABLE IF EXISTS configuration
ADD last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL;
ALTER TABLE IF EXISTS configuration
ADD last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL;
ALTER TABLE IF EXISTS configuration
ADD version               DECIMAL(19,0)    NOT NULL    DEFAULT 0;

INSERT INTO configuration (configuration_key, configuration_value)
VALUES('SMTPHost','mx2f1a.netcup.net')
ON CONFLICT (configuration_key) DO
    NOTHING;
INSERT INTO configuration (configuration_key, configuration_value)
VALUES('SMTPEmail','noreply@bsapp.de')
ON CONFLICT (configuration_key) DO
    NOTHING;
INSERT INTO configuration (configuration_key, configuration_value)
VALUES('SMTPBenutzer','noreply@bsapp.de')
ON CONFLICT (configuration_key) DO
    NOTHING;
INSERT INTO configuration (configuration_key, configuration_value)
VALUES('SMTPPort','465')
ON CONFLICT (configuration_key) DO
    NOTHING;
INSERT INTO configuration (configuration_key, configuration_value)
VALUES('SMTPPasswort','Passwort bitte ändern')
ON CONFLICT (configuration_key) DO
    NOTHING;