ALTER TABLE veranstaltung
    DROP COLUMN IF EXISTS veranstaltung_groesse;

ALTER TABLE veranstaltung
    ADD COLUMN veranstaltung_groesse INT DEFAULT 8
        CHECK ( veranstaltung_groesse = 4 OR veranstaltung_groesse = 6 OR veranstaltung_groesse = 8 );