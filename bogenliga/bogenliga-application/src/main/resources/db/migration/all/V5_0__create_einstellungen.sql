CREATE TABLE IF NOT EXISTS einstellungen
(
    einstellungen_id    INT          NOT NULL,
    einstellungen_key   VARCHAR(255) NOT NULL,
    einstellungen_value TEXT         NOT NULL,

        -- technical columns to track the lifecycle of each row
        -- the "_by" columns references a "benutzer_id" without foreign key constraint
        -- the "_at_utc" columns using the timestamp with the UTC timezone
        -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
    created_at_utc               TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
    created_by                   DECIMAL(19, 0) NOT NULL    DEFAULT 0,
    last_modified_at_utc         TIMESTAMP      NULL        DEFAULT NULL,
    last_modified_by             DECIMAL(19, 0) NULL        DEFAULT NULL,
    version                      DECIMAL(19, 0) NOT NULL    DEFAULT 0,


    CONSTRAINT pk_einstellungen_id PRIMARY KEY (einstellungen_id),
    CONSTRAINT uc_einstellungen_key UNIQUE (einstellungen_key)
);




