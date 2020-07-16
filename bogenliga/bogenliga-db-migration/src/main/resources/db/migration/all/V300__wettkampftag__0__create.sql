-- inserts a Wettkampftag 0 for each Veranstaltung which has no Wettkampf entry at all
-- and inserts a Wettkampftag 0 for each Wettkampf entry that has no Wettkampftag 0.
-- just the wettkampf_veranstaltungs_id will be set by circumstances. The other values are static default values.

INSERT INTO wettkampf(wettkampf_veranstaltung_id,
                      wettkampf_datum,
                      wettkampf_ort,
                      wettkampf_beginn,
                      wettkampf_tag,
                      wettkampf_disziplin_id,
                      wettkampf_wettkampftyp_id)

SELECT DISTINCT v.veranstaltung_id,
                DATE '1900-01-01' AS datum,
                ' - '             AS ort,
                ' - '             AS beginn,
                0                 AS tag,
                0                 AS disziplin,
                0                 AS typ
FROM veranstaltung v
         LEFT JOIN wettkampf w on v.veranstaltung_id = w.wettkampf_veranstaltung_id
WHERE w.wettkampf_id ISNULL
   OR v.veranstaltung_id IN
      (SELECT wettkampf_veranstaltung_id
       from wettkampf
       GROUP BY wettkampf_veranstaltung_id
       HAVING NOT min(wettkampf_tag) = 0
      )
;


