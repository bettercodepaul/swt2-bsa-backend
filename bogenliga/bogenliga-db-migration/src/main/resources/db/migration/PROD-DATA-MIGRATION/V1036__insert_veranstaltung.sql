-- für jede der angelegten Ligen benötigen wir eine Veranstaltung
-- das aktuelle Sportjahr liegt in "aktuellesaison"

-- Default für Wettkampfart Liga hinterlegen
-- Datenmigration nicht möglich, da Daten fehlerhaft, modellierung nicht nachjvollziehbar

-- Insert der Ligen manuell, keinen Migration wg. schlechter Datenqualität
-- Namen der Ligen kopiert --> Matching über Identität "Name" möglich.

SET search_path = 'prod'
;

insert INTO veranstaltung(
  veranstaltung_wettkampftyp_id,
  veranstaltung_name,
  veranstaltung_sportjahr,
  veranstaltung_ligaleiter_id,
  veranstaltung_meldedeadline,
  veranstaltung_liga_id)
SELECT
       '1',
       liga_name,
       '2019',
       '1',
       '2018-10-31',
       liga_id
from prod_data_migration."Ligen" as alt, liga as neu
where  alt."Liga" = neu.liga_name
;




