-- bei der Migration der Vereine haben wir die Mannschaftsnummer bestimmt
-- Prämisse: es existieren nur Vereine, die auch Mannschaften stellen
-- somit erzeugen wir für jeden einzelnen Eintrag in der "vereine" Tabelle eine Mannschaft.

-- die ID geben wir nicht vor, die wird beim Insert automatisch generiert

-- alle mal lachen: die Zuordnung zwischen Liga und Mannschaft ist in der Tabelle vereinsanschriften
-- übringens stehen da keine Adressen drin ;-))

-- Bezug zur Region fest auf Württemberg
-- Hierarchie der Ligen muss später manuell gepflegt werden
SET search_path = 'prod'
;

insert INTO liga
 ( liga_region_id,
    liga_name
   )
SELECT
  '1',
  "Liga"
from prod_data_migration."Ligen"
;




