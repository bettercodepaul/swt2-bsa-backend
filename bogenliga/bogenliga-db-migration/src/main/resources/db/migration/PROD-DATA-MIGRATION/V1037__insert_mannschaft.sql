-- bei der Migration der Vereine haben wir die Mannschaftsnummer bestimmt
-- Prämisse: es existieren nur Vereine, die auch Mannschaften stellen
-- somit erzeugen wir für jeden einzelnen Eintrag in der "vereine" Tabelle eine Mannschaft.

-- die ID geben wir nicht vor, die wird beim Insert automatisch generiert

-- alle mal lachen: die Zuordnung zwischen Liga und Mannschaft ist in der Tabelle vereinsanschriften
-- übringens stehen da keine Addressen drin ;-))

SET search_path = 'prod'
;

DELETE from mannschaft;

insert INTO mannschaft
  (mannschaft_nummer,
  mannschaft_verein_id,
  mannschaft_veranstaltung_id,
   mannschaft_benutzer_id
  )
SELECT
  to_number("vereine"."Mannschaft", '9'),
  verein.verein_id,
  veranstaltung.veranstaltung_id,
  '1'
from
  prod_data_migration."vereine",
  prod_data_migration."Ligen",
  prod_data_migration."vereinsanschriften",
  verein,
  veranstaltung
where "vereine"."VNR" = "vereinsanschriften"."Vnr"
and   "vereine"."Verein" = verein.verein_name
and "vereinsanschriften"."Wettkampfklasse" = "Ligen"."Wregion"
and "Ligen"."Liga" = veranstaltung.veranstaltung_name
;




