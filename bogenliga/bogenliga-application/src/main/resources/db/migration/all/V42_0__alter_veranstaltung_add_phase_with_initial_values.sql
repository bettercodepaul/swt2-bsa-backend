-- Add new phase element with initial value Geplant as enum
CREATE TYPE phase AS ENUM ('Geplant', 'Laufend','Abgeschlossen');

alter table veranstaltung
add veranstaltung_phase phase
default 'Geplant';