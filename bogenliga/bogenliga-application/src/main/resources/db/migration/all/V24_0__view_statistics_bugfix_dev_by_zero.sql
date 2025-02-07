DROP VIEW IF EXISTS pfeilschnitte_schuetze_veranstaltung;
Drop VIEW if exists schuetzenstatistik;
drop VIEW if exists pfeilwerte_schuetze_match;

/* Update für den folgenden View wg. Division durch 0 */
/* die beiden weiteren Views müssen für das Update gelöscht und unverändert wieder angelegt werden. */


create view pfeilwerte_schuetze_match
            (pfeilwerte_schuetze_match_match_id, pfeilwerte_schuetze_match_dsb_mitglied_id,
             pfeilwerte_schuetze_match_pfeilwerte1, pfeilwerte_schuetze_match_pfeilwerte2,
             pfeilwerte_schuetze_match_pfeilwerte3, pfeilwerte_schuetze_match_pfeilwerte4,
             pfeilwerte_schuetze_match_pfeilwerte5, pfeilwerte_schuetze_match_pfeilwerte6,
             pfeilwerte_schuetze_match_pfeilwert_schnitt)
as
SELECT match.match_id                                                                                       AS  pfeilwerte_schuetze_match_match_id,
       dsb_mitglied.dsb_mitglied_id                                                                         AS  pfeilwerte_schuetze_match_dsb_mitglied_id,
       array_agg(passe.passe_ringzahl_pfeil1 ORDER BY passe.passe_lfdnr)                                    AS  pfeilwerte_schuetze_match_pfeilwerte1,
       array_agg(passe.passe_ringzahl_pfeil2 ORDER BY passe.passe_lfdnr)                                    AS  pfeilwerte_schuetze_match_pfeilwerte2,
       array_agg(passe.passe_ringzahl_pfeil3 ORDER BY passe.passe_lfdnr)                                    AS  pfeilwerte_schuetze_match_pfeilwerte3,
       array_agg(passe.passe_ringzahl_pfeil4 ORDER BY passe.passe_lfdnr)                                    AS  pfeilwerte_schuetze_match_pfeilwerte4,
       array_agg(passe.passe_ringzahl_pfeil5 ORDER BY passe.passe_lfdnr)                                    AS  pfeilwerte_schuetze_match_pfeilwerte5,
       array_agg(passe.passe_ringzahl_pfeil6 ORDER BY passe.passe_lfdnr)                                    AS  pfeilwerte_schuetze_match_pfeilwerte6,
       (sum(COALESCE(passe.passe_ringzahl_pfeil1, 0::numeric)) +
        sum(COALESCE(passe.passe_ringzahl_pfeil2, 0::numeric)) +
        sum(COALESCE(passe.passe_ringzahl_pfeil3, 0::numeric)) +
        sum(COALESCE(passe.passe_ringzahl_pfeil4, 0::numeric)) +
        sum(COALESCE(passe.passe_ringzahl_pfeil5, 0::numeric)) +
        sum(COALESCE(passe.passe_ringzahl_pfeil6, 0::numeric))) / (sum(
                                                                           CASE
                                                                               WHEN passe.passe_ringzahl_pfeil1 IS NOT NULL
                                                                                   THEN 1
                                                                               ELSE 1
                                                                               //* by definition at least a value for the first arrow is given
                                                                               //* and by setting min = 1 we prohibit division by zero ;-)
                                                                               END) + sum(
                                                                           CASE
                                                                               WHEN passe.passe_ringzahl_pfeil2 IS NOT NULL
                                                                                   THEN 1
                                                                               ELSE 0
                                                                               END) + sum(
                                                                           CASE
                                                                               WHEN passe.passe_ringzahl_pfeil3 IS NOT NULL
                                                                                   THEN 1
                                                                               ELSE 0
                                                                               END) + sum(
                                                                           CASE
                                                                               WHEN passe.passe_ringzahl_pfeil4 IS NOT NULL
                                                                                   THEN 1
                                                                               ELSE 0
                                                                               END) + sum(
                                                                           CASE
                                                                               WHEN passe.passe_ringzahl_pfeil5 IS NOT NULL
                                                                                   THEN 1
                                                                               ELSE 0
                                                                               END) + sum(
                                                                           CASE
                                                                               WHEN passe.passe_ringzahl_pfeil6 IS NOT NULL
                                                                                   THEN 1
                                                                               ELSE 0
                                                                               END))::numeric             AS pfeilwerte_schuetze_match_pfeilwert_schnitt
FROM match,
     dsb_mitglied,
     passe
WHERE   passe.passe_match_id = match.match_id
  AND passe.passe_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
GROUP BY match.match_id, dsb_mitglied.dsb_mitglied_id,
         (passe.passe_ringzahl_pfeil1 + passe.passe_ringzahl_pfeil2 + passe.passe_ringzahl_pfeil3 +
          passe.passe_ringzahl_pfeil4 + passe.passe_ringzahl_pfeil5 + passe.passe_ringzahl_pfeil6);



-- extend view, by dropping it first and then adding new coloumns (schuetze_satz1,...)
Drop VIEW if exists schuetzenstatistik;

create view schuetzenstatistik
            (schuetzenstatistik_veranstaltung_id, schuetzenstatistik_veranstaltung_name,
             schuetzenstatistik_wettkampf_id, schuetzenstatistik_wettkampf_tag, schuetzenstatistik_wettkampf_datum, schuetzenstatistik_mannschaft_id,
             schuetzenstatistik_mannschaft_nummer, schuetzenstatistik_verein_id, schuetzenstatistik_verein_name,
             schuetzenstatistik_match_id, schuetzenstatistik_match_nr, schuetzenstatistik_dsb_mitglied_id, schuetzenstatistik_dsb_mitglied_name,
             schuetzenstatistik_rueckennummer, schuetzenstatistik_schuetze_satz1, schuetzenstatistik_schuetze_satz2,
             schuetzenstatistik_schuetze_satz3, schuetzenstatistik_schuetze_satz4, schuetzenstatistik_schuetze_satz5,
             schuetzenstatistik_pfeilpunkte_schnitt)
as
SELECT veranstaltung.veranstaltung_id                                                                     AS schuetzenstatistik_veranstaltung_id,
       veranstaltung.veranstaltung_name                                                                   AS schuetzenstatistik_veranstaltung_name,
       wettkampf.wettkampf_id                                                                             AS schuetzenstatistik_wettkampf_id,
       wettkampf.wettkampf_tag                                                                            AS schuetzenstatistik_wettkampf_tag,
       wettkampf.wettkampf_datum                                                                          AS schuetzenstatistik_wettkampf_datum,
       mannschaft.mannschaft_id                                                                           AS schuetzenstatistik_mannschaft_id,
       mannschaft.mannschaft_nummer                                                                       AS schuetzenstatistik_mannschaft_nummer,
       verein.verein_id                                                                                   AS schuetzenstatistik_verein_id,
       verein.verein_name                                                                                 AS schuetzenstatistik_verein_name,
       match.match_id                                                                                     AS schuetzenstatistik_match_id,
       match.match_nr                                                                                     AS schuetzenstatistik_match_nr,
       dsb_mitglied.dsb_mitglied_id                                                                       AS schuetzenstatistik_dsb_mitglied_id,
       (dsb_mitglied.dsb_mitglied_vorname::text || ' '::text) ||
       dsb_mitglied.dsb_mitglied_nachname::text                                                           AS schuetzenstatistik_dsb_mitglied_name,
       mannschaftsmitglied.mannschaftsmitglied_rueckennummer                                              AS schuetzenstatistik_rueckennummer,


       ARRAY[
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte1[1], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte2[1],
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte3[1], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte4[1],
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte5[1], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte6[1]]
                                                                                                          AS schuetzenstatistik_schuetze_satz1,
       ARRAY[
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte1[2], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte2[2],
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte3[2], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte4[2],
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte5[2], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte6[2]]
                                                                                                          AS schuetzenstatistik_schuetze_satz2,
       ARRAY[
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte1[3], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte2[3],
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte3[3], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte4[3],
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte5[3], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte6[3]]
                                                                                                          AS schuetzenstatistik_schuetze_satz3,
       ARRAY[
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte1[4], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte2[4],
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte3[4], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte4[4],
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte5[4], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte6[4]]
                                                                                                          AS schuetzenstatistik_schuetze_satz4,
       ARRAY[
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte1[5], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte2[5],
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte3[5], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte4[5],
            pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte5[5], pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte6[5]]
                                                                                                          AS schuetzenstatistik_schuetze_satz5,

       ROUND(pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwert_schnitt, 2)                            AS schuetzenstatistik_pfeilpunkte_schnitt
FROM match,
     veranstaltung,
     wettkampf,
     mannschaft,
     mannschaftsmitglied,
     verein,
     dsb_mitglied,
     pfeilwerte_schuetze_match
WHERE match.match_wettkampf_id = wettkampf.wettkampf_id
  AND wettkampf.wettkampf_veranstaltung_id = veranstaltung.veranstaltung_id
  AND match.match_mannschaft_id = mannschaft.mannschaft_id
  AND mannschaft.mannschaft_verein_id = verein.verein_id
  AND mannschaftsmitglied_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
  AND mannschaftsmitglied.mannschaftsmitglied_mannschaft_id = mannschaft.mannschaft_id
  AND pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_match_id = match.match_id
  AND pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
GROUP BY veranstaltung.veranstaltung_id, veranstaltung.veranstaltung_name, wettkampf.wettkampf_id,
         wettkampf.wettkampf_tag, mannschaft.mannschaft_id, mannschaft.mannschaft_nummer, verein.verein_id,
         verein.verein_name, match.match_id, match.match_nr, dsb_mitglied.dsb_mitglied_id, pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwert_schnitt,
         ((dsb_mitglied.dsb_mitglied_vorname::text || ' '::text) || dsb_mitglied.dsb_mitglied_nachname::text),
         mannschaftsmitglied.mannschaftsmitglied_rueckennummer,
         pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte1, pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte2,
         pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte3, pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte4,
         pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte5, pfeilwerte_schuetze_match.pfeilwerte_schuetze_match_pfeilwerte6;

alter table schuetzenstatistik
    owner to swt2;

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
