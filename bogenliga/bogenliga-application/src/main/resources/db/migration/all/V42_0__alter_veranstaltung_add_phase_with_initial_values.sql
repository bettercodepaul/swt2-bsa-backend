CREATE TYPE phase AS ENUM ('Geplant', 'Laufend','Abgeschlossen');

 alter table veranstaltung
 add veranstaltung_phase phase
 default 'Geplant';
