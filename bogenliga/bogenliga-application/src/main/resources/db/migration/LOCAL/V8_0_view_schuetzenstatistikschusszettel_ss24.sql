select * from ligapasse;

select  schuetzenstatistik_schusszettel_dsb_mitglied_name as name,
        ARRAY_remove(schuetzenstatistik_schusszettel_satz1_pfeilwerte, NULL) as Satz1,
        ARRAY_remove(schuetzenstatistik_schusszettel_satz2_pfeilwerte, NULL) as Satz2,
        ARRAY_remove(schuetzenstatistik_schusszettel_satz3_pfeilwerte, NULL) as Satz3,
        ARRAY_remove(schuetzenstatistik_schusszettel_satz4_pfeilwerte, NULL) as Satz4,
        ARRAY_remove(schuetzenstatistik_schusszettel_satz5_pfeilwerte, NULL) as Satz5,
        schuetzenstatistik_schusszettel_wettkampf_tag as wettkampftag,
        schuetzenstatistik_schusszettel_match_nr as match_nr,
        schuetzenstatistik_schusszettel_wettkampf_datum as datum
from schuetzenstatistik_schusszettel where schuetzenstatistik_schusszettel_dsb_mitglied_id = 28;





Drop VIEW if exists schuetzenstatistik_schusszettel;


--- V32
create view schuetzenstatistik_schusszettel
            (schuetzenstatistik_schusszettel_veranstaltung_id, schuetzenstatistik_schusszettel_veranstaltung_name,
             schuetzenstatistik_schusszettel_wettkampf_id, schuetzenstatistik_schusszettel_wettkampf_tag,
             schuetzenstatistik_schusszettel_mannschaft_id, schuetzenstatistik_schusszettel_mannschaft_nummer,
             schuetzenstatistik_schusszettel_verein_id, schuetzenstatistik_schusszettel_verein_name,
             schuetzenstatistik_schusszettel_match_id, schuetzenstatistik_schusszettel_match_nr,
             schuetzenstatistik_schusszettel_dsb_mitglied_id, schuetzenstatistik_schusszettel_dsb_mitglied_name,
             schuetzenstatistik_schusszettel_rueckennummer,
             schuetzenstatistik_schusszettel_wettkampf_datum,
             schuetzenstatistik_schusszettel_satz1_pfeilwerte,
             schuetzenstatistik_schusszettel_satz2_pfeilwerte,
             schuetzenstatistik_schusszettel_satz3_pfeilwerte,
             schuetzenstatistik_schusszettel_satz4_pfeilwerte,
             schuetzenstatistik_schusszettel_satz5_pfeilwerte,
             schuetzenstatistik_schusszettel_pfeilschuss1_werte ,
             schuetzenstatistik_schusszettel_pfeilschuss2_werte,
             schuetzenstatistik_schusszettel_pfeilschuss3_werte,
             schuetzenstatistik_schusszettel_pfeilschuss4_werte,
             schuetzenstatistik_schusszettel_pfeilschuss5_werte,
             schuetzenstatistik_schusszettel_pfeilschuss6_werte)
as
SELECT veranstaltung.veranstaltung_id                                                                     AS schuetzenstatistik_schusszettel_veranstaltung_id,
       veranstaltung.veranstaltung_name                                                                   AS schuetzenstatistik_schusszettel_veranstaltung_name,
       wettkampf.wettkampf_id                                                                             AS schuetzenstatistik_schusszettel_wettkampf_id,
       wettkampf.wettkampf_tag                                                                            AS schuetzenstatistik_schusszettel_wettkampf_tag,
       mannschaft.mannschaft_id                                                                           AS schuetzenstatistik_schusszettel_mannschaft_id,
       mannschaft.mannschaft_nummer                                                                       AS schuetzenstatistik_schusszettel_mannschaft_nummer,
       verein.verein_id                                                                                   AS schuetzenstatistik_schusszettel_verein_id,
       verein.verein_name                                                                                 AS schuetzenstatistik_schusszettel_verein_name,
       match.match_id                                                                                     AS schuetzenstatistik_schusszettel_match_id,
       match.match_nr                                                                                     AS schuetzenstatistik_schusszettel_match_nr,
       dsb_mitglied.dsb_mitglied_id                                                                       AS schuetzenstatistik_schusszettel_dsb_mitglied_id,
       (dsb_mitglied.dsb_mitglied_vorname::text || ' '::text) ||
       dsb_mitglied.dsb_mitglied_nachname::text                                                           AS schuetzenstatistik_schusszettel_dsb_mitglied_name,
       mannschaftsmitglied.mannschaftsmitglied_rueckennummer                                              AS schuetzenstatistik_schusszettel_rueckennummer,
       wettkampf.wettkampf_datum                                                                          AS schuetzenstatistik_schusszettel_wettkampf_datum,

       ARRAY[
           (SELECT array_agg(passe.passe_ringzahl_pfeil1))[1], (SELECT array_agg(passe.passe_ringzahl_pfeil2))[1],
           (SELECT array_agg(passe.passe_ringzahl_pfeil3))[1], (SELECT array_agg(passe.passe_ringzahl_pfeil4))[1],
            (SELECT array_agg(passe.passe_ringzahl_pfeil5))[1], (SELECT array_agg(passe.passe_ringzahl_pfeil6))[1]]
        AS schuetzenstatistik_schusszettel_satz1_pfeilwerte,
       ARRAY[
           (SELECT array_agg(passe.passe_ringzahl_pfeil1))[2], (SELECT array_agg(passe.passe_ringzahl_pfeil2))[2],
           (SELECT array_agg(passe.passe_ringzahl_pfeil3))[2], (SELECT array_agg(passe.passe_ringzahl_pfeil4))[2],
           (SELECT array_agg(passe.passe_ringzahl_pfeil5))[2], (SELECT array_agg(passe.passe_ringzahl_pfeil6))[2]]
           AS schuetzenstatistik_schusszettel_satz2_pfeilwerte,
       ARRAY[
           (SELECT array_agg(passe.passe_ringzahl_pfeil1))[3], (SELECT array_agg(passe.passe_ringzahl_pfeil2))[3],
           (SELECT array_agg(passe.passe_ringzahl_pfeil3))[3], (SELECT array_agg(passe.passe_ringzahl_pfeil4))[3],
           (SELECT array_agg(passe.passe_ringzahl_pfeil5))[3], (SELECT array_agg(passe.passe_ringzahl_pfeil6))[3]]
           AS schuetzenstatistik_schusszettel_satz3_pfeilwerte,
       ARRAY[
           (SELECT array_agg(passe.passe_ringzahl_pfeil1))[4], (SELECT array_agg(passe.passe_ringzahl_pfeil2))[4],
           (SELECT array_agg(passe.passe_ringzahl_pfeil3))[4], (SELECT array_agg(passe.passe_ringzahl_pfeil4))[4],
           (SELECT array_agg(passe.passe_ringzahl_pfeil5))[4], (SELECT array_agg(passe.passe_ringzahl_pfeil6))[4]]
           AS schuetzenstatistik_schusszettel_satz4_pfeilwerte,
       ARRAY[
           (SELECT array_agg(passe.passe_ringzahl_pfeil1))[5], (SELECT array_agg(passe.passe_ringzahl_pfeil2))[5],
           (SELECT array_agg(passe.passe_ringzahl_pfeil3))[5], (SELECT array_agg(passe.passe_ringzahl_pfeil4))[5],
           (SELECT array_agg(passe.passe_ringzahl_pfeil5))[5], (SELECT array_agg(passe.passe_ringzahl_pfeil6))[5]]
           AS schuetzenstatistik_schusszettel_satz5_pfeilwerte,



       array_agg(passe.passe_ringzahl_pfeil1)                             AS schuetzenstatistik_schusszettel_pfeilschuss1_werte,
       array_agg(passe.passe_ringzahl_pfeil2)                             AS schuetzenstatistik_schusszettel_pfeilschuss2_werte,
       array_agg(passe.passe_ringzahl_pfeil3)                             AS schuetzenstatistik_schusszettel_pfeilschuss3_werte,
       array_agg(passe.passe_ringzahl_pfeil4)                             AS schuetzenstatistik_schusszettel_pfeilschuss4_werte,
       array_agg(passe.passe_ringzahl_pfeil5)                             AS schuetzenstatistik_schusszettel_pfeilschuss5_werte,
       array_agg(passe.passe_ringzahl_pfeil6)                             AS schuetzenstatistik_schusszettel_pfeilschuss6_werte,
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
                                                                               END))::numeric             AS schuetzenstatistik_schusszettel_pfeilpunkte_schnitt
FROM match,
     veranstaltung,
     wettkampf,
     mannschaft,
     mannschaftsmitglied,
     verein,
     dsb_mitglied,
     passe,
     match match_gegen
WHERE match.match_wettkampf_id = wettkampf.wettkampf_id
  AND wettkampf.wettkampf_veranstaltung_id = veranstaltung.veranstaltung_id
  AND match.match_mannschaft_id = mannschaft.mannschaft_id
  AND mannschaft.mannschaft_verein_id = verein.verein_id
  AND match.match_wettkampf_id = match_gegen.match_wettkampf_id
  AND match.match_nr = match_gegen.match_nr
  AND passe.passe_match_nr = match.match_nr
  AND mannschaftsmitglied_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
  AND mannschaftsmitglied.mannschaftsmitglied_mannschaft_id = mannschaft.mannschaft_id
  AND passe.passe_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
  AND passe.passe_wettkampf_id = wettkampf.wettkampf_id
  AND passe.passe_mannschaft_id = mannschaft.mannschaft_id
  AND match.match_begegnung = match_gegen.match_begegnung
  AND match.match_mannschaft_id <> match_gegen.match_mannschaft_id
GROUP BY veranstaltung.veranstaltung_id, veranstaltung.veranstaltung_name, wettkampf.wettkampf_id,
         wettkampf.wettkampf_tag, mannschaft.mannschaft_id, mannschaft.mannschaft_nummer, verein.verein_id,
         verein.verein_name, match.match_id, match.match_nr, dsb_mitglied.dsb_mitglied_id,
         ((dsb_mitglied.dsb_mitglied_vorname::text || ' '::text) || dsb_mitglied.dsb_mitglied_nachname::text),
         mannschaftsmitglied.mannschaftsmitglied_rueckennummer,
         (passe.passe_ringzahl_pfeil1 + passe.passe_ringzahl_pfeil2 + passe.passe_ringzahl_pfeil3 +
          passe.passe_ringzahl_pfeil4 + passe.passe_ringzahl_pfeil5 + passe.passe_ringzahl_pfeil6)
ORDER BY ((sum(COALESCE(passe.passe_ringzahl_pfeil1, 0::numeric)) +
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
                                                                                  END))::numeric) DESC;

select * from schuetzenstatistik;


drop view if exists schuetzenstatistik;

--- V32
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
           (SELECT array_agg(passe.passe_ringzahl_pfeil1))[1], (SELECT array_agg(passe.passe_ringzahl_pfeil2))[1],
           (SELECT array_agg(passe.passe_ringzahl_pfeil3))[1], (SELECT array_agg(passe.passe_ringzahl_pfeil4))[1],
           (SELECT array_agg(passe.passe_ringzahl_pfeil5))[1], (SELECT array_agg(passe.passe_ringzahl_pfeil6))[1]]
                                                                                                          AS schuetzenstatistik_schuetze_satz1,
       ARRAY[
           (SELECT array_agg(passe.passe_ringzahl_pfeil1))[2], (SELECT array_agg(passe.passe_ringzahl_pfeil2))[2],
           (SELECT array_agg(passe.passe_ringzahl_pfeil3))[2], (SELECT array_agg(passe.passe_ringzahl_pfeil4))[2],
           (SELECT array_agg(passe.passe_ringzahl_pfeil5))[2], (SELECT array_agg(passe.passe_ringzahl_pfeil6))[2]]
                                                                                                          AS schuetzenstatistik_schuetze_satz2,
       ARRAY[
           (SELECT array_agg(passe.passe_ringzahl_pfeil1))[3], (SELECT array_agg(passe.passe_ringzahl_pfeil2))[3],
           (SELECT array_agg(passe.passe_ringzahl_pfeil3))[3], (SELECT array_agg(passe.passe_ringzahl_pfeil4))[3],
           (SELECT array_agg(passe.passe_ringzahl_pfeil5))[3], (SELECT array_agg(passe.passe_ringzahl_pfeil6))[3]]
                                                                                                          AS schuetzenstatistik_schuetze_satz3,
       ARRAY[
           (SELECT array_agg(passe.passe_ringzahl_pfeil1))[4], (SELECT array_agg(passe.passe_ringzahl_pfeil2))[4],
           (SELECT array_agg(passe.passe_ringzahl_pfeil3))[4], (SELECT array_agg(passe.passe_ringzahl_pfeil4))[4],
           (SELECT array_agg(passe.passe_ringzahl_pfeil5))[4], (SELECT array_agg(passe.passe_ringzahl_pfeil6))[4]]
                                                                                                          AS schuetzenstatistik_schuetze_satz4,
       ARRAY[
           (SELECT array_agg(passe.passe_ringzahl_pfeil1))[5], (SELECT array_agg(passe.passe_ringzahl_pfeil2))[5],
           (SELECT array_agg(passe.passe_ringzahl_pfeil3))[5], (SELECT array_agg(passe.passe_ringzahl_pfeil4))[5],
           (SELECT array_agg(passe.passe_ringzahl_pfeil5))[5], (SELECT array_agg(passe.passe_ringzahl_pfeil6))[5]]
                                                                                                          AS schuetzenstatistik_schuetze_satz5,

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
                                                                               END))::numeric             AS schuetzenstatistik_pfeilpunkte_schnitt
FROM match,
     veranstaltung,
     wettkampf,
     mannschaft,
     mannschaftsmitglied,
     verein,
     dsb_mitglied,
     passe,
     match match_gegen
WHERE match.match_wettkampf_id = wettkampf.wettkampf_id
  AND wettkampf.wettkampf_veranstaltung_id = veranstaltung.veranstaltung_id
  AND match.match_mannschaft_id = mannschaft.mannschaft_id
  AND mannschaft.mannschaft_verein_id = verein.verein_id
  AND match.match_wettkampf_id = match_gegen.match_wettkampf_id
  AND match.match_nr = match_gegen.match_nr
  AND passe.passe_match_nr = match.match_nr
  AND mannschaftsmitglied_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
  AND mannschaftsmitglied.mannschaftsmitglied_mannschaft_id = mannschaft.mannschaft_id
  AND passe.passe_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
  AND passe.passe_wettkampf_id = wettkampf.wettkampf_id
  AND passe.passe_mannschaft_id = mannschaft.mannschaft_id
  AND match.match_begegnung = match_gegen.match_begegnung
  AND match.match_mannschaft_id <> match_gegen.match_mannschaft_id
GROUP BY veranstaltung.veranstaltung_id, veranstaltung.veranstaltung_name, wettkampf.wettkampf_id,
         wettkampf.wettkampf_tag, mannschaft.mannschaft_id, mannschaft.mannschaft_nummer, verein.verein_id,
         verein.verein_name, match.match_id, match.match_nr, dsb_mitglied.dsb_mitglied_id,
         ((dsb_mitglied.dsb_mitglied_vorname::text || ' '::text) || dsb_mitglied.dsb_mitglied_nachname::text),
         mannschaftsmitglied.mannschaftsmitglied_rueckennummer,
         (passe.passe_ringzahl_pfeil1 + passe.passe_ringzahl_pfeil2 + passe.passe_ringzahl_pfeil3 +
          passe.passe_ringzahl_pfeil4 + passe.passe_ringzahl_pfeil5 + passe.passe_ringzahl_pfeil6)
ORDER BY ((sum(COALESCE(passe.passe_ringzahl_pfeil1, 0::numeric)) +
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
                                                                                  END))::numeric) DESC;

alter table schuetzenstatistik
    owner to swt2;
