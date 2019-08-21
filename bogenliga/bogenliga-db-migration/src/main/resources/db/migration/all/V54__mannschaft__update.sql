-- die Verbindung zur Liga ist ebim Anlegen/ Ändern einer Mannschaft keine Pflicht
-- der Ligaleiter oder... können die Mannschaft später der Veranstaltung zuordnen

ALTER TABLE mannschaft
    ALTER COLUMN
        -- drop a require veranstaltung - you may define and set up teams and
      -- bind them to a veranstaltung later (or by a ligaleiter)
        mannschaft_veranstaltung_id DROP NOT NULL

;

