-- view that contains all arrow averages per wettkampftag and the veranstaltung from a schuetze

--> Problem!!! This approach ignores 0 values, because the current design creates Passen for Schuetzen, even when they don't shoot at the match, when it's fixed then remove the condition:
-- 'AND merged_pfeilwerte <> 0' in both subqueries

--> allows faster requests especially looking at average values


DROP VIEW IF EXISTS pfeilschnitte_schuetze_veranstaltung;

CREATE VIEW pfeilschnitte_schuetze_veranstaltung (
                                                pfeilschnitte_schuetze_veranstaltung_schuetzenname,
                                                pfeilschnitte_schuetze_veranstaltung_wettkampftag_1_schnitt,
                                                pfeilschnitte_schuetze_veranstaltung_wettkampftag_1_id,
                                                pfeilschnitte_schuetze_veranstaltung_wettkampftag_2_schnitt,
                                                pfeilschnitte_schuetze_veranstaltung_wettkampftag_2_id,
                                                pfeilschnitte_schuetze_veranstaltung_wettkampftag_3_schnitt,
                                                pfeilschnitte_schuetze_veranstaltung_wettkampftag_3_id,
                                                pfeilschnitte_schuetze_veranstaltung_wettkampftag_4_schnitt,
                                                pfeilschnitte_schuetze_veranstaltung_wettkampftag_4_id,
                                                pfeilschnitte_schuetze_veranstaltung_veranstaltung_schnitt,
                                                pfeilschnitte_schuetze_veranstaltung_veranstaltung_id,
                                                pfeilschnitte_schuetze_veranstaltung_verein_id,
                                                pfeilschnitte_schuetze_veranstaltung_dsb_mitglied_id,
                                                pfeilschnitte_schuetze_veranstaltung_mannschaft_id
    ) AS
SELECT
    (MAX(dsb_mitglied_vorname) || ' ' || MAX(dsb_mitglied_nachname))    AS pfeilschnitte_schuetze_veranstaltung_schuetzenname,
    MAX(CASE WHEN wettkampf_tag = 1 THEN pfeilschnitt_wettkampf END)    AS pfeilschnitte_schuetze_veranstaltung_wettkampftag_1_schnitt,
    MAX(CASE WHEN wettkampf_tag = 1 THEN wettkampf_id END)              AS pfeilschnitte_schuetze_veranstaltung_wettkampftag_1_id,
    MAX(CASE WHEN wettkampf_tag = 2 THEN pfeilschnitt_wettkampf END)    AS pfeilschnitte_schuetze_veranstaltung_wettkampftag_2_schnitt,
    MAX(CASE WHEN wettkampf_tag = 2 THEN wettkampf_id END)              AS pfeilschnitte_schuetze_veranstaltung_wettkampftag_2_id,
    MAX(CASE WHEN wettkampf_tag = 3 THEN pfeilschnitt_wettkampf END)    AS pfeilschnitte_schuetze_veranstaltung_wettkampftag_3_schnitt,
    MAX(CASE WHEN wettkampf_tag = 3 THEN wettkampf_id END)              AS pfeilschnitte_schuetze_veranstaltung_wettkampftag_3_id,
    MAX(CASE WHEN wettkampf_tag = 4 THEN pfeilschnitt_wettkampf END)    AS pfeilschnitte_schuetze_veranstaltung_wettkampftag_4_schnitt,
    MAX(CASE WHEN wettkampf_tag = 4 THEN wettkampf_id END)              AS pfeilschnitte_schuetze_veranstaltung_wettkampftag_4_id,
    MAX(pfeilschnitt_veranstaltung)                                     AS pfeilschnitte_schuetze_veranstaltung_veranstaltung_schnitt,
    MAX(veranstaltung_id)                                               AS pfeilschnitte_schuetze_veranstaltung_veranstaltung_id,
    MAX(mannschaft_verein_id)                                           AS pfeilschnitte_schuetze_veranstaltung_verein_id,
    pfeilwerte_schuetze_match_dsb_mitglied_id                           AS pfeilschnitte_schuetze_veranstaltung_dsb_mitglied_id,
    MAX(match_mannschaft_id)                                            AS pfeilschnitte_schuetze_veranstaltung_mannschaft_id

    FROM (
        -- this calculates the wettkampfsschnitt of a schuetze based on every Pfeilwert
        SELECT
            ROUND(AVG(merged_pfeilwerte), 2)        AS pfeilschnitt_wettkampf,
            m.match_wettkampf_id,
            pfeilwerte_schuetze_match_dsb_mitglied_id,
            w.wettkampf_veranstaltung_id,
            w.wettkampf_id,
            w.wettkampf_tag,
            m.match_mannschaft_id
                FROM (
                  SELECT
                      UNNEST(
                            pfeilwerte_schuetze_match_pfeilwerte1 || pfeilwerte_schuetze_match_pfeilwerte2
                        || pfeilwerte_schuetze_match_pfeilwerte3 || pfeilwerte_schuetze_match_pfeilwerte4
                        || pfeilwerte_schuetze_match_pfeilwerte5 || pfeilwerte_schuetze_match_pfeilwerte6
                         )                         AS merged_pfeilwerte,
                         pfeilwerte_schuetze_match_match_id,
                         pfeilwerte_schuetze_match_dsb_mitglied_id
                    FROM
                      pfeilwerte_schuetze_match) subquery
                    JOIN match m ON pfeilwerte_schuetze_match_match_id = m.match_id JOIN wettkampf w ON m.match_wettkampf_id = w.wettkampf_id
         WHERE
             merged_pfeilwerte IS NOT NULL AND merged_pfeilwerte <> 0
         GROUP BY
             wettkampf_veranstaltung_id, m.match_wettkampf_id, pfeilwerte_schuetze_match_dsb_mitglied_id, w.wettkampf_tag, wettkampf_id, m.match_mannschaft_id
         ORDER BY
             m.match_wettkampf_id, w.wettkampf_tag) sub
    JOIN (
        -- this calculates the veranstaltungsschnitt of a schuetze based on every Pfeilwert
        SELECT
            ROUND(AVG(merged_pfeilwerte), 2)                AS pfeilschnitt_veranstaltung,
               pfeilwerte_schuetze_match_dsb_mitglied_id    AS dsb_mitglied_id,
               wettkampf_veranstaltung_id                   AS veranstaltung_id
            FROM (
                 SELECT UNNEST(
                            pfeilwerte_schuetze_match_pfeilwerte1 || pfeilwerte_schuetze_match_pfeilwerte2
                        ||  pfeilwerte_schuetze_match_pfeilwerte3 || pfeilwerte_schuetze_match_pfeilwerte4
                        ||  pfeilwerte_schuetze_match_pfeilwerte5 || pfeilwerte_schuetze_match_pfeilwerte6
                        )           AS merged_pfeilwerte,
                        pfeilwerte_schuetze_match_match_id,
                        pfeilwerte_schuetze_match_dsb_mitglied_id
                 FROM
                     pfeilwerte_schuetze_match
                 ) subquery JOIN match m ON pfeilwerte_schuetze_match_match_id = m.match_id JOIN wettkampf w ON m.match_wettkampf_id = w.wettkampf_id
        WHERE
            merged_pfeilwerte IS NOT NULL AND merged_pfeilwerte <> 0
        GROUP BY
            w.wettkampf_veranstaltung_id, pfeilwerte_schuetze_match_dsb_mitglied_id
) veranstaltung_schnitt ON sub.wettkampf_veranstaltung_id = veranstaltung_schnitt.veranstaltung_id
        JOIN mannschaft ON mannschaft.mannschaft_id = sub.match_mannschaft_id
        JOIN dsb_mitglied ON dsb_mitglied.dsb_mitglied_id = pfeilwerte_schuetze_match_dsb_mitglied_id
WHERE
    pfeilwerte_schuetze_match_dsb_mitglied_id = veranstaltung_schnitt.dsb_mitglied_id

GROUP BY wettkampf_veranstaltung_id, pfeilwerte_schuetze_match_dsb_mitglied_id;

