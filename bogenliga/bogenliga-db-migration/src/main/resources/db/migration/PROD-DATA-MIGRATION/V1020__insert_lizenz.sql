


-- Migration Tabelle "lizenzneutabelle" -- macht keinen Sinn, da steht nichts drin
-- wir gegenerieren hier eine Lizenz für alle Mannschaftsmitglieder



insert INTO public.lizenz
    (lizenz_nummer,
    lizenz_region_id,
    lizenz_dsb_mitglied_id,
    'Liga',
    lizenz_disziplin_id
    )
SELECT
   mannschaftsmitglied.mannschaftsmitglied_dsb_mitglied_id,
  "Bogenvorbereitung"."lfd"
from
  public.verein,
  public.mannschaft,
  public.mannschaftsmitglied
where verein.verein_id = mannschaft.mannschaft_verein_id
and mannschaftsmitglied.mannschaftsmitglied_mannschaft_id = mannschaft.mannschaft_id
;

