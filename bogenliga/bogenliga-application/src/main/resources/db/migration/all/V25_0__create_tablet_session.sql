-- Erstellt Tabelle für persistente Tablet-Session-Verwaltung
CREATE TABLE schusszettel_tablet_session (
    id BIGSERIAL PRIMARY KEY,
    token TEXT NOT NULL UNIQUE,
    team_id BIGINT NOT NULL REFERENCES mannschaft(mannschaft_id),
    wettkampf_id BIGINT NOT NULL REFERENCES wettkampf(wettkampf_id),
    current_match_id BIGINT REFERENCES match(match_id),
    current_match_number INTEGER NOT NULL DEFAULT 1,
    current_passe_number INTEGER DEFAULT 1,
    status VARCHAR(50) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gegner_team_id BIGINT REFERENCES mannschaft(mannschaft_id),

    -- Audit‐Spalten für BasicDAO
    created_at_utc       TIMESTAMP     NOT NULL DEFAULT now(),
    created_by           BIGINT        NOT NULL,
    last_modified_at_utc TIMESTAMP     NULL,
    last_modified_by     BIGINT        NULL,
    version              BIGINT        NOT NULL DEFAULT 0
);

-- Indexe für schnelle Abfragen
CREATE INDEX idx_tablet_session_token  ON schusszettel_tablet_session(token);
CREATE INDEX idx_tablet_session_lookup ON schusszettel_tablet_session(wettkampf_id, team_id);
