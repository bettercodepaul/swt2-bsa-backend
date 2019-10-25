

-- Migration Tabelle "lizenzneutabelle" -- macht keinen Sinn, da steht nichts drin
-- wir gegenerieren hier eine Lizenz für alle Mannschaftsmitglieder
-- 999999999999S = 5 Stellen für region_id, 6 Stellen für dsb_mitglied_id, 1 Stelle für die 0


DELETE from lizenz;

insert INTO lizenz
    (lizenz_nummer,
    lizenz_region_id,
    lizenz_dsb_mitglied_id,
    lizenz_typ,
    lizenz_disziplin_id
    )
SELECT
       to_number(TRIM(LEADING '0' FROM (CAST(verein_region_id AS TEXT))) || '0' ||
                 TRIM(LEADING '0' FROM (CAST(mannschaftsmitglied.mannschaftsmitglied_dsb_mitglied_id AS TEXT))),
                 '999999999999S'),
       verein.verein_region_id,
       mannschaftsmitglied.mannschaftsmitglied_dsb_mitglied_id,
       'Liga',
       '0'
from verein,
     mannschaft,
     mannschaftsmitglied
where verein.verein_id = mannschaft.mannschaft_verein_id
  and mannschaftsmitglied.mannschaftsmitglied_mannschaft_id = mannschaft.mannschaft_id
;
