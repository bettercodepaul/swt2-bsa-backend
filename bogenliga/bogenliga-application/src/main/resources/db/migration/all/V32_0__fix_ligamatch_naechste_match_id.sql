-- Migration to fix next match id calculation in ligamatch view
-- Drop existing view to allow clean recreation
DROP VIEW IF EXISTS ligamatch CASCADE;

CREATE VIEW ligamatch (
                       ligamatch_match_wettkampf_id,
                       ligamatch_match_id,
                       ligamatch_match_nr,
                       ligamatch_match_scheibennummer,
                       ligamatch_match_mannschaft_id,
                       ligamatch_mannschaft_name,
                       ligamatch_mannschaft__name_gegner,
                       ligamatch_match_scheibennummer_gegner,
                       ligamatch_match_id_gegner,
                       ligamatch_naechste_match_id,
                       ligamatch_naechste_naechste_match_nr_match_id,
                       ligamatch_match_strafpunkte_satz_1,
                       ligamatch_match_strafpunkte_satz_2,
                       ligamatch_match_strafpunkte_satz_3,
                       ligamatch_match_strafpunkte_satz_4,
                       ligamatch_match_strafpunkte_satz_5,
                       ligamatch_begegnung,
                       ligamatch_wettkampftyp_id,
                       ligamatch_wettkampf_tag,
                       ligamatch_satzpunkte,
                       ligamatch_matchpunkte
    )
AS
(
select match1.match_wettkampf_id,
       match1.match_id,
       match1.match_nr,
       match1.match_scheibennummer,
       match1.match_mannschaft_id,
       verein.verein_name || ' ' || mannschaft.mannschaft_nummer   as mannschaft_name,
       verein3.verein_name || ' ' || mannschaft3.mannschaft_nummer as mannschaft__name_gegner,
       match3.match_scheibennummer                                 as scheibennummer_gegner,
       match3.match_id                                             as match_id_gegner,
       (SELECT m2.match_id
        FROM match m2
        JOIN wettkampf w2 ON m2.match_wettkampf_id = w2.wettkampf_id
        JOIN wettkampf w1 ON match1.match_wettkampf_id = w1.wettkampf_id
        WHERE m2.match_mannschaft_id = match1.match_mannschaft_id
          AND w2.wettkampf_veranstaltung_id = w1.wettkampf_veranstaltung_id
          AND (w2.wettkampf_tag > w1.wettkampf_tag
               OR (w2.wettkampf_tag = w1.wettkampf_tag AND m2.match_nr > match1.match_nr))
        ORDER BY w2.wettkampf_tag ASC, m2.match_nr ASC
        LIMIT 1)                                                   as naechste_match_id,
       match4.match_id                                             as naechste_match_nr_match_id,
       match1.match_strafpunkte_satz_1,
       match1.match_strafpunkte_satz_2,
       match1.match_strafpunkte_satz_3,
       match1.match_strafpunkte_satz_4,
       match1.match_strafpunkte_satz_5,
       match1.match_begegnung,
       wett.wettkampf_wettkampftyp_id,
       wett.wettkampf_tag,
       match1.match_satzpunkte,
       match1.match_matchpunkte

from match as match1
     join mannschaft as mannschaft on match1.match_mannschaft_id = mannschaft.mannschaft_id
     join verein as verein on mannschaft.mannschaft_verein_id = verein.verein_id
     join wettkampf as wettkampf on match1.match_wettkampf_id = wettkampf.wettkampf_id
     join veranstaltung as veranstaltung on wettkampf.wettkampf_veranstaltung_id = veranstaltung.veranstaltung_id
     join match as match3 on match3.match_wettkampf_id = match1.match_wettkampf_id
         and match3.match_nr = match1.match_nr
         and match3.match_begegnung = match1.match_begegnung
         and match3.match_mannschaft_id <> match1.match_mannschaft_id
     join mannschaft as mannschaft3 on match3.match_mannschaft_id = mannschaft3.mannschaft_id
     join verein as verein3 on mannschaft3.mannschaft_verein_id = verein3.verein_id
     join wettkampf as wett on wett.wettkampf_id = match1.match_wettkampf_id
     left join match as match4 on match4.match_wettkampf_id = match1.match_wettkampf_id
         and match4.match_nr = CASE
                                   WHEN veranstaltung.veranstaltung_groesse IN (8, 6) THEN LEAST(match1.match_nr + 1, veranstaltung.veranstaltung_groesse - 1)
                                   WHEN veranstaltung.veranstaltung_groesse = 4 THEN LEAST(match1.match_nr + 1, 6)
             END
         and match4.match_scheibennummer = 1
order by match1.match_wettkampf_id, match1.match_nr, match1.match_scheibennummer
);
