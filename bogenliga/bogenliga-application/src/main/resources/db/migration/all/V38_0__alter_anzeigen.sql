alter table anzeigen
    -- adding BasicDAO audit columns
    ADD COLUMN created_at_utc TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN created_by BIGINT NOT NULL DEFAULT -1,
    ADD COLUMN last_modified_at_utc TIMESTAMP NULL,
    ADD COLUMN last_modified_by BIGINT NULL,
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0,

    -- adding missing column
    ADD COLUMN aktuelles_match NUMERIC(2),

    -- changing physische_bilschirm_id to be nullable
    ALTER COLUMN physische_bildschirm_id DROP NOT NULL;


