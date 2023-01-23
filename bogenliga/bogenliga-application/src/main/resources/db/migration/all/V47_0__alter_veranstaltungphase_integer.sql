ALTER TABLE veranstaltung
DROP COLUMN IF EXISTS veranstaltung_phase;

alter table veranstaltung
ADD COLUMN veranstaltung_phase int
default 1;