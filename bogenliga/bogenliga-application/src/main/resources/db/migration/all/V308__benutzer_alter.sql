/* Prüfen ob Gero als DSB-Mitglied eingetragen ist - falls nicht, dann ergänzen*/
INSERT INTO dsb_mitglied (dsb_mitglied_vorname,
                          dsb_mitglied_nachname,
                          dsb_mitglied_geburtsdatum,
                          dsb_mitglied_nationalitaet,
                          dsb_mitglied_mitgliedsnummer,
                          dsb_mitglied_verein_id)
VALUES ('Gero', 'Gras', '1990-03-16', 'D', 'WT1009', 0)
ON CONFLICT DO NOTHING;

COMMIT;

/* neue Spalte in Tabelle einfügen mit Defaul-Wert */
ALTER TABLE benutzer
    ADD COLUMN benutzer_dsb_mitglied_id DECIMAL(19,0) DEFAULT 0;

COMMIT;

/* Neue Spalte korrekt befüllen - im ersten Schritt sind alle existierenden Benutzer == Gero */
UPDATE
    benutzer
SET
    benutzer_dsb_mitglied_id = T2.dsb_mitglied_id
FROM
    dsb_mitglied T2
WHERE
        T2.dsb_mitglied_nachname = 'Gras'
  AND T2.dsb_mitglied_vorname = 'Gero';

/* aus der bestehender Tabelle den CONTRAIN entfernen */
ALTER TABLE dsb_mitglied
    DROP CONSTRAINT IF EXISTS fk_dsb_mitglied_benutzer_id;

COMMIT;
/*später muss  mal die alte Spalte in der DSB-Mitgliedstabelle bereinigt werden
  aktuell lasse ich sie stehen um den Umbauaufwand klein zu halten. */

/* in die Benutzer-Tabelle den Constrain eintragen */
ALTER TABLE benutzer
    ADD CONSTRAINT fk_benutzer_dsb_mitglied_id
        FOREIGN KEY (benutzer_dsb_mitglied_id)
            REFERENCES dsb_mitglied (dsb_mitglied_id);