-- rework ligatabelle, since it contains basic problems, is only a temporary fix: ignores all the match ids that are separated more than 1000
-- Open problem: mannschaften have at least 2 matches for one actual match
-- if there is a fix for that problem: remove the following 2 conditions:
--         AND ma.match_id - (SELECT MIN(sm.match_id) FROM match sm WHERE sm.match_mannschaft_id = m.mannschaft_id AND sm.match_wettkampf_id = w.wettkampf_id) < 1000
--         AND mavs.match_id - (SELECT MIN(smavs.match_id) FROM match smavs WHERE smavs.match_mannschaft_id = mavs.match_mannschaft_id AND smavs.match_wettkampf_id = w.wettkampf_id) < 500
-- Comment: the added conditions won't affect the correctness results of the normal ligatabelle
DROP VIEW IF EXISTS ligatabelle;

CREATE VIEW ligatabelle (
                         ligatabelle_veranstaltung_id,      -- Bezug zum Sportjahr
                         ligatabelle_veranstaltung_name,    -- Name der Veranstaltung
                         ligatabelle_wettkampf_id,          -- Daten des einzelnen Wettkampfs
                         ligatabelle_wettkampf_tag,         -- Liga hat 4 Wettkampftage, initiale Ligatabelle für Tag 0
                         ligatabelle_mannschaft_id,         -- Bezug zur Mannschaft
                         ligatabelle_mannschaft_nummer,     -- Nummer der Mannschaft
                         ligatabelle_verein_id,             -- Bezug zum Verein
                         ligatabelle_verein_name,           -- Name des Vereins
                         ligatabelle_matchpkt,              -- Summe aller eigene Matchpunkte
                         ligatabelle_matchpkt_gegen,        -- Summe aller gegnerischen Matchpunkte
                         ligatabelle_satzpkt,               -- Summe aller eigenen Satzpunkte
                         ligatabelle_satzpkt_gegen,         -- Summe aller gegnerischen Satzpunkte
                         ligatabelle_satzpkt_differenz,     -- Differrenz der Satzpunkte
                         ligatabelle_sortierung,            -- editierbares Attribut der Mannschaft für Sortierung bei Punkte-Gleichstand
                         ligatabelle_match_count            -- Anzahl der Matches pro Mannschaft
    )
AS (
    SELECT
            v.veranstaltung_id,
            v.veranstaltung_name,
            w.wettkampf_id,
            w.wettkampf_tag,
            m.mannschaft_id,
            m.mannschaft_nummer,
            m.mannschaft_verein_id,
            ver.verein_name,
            sum(ma.match_matchpunkte) as matchpkt,
            sum(mavs.match_matchpunkte) as matchpkt_gegen,
            sum(ma.match_satzpunkte) as satzpkt,
            sum(mavs.match_satzpunkte) as satzpkt_gegen,
            (sum(ma.match_satzpunkte) - sum(mavs.match_satzpunkte)) as satzpkt_differenz,
            m.mannschaft_sortierung,
            COUNT(ma.match_id) AS ligatabelle_match_count


   FROM veranstaltung v
        JOIN wettkampf w    ON v.veranstaltung_id = w.wettkampf_veranstaltung_id
        JOIN mannschaft m   ON v.veranstaltung_id = m.mannschaft_veranstaltung_id
        JOIN verein ver     ON m.mannschaft_verein_id = ver.verein_id
        JOIN match ma       ON m.mannschaft_id = ma.match_mannschaft_id
        JOIN match mavs     ON mavs.match_wettkampf_id = w.wettkampf_id


    WHERE
            ma.match_wettkampf_id = w.wettkampf_id
        AND mavs.match_begegnung = ma.match_begegnung
        AND mavs.match_mannschaft_id != ma.match_mannschaft_id
        AND mavs.match_nr = ma.match_nr
        AND ma.match_id - (SELECT MIN(sm.match_id) FROM match sm WHERE sm.match_mannschaft_id = m.mannschaft_id AND sm.match_wettkampf_id = w.wettkampf_id) < 1000
        AND mavs.match_id - (SELECT MIN(smavs.match_id) FROM match smavs WHERE smavs.match_mannschaft_id = mavs.match_mannschaft_id AND smavs.match_wettkampf_id = w.wettkampf_id) < 1000

   GROUP BY v.veranstaltung_id, v.veranstaltung_name, w.wettkampf_id, w.wettkampf_tag, m.mannschaft_id, m.mannschaft_nummer, m.mannschaft_verein_id, ver.verein_name
)
