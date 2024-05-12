CREATE VIEW fullscreen_ligatabelle AS
SELECT
    veranstaltung_id AS ligatabelle_veranstaltung_id,
    veranstaltung_name AS ligatabelle_veranstaltung_name,
    wettkampf_id AS ligatabelle_wettkampf_id,
    wettkampf_tag AS ligatabelle_wettkampf_tag,
    mannschaft_id AS ligatabelle_mannschaft_id,
    mannschaft_nummer AS ligatabelle_mannschaft_nummer,
    verein_id AS ligatabelle_verein_id,
    verein_name AS ligatabelle_verein_name,
    SUM(match.match_matchpunkte) AS ligatabelle_matchpkt,
    SUM(match_gegen.match_matchpunkte) AS ligatabelle_matchpkt_gegen,
    SUM(match.match_satzpunkte) AS ligatabelle_satzpkt,
    SUM(match_gegen.match_satzpunkte) AS ligatabelle_satzpkt_gegen,
    (SUM(match.match_satzpunkte) - SUM(match_gegen.match_satzpunkte)) AS ligatabelle_satzpkt_differenz,
    mannschaft_sortierung AS ligatabelle_sortierung,
    COUNT(match.match_id) AS ligatabelle_match_count
FROM
    match
        INNER JOIN wettkampf ON match.match_wettkampf_id = wettkampf.wettkampf_id
        INNER JOIN veranstaltung ON wettkampf.wettkampf_veranstaltung_id = veranstaltung.veranstaltung_id
        INNER JOIN mannschaft ON match.match_mannschaft_id = mannschaft.mannschaft_id
        INNER JOIN verein ON mannschaft.mannschaft_verein_id = verein.verein_id
        INNER JOIN match AS match_gegen ON match.match_wettkampf_id = match_gegen.match_wettkampf_id
        AND match.match_nr = match_gegen.match_nr
        AND match.match_begegnung = match_gegen.match_begegnung
        AND match.match_mannschaft_id != match_gegen.match_mannschaft_id
GROUP BY
    veranstaltung_id,
    veranstaltung_name,
    wettkampf_id,
    wettkampf_tag,
    mannschaft_id,
    mannschaft_nummer,
    verein_id,
    verein_name,
    mannschaft_sortierung;
