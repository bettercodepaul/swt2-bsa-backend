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
        sum(COALESCE(passe.passe_ringzahl_pfeil6, 0::numeric))) /
       GREATEST( 1, (sum(
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
                                                                               END)))::numeric             AS pfeilwerte_schuetze_match_pfeilwert_schnitt
FROM match,
     dsb_mitglied,
     passe
WHERE   passe.passe_match_id = match.match_id
  AND passe.passe_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
GROUP BY match.match_id, dsb_mitglied.dsb_mitglied_id,
         (passe.passe_ringzahl_pfeil1 + passe.passe_ringzahl_pfeil2 + passe.passe_ringzahl_pfeil3 +
          passe.passe_ringzahl_pfeil4 + passe.passe_ringzahl_pfeil5 + passe.passe_ringzahl_pfeil6);



-- extend view, by dropping it first and then adding new coloumns (schuetze_satz1,...)
