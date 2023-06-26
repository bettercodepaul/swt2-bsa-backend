/* we will create a view to table MATCH to enchance performance
*  when reading data for a single schusszettel
*  by creating a view inluding all relevant data for dialoge display
*  and the indicies to be able to save the data afterwards
*
* frist read the entry foir scheibenummer 1
* it will include the match_id for the "gegner" - match_id_gegner
* then read the data from ligapasse for both match_id's
*
* to read the next schusszettel check whether ligamatch_match_id
* and ligamatch_naechste_match_id are identical --> in this case you have to read data for the next
* match_nr --> the ID is available in ligamatch_naechste_naechste_match_nr_match_id
*
* in case the match_id are not identical start reading the next match by reading to for
* ligamatch_naechste_match_id
*
* data which will be calculated by dialoge functions (like Sum, Satzpunkte, Matchpunkte) will not be
* part of this table, as it is not required to display the data on the dialoge.
*/

/* in case the view is already existing, we will drop it first */
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
  and match2.match_wettkampf_id = match1.match_wettkampf_id
  and match2.match_nr = match1.match_nr
  and match2.match_scheibennummer = LEAST(match1.match_scheibennummer + 1, 4)
  and match4.match_wettkampf_id = match1.match_wettkampf_id
  and match4.match_nr = LEAST(match1.match_nr + 1, 5)
  and match4.match_scheibennummer = 1
  and wett.wettkampf_id = match1.match_wettkampf_id
order by match1.match_wettkampf_id, match1.match_nr, match1.match_scheibennummer
    )
;