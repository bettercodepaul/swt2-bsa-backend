-- lizenzen erstellen fuer alle mannschaftsmitglieder, die noch keine haben
INSERT INTO lizenz (
    lizenz_nummer,
    lizenz_region_id,
    lizenz_dsb_mitglied_id,
    lizenz_typ,
    lizenz_disziplin_id
    )
-- no duplicates, everyone can only have one liga license, but can have kampfrichter and liga:
SELECT DISTINCT (
        r.region_kuerzel ||
        CAST(m.mannschaft_veranstaltung_id AS TEXT) ||
        CAST(m.mannschaft_verein_id AS TEXT) ||
        CAST(m.mannschaft_nummer AS TEXT) ||
        CAST(mg.mannschaftsmitglied_dsb_mitglied_id AS TEXT)
    ) AS lizenz_nummer,
    v.verein_region_id AS lizenz_region_id,
    mg.mannschaftsmitglied_dsb_mitglied_id AS lizenz_dsb_mitglied_id,
    'Liga' AS lizenz_typ,
    w.wettkampf_disziplin_id AS lizenz_disziplin_id

FROM verein v JOIN mannschaft m ON v.verein_id = m.mannschaft_verein_id
              JOIN mannschaftsmitglied mg ON m.mannschaft_id = mg.mannschaftsmitglied_mannschaft_id
              JOIN region r ON v.verein_region_id = r.region_id
              JOIN wettkampf w ON w.wettkampf_veranstaltung_id = m.mannschaft_veranstaltung_id

WHERE NOT EXISTS (
        SELECT *
        FROM lizenz l
        WHERE l.lizenz_dsb_mitglied_id = mg.mannschaftsmitglied_dsb_mitglied_id
          AND l.lizenz_typ = 'Liga'
    )
;
