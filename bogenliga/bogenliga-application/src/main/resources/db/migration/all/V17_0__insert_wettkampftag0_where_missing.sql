DO $do$

DECLARE
    tmprows RECORD;
BEGIN
    FOR tmprows IN SELECT DISTINCT wettkampf_veranstaltung_id FROM wettkampf LOOP
        INSERT INTO wettkampf(wettkampf_id, wettkampf_veranstaltung_id, wettkampf_datum, wettkampf_beginn, wettkampf_tag, wettkampf_disziplin_id, wettkampf_wettkampftyp_id,
                              created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, wettkampfausrichter, wettkampf_strasse, wettkampf_plz, wettkampf_ortsname,
                              wettkampf_ortsinfo)
        SELECT nextval('sq_wettkampf_id'),
               wettkampf_veranstaltung_id,
               wettkampf_datum,
               wettkampf_beginn,
               0,
               wettkampf_disziplin_id,
               wettkampf_wettkampftyp_id,
               created_at_utc,
               created_by,
               last_modified_at_utc,
               last_modified_by,
               version,
               wettkampfausrichter,
               wettkampf_strasse,
               wettkampf_plz,
               wettkampf_ortsname,
               wettkampf_ortsinfo
        FROM wettkampf WHERE wettkampf_veranstaltung_id=tmprows.wettkampf_veranstaltung_id
                        AND wettkampf_tag=1
                        AND NOT EXISTS(SELECT * FROM wettkampf WHERE wettkampf_veranstaltung_id=tmprows.wettkampf_veranstaltung_id AND wettkampf_tag=0);
        END LOOP;
END
$do$