
-- Erstellt Tabelle für persistente Tablet-Session-Verwaltung
CREATE TABLE schusszettel_tablet_session (
    id BIGSERIAL PRIMARY KEY,
    token TEXT NOT NULL UNIQUE,
    team_id BIGINT NOT NULL REFERENCES mannschaft(id),
    wettkampf_id BIGINT NOT NULL REFERENCES wettkampf(id),
    current_match_id BIGINT REFERENCES match(id),
    current_passe_number INTEGER DEFAULT 1,
    status VARCHAR(50) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gegner_team_id BIGINT REFERENCES mannschaft(id)
    -- kein finalized-Flag nötig, da Statusvergleich ausreichend
);

-- Indexe für schnelle Abfragen
CREATE INDEX idx_tablet_session_token ON schusszettel_tablet_session(token);
CREATE INDEX idx_tablet_session_lookup ON schusszettel_tablet_session(wettkampf_id, team_id);
