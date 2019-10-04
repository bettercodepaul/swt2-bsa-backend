-- das wird jetzt ein wenig komplexer: da wir die Vereine um Dublikate
-- bereinigt haben, ist die Zuordnung Schütze-Mannschaft nicht migirert.
-- d.h. wir joinen über die "vereine"-Tabelle und dann erst auf die migierten Vereine
-- und zwar auf den eindeutigen Vereinsnamen um die Zuorndung zu erhalten...

--  löschen korrupter Daten - Geburtsdatum leer
-- in den produtkionsnahen testdaten gibt es fünf Fälle, davon sind 4 doppelte Einträge

delete from prod_data_migration."Schützentabelle"
where "SchGebDat"=''
OR "SchGebDat"=' '
;


-- leider gibt es auch hier korrupte Daten (doppelte Mitgliedsnummer)
-- wir nehmen jeweils den ersten Eintrag
-- weiter ist die Nationalität nicht nachzuvollziehen
-- default auf DE gesetzt

DELETE from dsb_mitglied;

insert INTO dsb_mitglied
 ( dsb_mitglied_mitgliedsnummer,
   dsb_mitglied_vorname,
   dsb_mitglied_nachname,
   dsb_mitglied_geburtsdatum,
   dsb_mitglied_nationalitaet,
   dsb_mitglied_verein_id
   )
SELECT DISTINCT ON
  ("Schützentabelle"."Mitgliedsnummer") "Schützentabelle"."Mitgliedsnummer",
   split_part("Schützentabelle"."Schname", ',', 2),
   split_part("Schützentabelle"."Schname", ',', 1),
   to_date("Schützentabelle"."SchGebDat",'DD MM YYYY'),
   'DE',
   verein.verein_id
   FROM prod_data_migration."Schützentabelle", prod_data_migration."vereine", public.verein
   where "Schützentabelle"."SchVNR"="vereine"."VNR"
   and   "vereine"."Verein"=verein.verein_name
   AND "Schützentabelle"."SchGebDat"!=' '
   ORDER BY "Schützentabelle"."Mitgliedsnummer",
                 verein.verein_id
;


