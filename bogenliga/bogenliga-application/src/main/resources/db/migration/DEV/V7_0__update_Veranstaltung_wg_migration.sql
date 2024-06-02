/*Geplant = 1, Laufend = 2, Abgeschlossen = 3
wir überarbeiten nur Veranstaltungen die aktuell im Status "geplant" sind
  da die Migration vor dem Bugfix alle Veranstaltungen mit Phase geplant
  angelegt hat...*/

-- die geplanten müssen nicht geändert werden...

-- die abgeschlossenen haben ein Sportjahr vor/kleiner dem aktiven Sportjahr
update veranstaltung
set veranstaltung_phase = 3
    from (select veranstaltung_id from veranstaltung, configuration
      where veranstaltung_sportjahr < CAST(configuration_value AS NUMERIC)
        and configuration_key = 'aktives-Sportjahr'
        and veranstaltung_phase = 1) as subquery
where subquery.veranstaltung_id = veranstaltung.veranstaltung_id;

-- und  die laufenden sportjahr = aktives Sportjahr
update veranstaltung
set veranstaltung_phase = 2
    from (select veranstaltung_id from veranstaltung, configuration
      where veranstaltung_sportjahr = CAST(configuration_value AS NUMERIC)
        and configuration_key = 'aktives-Sportjahr'
        and veranstaltung_phase = 1) as subquery
where subquery.veranstaltung_id = veranstaltung.veranstaltung_id;