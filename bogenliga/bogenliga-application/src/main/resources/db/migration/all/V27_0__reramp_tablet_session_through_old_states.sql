-- V27: Tablet Session Tabelle komplett neu aufbauen
-- Problem: Migration mehrmals geändert, jetzt sind Duplikate und Mist drin
-- Lösung: Einfach alles wegwerfen und neu machen (Bei erst und einziglauf gibts noch keine wichtigen daten da, und die tabelle ist sowieso meta reagibel auf system)

BEGIN;

-- Checken ob die kaputte Tabelle da ist
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables 
                   WHERE table_schema = 'public' 
                   AND table_name = 'schusszettel_tablet_session') THEN
        RAISE NOTICE 'Tabelle schusszettel_tablet_session nicht da - ist ok, machen wir neu';
    ELSE
        RAISE NOTICE 'Alte kaputte Tabelle gefunden - wird gelöscht...';
    END IF;
END $$;

-- Alte Tabelle komplett löschen (falls da)
DROP TABLE IF EXISTS schusszettel_tablet_session CASCADE;

RAISE NOTICE 'Erstelle saubere neue Tabelle...';

-- Neue saubere Tabelle mit korrekter Struktur erstellen
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

    -- BasicDAO Spalten (die der Prof will)
    created_at_utc TIMESTAMP NOT NULL DEFAULT now(),
    created_by BIGINT NOT NULL DEFAULT -1,
    last_modified_at_utc TIMESTAMP NULL,
    last_modified_by BIGINT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

-- Indexe für Performance
CREATE INDEX idx_tablet_session_token ON schusszettel_tablet_session(token);
CREATE INDEX idx_tablet_session_lookup ON schusszettel_tablet_session(wettkampf_id, team_id);

-- Kurze Validierung dass alles ok ist
DO $$
DECLARE
    col_count integer;
    basicdao_count integer;
    fk_count integer;
BEGIN
    -- Spalten zählen
    SELECT count(*) INTO col_count
    FROM information_schema.columns 
    WHERE table_schema = 'public' 
    AND table_name = 'schusszettel_tablet_session';
    
    -- BasicDAO Spalten prüfen
    SELECT count(*) INTO basicdao_count
    FROM information_schema.columns 
    WHERE table_schema = 'public' 
    AND table_name = 'schusszettel_tablet_session' 
    AND column_name IN ('created_at_utc', 'created_by', 'last_modified_at_utc', 'last_modified_by', 'version');
    
    -- Foreign Keys zählen
    SELECT count(*) INTO fk_count
    FROM information_schema.table_constraints 
    WHERE table_schema = 'public' 
    AND table_name = 'schusszettel_tablet_session' 
    AND constraint_type = 'FOREIGN KEY';
    
    RAISE NOTICE 'Neue Tabelle erstellt:';
    RAISE NOTICE '- % Spalten gesamt', col_count;
    RAISE NOTICE '- % BasicDAO Spalten (sollten 5 sein)', basicdao_count;
    RAISE NOTICE '- % Foreign Keys', fk_count;
    
    IF basicdao_count = 5 THEN
        RAISE NOTICE 'Perfect! Alle BasicDAO Spalten sind da.';
    ELSE
        RAISE WARNING 'Ups, BasicDAO Spalten stimmen nicht ganz...';
    END IF;
    
    RAISE NOTICE 'Tabelle ist sauber und ready to use!';
END $$;

COMMIT;

-- Fertig - viel einfacher als den ganzen Mist zu reparieren
