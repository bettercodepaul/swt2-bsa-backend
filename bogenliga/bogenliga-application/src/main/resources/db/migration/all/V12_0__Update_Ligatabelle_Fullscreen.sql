DROP VIEW IF EXISTS fullscreen_ligatabelle;

DROP VIEW IF EXISTS ligatabelle;

/**
 * Die Ligatabelle wird durch einen view auf die Tabelle Match ersetzt.
 * die Sortierung der Tabelle wird erst beim Lesen durch Generiern einer weiteren Spalte erzeugt.
 **/
CREATE VIEW ligatabelle (
                         ligatabelle_veranstaltung_id,     -- Bezug zum Sportjahr
                         ligatabelle_veranstaltung_name,   -- Name der Veranstaltung
                         ligatabelle_wettkampf_id,        -- Daten des einzelnen Wettkampfs
                         ligatabelle_wettkampf_tag,        -- Liga hat 4 Wettkampftage, initiale Ligatabelle für Tag 0
                         ligatabelle_mannschaft_id,        -- Bezug zur Mannschaft
                         ligatabelle_mannschaft_nummer,     -- Nummer der Mannschaft
                         ligatabelle_verein_id,             -- Bezug zum Verein
                         ligatabelle_verein_name,           -- Name des Vereins
                         ligatabelle_matchpkt,             -- Summe aller eigene Matchpunkte
                         ligatabelle_matchpkt_gegen,       -- Summe aller gegnerischen Matchpunkte
                         ligatabelle_satzpkt,              -- Summe aller eigenen Satzpunkte
                         ligatabelle_satzpkt_gegen,        -- Summe aller gegnerischen Satzpunkte
                         ligatabelle_satzpkt_differenz,    -- Differrenz der Satzpunkte
                         ligatabelle_sortierung            -- editierbares Attribut der Mannschaft für Sortierung bei Punkte-Gleichstand
    )
AS (
   select
       veranstaltung_id,
       veranstaltung_name,
       wettkampf_id,
       wettkampf_tag,
       mannschaft_id,
       mannschaft_nummer,
       verein_id,
       verein_name,
       sum(match.match_matchpunkte) as matchpkt,
       sum(match_gegen.match_matchpunkte) as matchpkt_gegen,
       sum(match.match_satzpunkte) as satzpkt,
       sum(match_gegen.match_satzpunkte) as satzpkt_gegen,
       (sum(match.match_satzpunkte) - sum(match_gegen.match_satzpunkte)) as satzpkt_differenz,
       mannschaft_sortierung,
       COUNT(match.match_id) AS ligatabelle_match_count

   from match, veranstaltung, wettkampf, mannschaft, verein, match as match_gegen
   where
       match.match_wettkampf_id = wettkampf_id
     and wettkampf_veranstaltung_id = veranstaltung_id
     and match.match_mannschaft_id = mannschaft_id
     and mannschaft_verein_id = verein_id
     and match.match_wettkampf_id = match_gegen.match_wettkampf_id
     and match.match_nr = match_gegen.match_nr
     and match.match_begegnung = match_gegen.match_begegnung
     and match.match_mannschaft_id != match_gegen.match_mannschaft_id
   group by veranstaltung_id,
       veranstaltung_name,
       wettkampf_id,
       wettkampf_tag,
       mannschaft_id,
       mannschaft_nummer,
       verein_id,
       verein_name,
       mannschaft_sortierung
       );

