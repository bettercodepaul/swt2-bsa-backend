Drop VIEW if exists ligamatch;

/* the view includes the name of the contestent - it by be diaplayed as a tooltip
 * to read the data for a single schusszettel make sure to read all entries
 * for a given combination of wettkampf_id and match_id
 * data is ordered by passe_lfdnr and rueckennummer
 */


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
       --      match2.match_id as naechstes_match,
       verein3.verein_name || ' ' || mannschaft3.mannschaft_nummer as mannschaft__name_gegner,
       match3.match_scheibennummer                                 as scheibennummer_gegner,
       match3.match_id                                             as match_id_gegner,
       match2.match_id                                             as naechste_match_id,
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

from match as match1,
     mannschaft as mannschaft,
     verein as verein,
     veranstaltung as veranstaltung,
     wettkampf as wettkampf,
     match as match2,
     match as match3,
     mannschaft as mannschaft3,
     verein as verein3,
     match as match4,
     wettkampf as wett

where match1.match_mannschaft_id = mannschaft.mannschaft_id
  and mannschaft.mannschaft_verein_id = verein.verein_id
  and match3.match_wettkampf_id = match1.match_wettkampf_id
  and match3.match_nr = match1.match_nr
  and match3.match_begegnung = match1.match_begegnung
  and match3.match_mannschaft_id <> match1.match_mannschaft_id
  and match3.match_mannschaft_id = mannschaft3.mannschaft_id
  and mannschaft3.mannschaft_verein_id = verein3.verein_id
  and match1.match_wettkampf_id = wettkampf.wettkampf_id
  and wettkampf.wettkampf_veranstaltung_id = veranstaltung.veranstaltung_id
  and match2.match_wettkampf_id = match1.match_wettkampf_id
  and match2.match_nr = match1.match_nr
  and match2.match_scheibennummer = LEAST(match1.match_scheibennummer + 1, veranstaltung.veranstaltung_groesse)
  and match4.match_wettkampf_id = match1.match_wettkampf_id
  and match4.match_nr = CASE
                            WHEN veranstaltung.veranstaltung_groesse IN (8, 6) THEN LEAST(match1.match_nr + 1, veranstaltung.veranstaltung_groesse - 1)
                            WHEN veranstaltung.veranstaltung_groesse = 4 THEN LEAST(match1.match_nr + 1, 6)
                        END
  and match4.match_scheibennummer = 1
  and wett.wettkampf_id = match1.match_wettkampf_id
order by match1.match_wettkampf_id, match1.match_nr, match1.match_scheibennummer
    )
;