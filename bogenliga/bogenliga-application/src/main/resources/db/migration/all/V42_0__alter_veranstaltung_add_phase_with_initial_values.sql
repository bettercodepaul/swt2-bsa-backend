alter table veranstaltung
add veranstaltung_phase varchar(20);

--Initial values
update veranstaltung
set veranstaltung_phase = 'Geplant'
where veranstaltung_sportjahr >= (
        select CAST(configuration_value as int)from configuration where configuration_key = 'aktives-Sportjahr')
and veranstaltung_meldedeadline > CURRENT_DATE;

update veranstaltung
set veranstaltung_phase = 'Laufend'
where veranstaltung_sportjahr >= (
        select CAST(configuration_value as int)from configuration where configuration_key = 'aktives-Sportjahr')
and veranstaltung_meldedeadline < CURRENT_DATE;

update veranstaltung
set veranstaltung_phase = 'Abgeschlossen'
where veranstaltung_sportjahr < (
        select CAST(configuration_value as int)from configuration where configuration_key = 'aktives-Sportjahr')
and veranstaltung_meldedeadline < CURRENT_DATE;