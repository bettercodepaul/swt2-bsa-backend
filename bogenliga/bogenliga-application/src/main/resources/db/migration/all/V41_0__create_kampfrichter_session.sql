-- Kampfrichter session table for token-based access to the referee tablet view

CREATE TABLE kampfrichter_session (
    id                    BIGSERIAL PRIMARY KEY,
    token                 TEXT NOT NULL UNIQUE,
    wettkampf_id          BIGINT NOT NULL UNIQUE REFERENCES wettkampf(wettkampf_id),
    created_at_utc        TIMESTAMP NOT NULL DEFAULT now(),
    created_by            BIGINT NOT NULL DEFAULT -1,
    last_modified_at_utc  TIMESTAMP NULL,
    last_modified_by      BIGINT NULL,
    version               BIGINT NOT NULL DEFAULT 0
);
