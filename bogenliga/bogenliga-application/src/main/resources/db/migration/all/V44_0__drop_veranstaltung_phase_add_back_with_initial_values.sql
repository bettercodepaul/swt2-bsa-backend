-- Add new phase element with initial value Geplant

ALTER TABLE veranstaltung
DROP COLUMN IF EXISTS veranstaltung_phase;


alter table veranstaltung
add column veranstaltung_phase varchar(20)
default 'Geplant';