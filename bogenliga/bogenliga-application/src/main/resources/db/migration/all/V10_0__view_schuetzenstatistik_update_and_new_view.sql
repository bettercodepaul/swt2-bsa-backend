Drop VIEW if exists schuetzenstatistik;
drop VIEW if exists pfeilwerte_schuetze_match;
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
                                                                               ELSE 0
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
