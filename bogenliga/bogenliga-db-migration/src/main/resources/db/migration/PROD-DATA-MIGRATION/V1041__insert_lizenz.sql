


-- Migration Tabelle "lizenzneutabelle" -- macht keinen Sinn, da steht nichts drin
-- wir gegenerieren hier eine Lizenz für alle Mannschaftsmitglieder

SET search_path = 'prod'
;

DELETE from lizenz;

insert INTO lizenz
    (lizenz_nummer,
    lizenz_region_id,
    lizenz_dsb_mitglied_id,
    lizenz_typ,
    lizenz_disziplin_id
    )
SELECT
  to_number((verein_region_id || '0' || mannschaftsmitglied.mannschaftsmitglied_dsb_mitglied_id), '999D99S') ,
  verein.verein_region_id,
   mannschaftsmitglied.mannschaftsmitglied_dsb_mitglied_id,
    'Liga',
    '0'
from
  verein,
  mannschaft,
  mannschaftsmitglied
where verein.verein_id = mannschaft.mannschaft_verein_id
and mannschaftsmitglied.mannschaftsmitglied_mannschaft_id = mannschaft.mannschaft_id
;

