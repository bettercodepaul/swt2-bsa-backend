drop view schuetzenstatistik;

create view schuetzenstatistik
            (schuetzenstatistik_veranstaltung_id, schuetzenstatistik_veranstaltung_name,
             schuetzenstatistik_wettkampf_id, schuetzenstatistik_wettkampf_tag, schuetzenstatistik_mannschaft_id,
             schuetzenstatistik_mannschaft_nummer, schuetzenstatistik_verein_id, schuetzenstatistik_verein_name,
             schuetzenstatistik_match_id, schuetzenstatistik_match_nr, schuetzenstatistik_dsb_mitglied_id, schuetzenstatistik_dsb_mitglied_name,
             schuetzenstatistik_rueckennummer,
             schuetzenstatistik_pfeilpunkte_schnitt)
as
SELECT veranstaltung.veranstaltung_id                                                                     AS schuetzenstatistik_veranstaltung_id,
       veranstaltung.veranstaltung_name                                                                   AS schuetzenstatistik_veranstaltung_name,
       wettkampf.wettkampf_id                                                                             AS schuetzenstatistik_wettkampf_id,
       wettkampf.wettkampf_tag                                                                            AS schuetzenstatistik_wettkampf_tag,
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

