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