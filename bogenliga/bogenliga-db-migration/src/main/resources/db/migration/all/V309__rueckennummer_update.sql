CREATE SEQUENCE rNrSeq START 1;

CREATE FUNCTION initRueckennummern ()
    RETURNS void AS $$
declare
    mannschaft integer;
    mitglied integer;
BEGIN
    FOR mannschaft IN SELECT DISTINCT mannschaftsmitglied_mannschaft_id FROM mannschaftsmitglied
        LOOP
            FOR mitglied IN SELECT mannschaftsmitglied_id FROM mannschaftsmitglied WHERE mannschaftsmitglied_mannschaft_id = mannschaft
                LOOP
                    UPDATE mannschaftsmitglied
                    SET mannschaftsmitglied_rueckennummer = (SELECT nextval('rNrSeq'))
                    WHERE mannschaftsmitglied_id = mitglied;
                END LOOP;
            ALTER SEQUENCE rNrSeq RESTART WITH 1;
        END LOOP;
END
$$ LANGUAGE plpgsql;

SELECT initRueckennummern();

--DROP FUNCTION initRueckennummern;
COMMIT;
DROP FUNCTION initRueckennummern();
DROP SEQUENCE rNrSeq;