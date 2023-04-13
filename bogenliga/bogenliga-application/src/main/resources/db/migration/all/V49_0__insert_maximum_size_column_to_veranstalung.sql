ALTER TABLE veranstaltung
DROP COLUMN IF EXISTS veranstaltung_groesse;

ALTER TABLE veranstaltung
ADD COLUMN veranstaltung_groesse ENUM(4, 6, 8)
default 8;