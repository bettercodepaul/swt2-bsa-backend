alter table veranstaltung
 add veranstaltung_phase varchar(600);

 --Initial values
 update veranstaltung
 set veranstaltung_phase = 'Geplant'
 where veranstaltung_sportjahr >= (
         extract(year from CURRENT_DATE))
 and veranstaltung_meldedeadline > CURRENT_DATE;

 update veranstaltung
 set veranstaltung_phase = 'Laufend'
 where veranstaltung_sportjahr >= (
         extract(year from CURRENT_DATE))
 and veranstaltung_meldedeadline < CURRENT_DATE;

 update veranstaltung
 set veranstaltung_phase = 'Abgeschlossen'
 where veranstaltung_sportjahr < (
         extract(year from CURRENT_DATE))
 and veranstaltung_meldedeadline < CURRENT_DATE;
