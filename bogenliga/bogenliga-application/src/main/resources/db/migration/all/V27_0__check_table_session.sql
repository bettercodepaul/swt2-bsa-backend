-- V27: Struktur-Check für schusszettel_tablet_session (nur zum schauen für Logs)
-- Dieser Check wirft keine Exceptions und bricht die Pipeline nicht ab

-- Timeout setzen falls was hängt
SET statement_timeout = '60s';

-- Funktion um alles zu checken ohne Pipeline zu zerstören
CREATE OR REPLACE FUNCTION check_tablet_session_structure() RETURNS void AS $$
DECLARE
    table_exists boolean := false;
    col_exists boolean;
    col_type text;
    index_exists boolean;
    missing_columns text[] := ARRAY[]::text[];
    wrong_type_columns text[] := ARRAY[]::text[];
    missing_indexes text[] := ARRAY[]::text[];
    col_count integer;
    total_issues integer := 0;
BEGIN
    RAISE NOTICE '=== TABLET SESSION STRUKTUR CHECK START ===';
    
    -- Checken ob Tabelle überhaupt existiert
    SELECT EXISTS (
        SELECT 1 FROM information_schema.tables 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session'
    ) INTO table_exists;
    
    IF NOT table_exists THEN
        RAISE WARNING 'PROBLEM: Tabelle schusszettel_tablet_session existiert nicht!';
        RAISE NOTICE 'Du musst wahrscheinlich erst V25 Migration laufen lassen.';
        total_issues := total_issues + 1;
        RAISE NOTICE '=== CHECK BEENDET (Tabelle fehlt komplett) ===';
        RETURN;
    END IF;
    
    RAISE NOTICE 'OK: Tabelle schusszettel_tablet_session existiert';

    -- created_at_utc checken
    SELECT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session' 
        AND column_name = 'created_at_utc'
    ) INTO col_exists;
    
    IF col_exists THEN
        SELECT data_type INTO col_type 
        FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session' 
        AND column_name = 'created_at_utc';
        
        IF col_type != 'timestamp without time zone' THEN
            RAISE WARNING 'PROBLEM: Spalte created_at_utc hat falschen Typ: % (sollte sein: timestamp without time zone)', col_type;
            wrong_type_columns := array_append(wrong_type_columns, 'created_at_utc');
            total_issues := total_issues + 1;
        ELSE
            RAISE NOTICE 'OK: created_at_utc ist da und hat richtigen Typ';
        END IF;
    ELSE
        RAISE WARNING 'PROBLEM: Spalte created_at_utc fehlt komplett';
        missing_columns := array_append(missing_columns, 'created_at_utc');
        total_issues := total_issues + 1;
    END IF;

    -- created_by checken
    SELECT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session' 
        AND column_name = 'created_by'
    ) INTO col_exists;
    
    IF col_exists THEN
        SELECT data_type INTO col_type 
        FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session' 
        AND column_name = 'created_by';
        
        IF col_type != 'bigint' THEN
            RAISE WARNING 'PROBLEM: Spalte created_by hat falschen Typ: % (sollte sein: bigint)', col_type;
            wrong_type_columns := array_append(wrong_type_columns, 'created_by');
            total_issues := total_issues + 1;
        ELSE
            RAISE NOTICE 'OK: created_by ist da und hat richtigen Typ';
        END IF;
    ELSE
        RAISE WARNING 'PROBLEM: Spalte created_by fehlt komplett';
        missing_columns := array_append(missing_columns, 'created_by');
        total_issues := total_issues + 1;
    END IF;

    -- last_modified_at_utc checken
    SELECT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session' 
        AND column_name = 'last_modified_at_utc'
    ) INTO col_exists;
    
    IF col_exists THEN
        SELECT data_type INTO col_type 
        FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session' 
        AND column_name = 'last_modified_at_utc';
        
        IF col_type != 'timestamp without time zone' THEN
            RAISE WARNING 'PROBLEM: Spalte last_modified_at_utc hat falschen Typ: % (sollte sein: timestamp without time zone)', col_type;
            wrong_type_columns := array_append(wrong_type_columns, 'last_modified_at_utc');
            total_issues := total_issues + 1;
        ELSE
            RAISE NOTICE 'OK: last_modified_at_utc ist da und hat richtigen Typ';
        END IF;
    ELSE
        RAISE WARNING 'PROBLEM: Spalte last_modified_at_utc fehlt komplett';
        missing_columns := array_append(missing_columns, 'last_modified_at_utc');
        total_issues := total_issues + 1;
    END IF;

    -- last_modified_by checken
    SELECT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session' 
        AND column_name = 'last_modified_by'
    ) INTO col_exists;
    
    IF col_exists THEN
        SELECT data_type INTO col_type 
        FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session' 
        AND column_name = 'last_modified_by';
        
        IF col_type != 'bigint' THEN
            RAISE WARNING 'PROBLEM: Spalte last_modified_by hat falschen Typ: % (sollte sein: bigint)', col_type;
            wrong_type_columns := array_append(wrong_type_columns, 'last_modified_by');
            total_issues := total_issues + 1;
        ELSE
            RAISE NOTICE 'OK: last_modified_by ist da und hat richtigen Typ';
        END IF;
    ELSE
        RAISE WARNING 'PROBLEM: Spalte last_modified_by fehlt komplett';
        missing_columns := array_append(missing_columns, 'last_modified_by');
        total_issues := total_issues + 1;
    END IF;

    -- version checken
    SELECT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session' 
        AND column_name = 'version'
    ) INTO col_exists;
    
    IF col_exists THEN
        SELECT data_type INTO col_type 
        FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'schusszettel_tablet_session' 
        AND column_name = 'version';
        
        IF col_type != 'bigint' THEN
            RAISE WARNING 'PROBLEM: Spalte version hat falschen Typ: % (sollte sein: bigint)', col_type;
            wrong_type_columns := array_append(wrong_type_columns, 'version');
            total_issues := total_issues + 1;
        ELSE
            RAISE NOTICE 'OK: version ist da und hat richtigen Typ';
        END IF;
    ELSE
        RAISE WARNING 'PROBLEM: Spalte version fehlt komplett';
        missing_columns := array_append(missing_columns, 'version');
        total_issues := total_issues + 1;
    END IF;

    -- Token-Index checken
    SELECT EXISTS (
        SELECT 1 FROM pg_indexes 
        WHERE schemaname = 'public' 
        AND tablename = 'schusszettel_tablet_session' 
        AND indexname = 'idx_tablet_session_token'
    ) INTO index_exists;
    
    IF index_exists THEN
        RAISE NOTICE 'OK: Index idx_tablet_session_token ist da';
    ELSE
        RAISE WARNING 'PROBLEM: Index idx_tablet_session_token fehlt';
        missing_indexes := array_append(missing_indexes, 'idx_tablet_session_token');
        total_issues := total_issues + 1;
    END IF;

    -- Lookup-Index checken
    SELECT EXISTS (
        SELECT 1 FROM pg_indexes 
        WHERE schemaname = 'public' 
        AND tablename = 'schusszettel_tablet_session' 
        AND indexname = 'idx_tablet_session_lookup'
    ) INTO index_exists;
    
    IF index_exists THEN
        RAISE NOTICE 'OK: Index idx_tablet_session_lookup ist da';
    ELSE
        RAISE WARNING 'PROBLEM: Index idx_tablet_session_lookup fehlt';
        missing_indexes := array_append(missing_indexes, 'idx_tablet_session_lookup');
        total_issues := total_issues + 1;
    END IF;

    -- Zusammenfassung ausgeben
    RAISE NOTICE '=== ZUSAMMENFASSUNG ===';
    
    IF total_issues = 0 THEN
        RAISE NOTICE 'SUPER: Alles ist perfekt! BasicDAO Struktur ist vollständig.';
    ELSE
        RAISE NOTICE 'PROBLEME GEFUNDEN: % Sachen müssen gefixt werden', total_issues;
        
        IF array_length(missing_columns, 1) > 0 THEN
            RAISE NOTICE 'Fehlende Spalten: %', array_to_string(missing_columns, ', ');
        END IF;
        
        IF array_length(wrong_type_columns, 1) > 0 THEN
            RAISE NOTICE 'Spalten mit falschem Typ: %', array_to_string(wrong_type_columns, ', ');
        END IF;
        
        IF array_length(missing_indexes, 1) > 0 THEN
            RAISE NOTICE 'Fehlende Indexe: %', array_to_string(missing_indexes, ', ');
        END IF;
        
        RAISE NOTICE 'Laufe V26 oder die robuste V27 um die Probleme zu fixen';
    END IF;
    
    RAISE NOTICE '=== CHECK BEENDET ===';

EXCEPTION
    WHEN OTHERS THEN
        RAISE WARNING 'Unerwarteter Fehler beim Check: %', SQLERRM;
        RAISE NOTICE 'Check konnte nicht vollständig durchgeführt werden, aber Pipeline läuft weiter';
END;
$$ LANGUAGE plpgsql;

-- Check-Funktion ausführen
SELECT check_tablet_session_structure();

-- Funktion wieder löschen (sauber bleiben!)
DROP FUNCTION check_tablet_session_structure();

-- Statement timeout zurücksetzen
SET statement_timeout = DEFAULT;

-- Immer erfolgreich beenden (auch wenn Probleme gefunden wurden)
SELECT 'V27 Structure Check completed - see log messages above for results' as status;
