-- Add new phase element with initial value Geplant
alter table veranstaltung
add column veranstaltung_phase varchar(20)
default 'Geplant';