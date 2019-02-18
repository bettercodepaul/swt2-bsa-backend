
-- bei der Migration der Vereine haben wir die Mannschaftsnummer bestimmt
-- Prämisse: es existieren nur Vereine, die auch Mannschaften stellen
-- somit erzeugen wir für jeden einzelnen Eintrag in der "vereine" Tabelle eine Mannschaft.

-- die ID geben wir nicht vor, die wird beim Insert automatisch generiert

-- alle mal lachen: die Zuordnung zwischen Liga und Mannschaft ist in der Tabelle vereinsanschriften
-- übringens stehen da keine Adressen drin ;-))

-- fachlich kritisch: eine Mannschaft gehört zu einem Verein -
-- aber dsb-mitglieder können prinzipiell in mehreren Vereinen sein
-- und je Disziplin in einem anderen Verein in einer Mannschaft sein...





insert INTO public.mannschaftsmitglied
    (mannschaftsmitglied_mannschaft_id,
    mannschaftsmitglied_dsb_mitglied_id,
    mannschaftsmitglied_dsb_mitglied_eingesetzt
    )
SELECT
  mannschaft.mannschaft_id,
  dsb_mitglied.dsb_mitglied_id,
  CASE
    WHEN "Schützentabelle"."w1m1"+"Schützentabelle"."w1m2"+"Schützentabelle"."w1m3"+
  "Schützentabelle"."w1m4"+"Schützentabelle"."w1m5"+"Schützentabelle"."w1m6"
  +"Schützentabelle"."w1m7" >0 AND
  "Schützentabelle"."w2m1"+"Schützentabelle"."w2m2"+"Schützentabelle"."w2m3"+
  "Schützentabelle"."w2m4"+"Schützentabelle"."w2m5"+"Schützentabelle"."w2m6"
  +"Schützentabelle"."w2m7" > 0
    THEN 2
    ELSE
      CASE
        WHEN "Schützentabelle"."w1m1"+"Schützentabelle"."w1m2"+"Schützentabelle"."w1m3"+
             "Schützentabelle"."w1m4"+"Schützentabelle"."w1m5"+"Schützentabelle"."w1m6"
               +"Schützentabelle"."w1m7"+
             "Schützentabelle"."w2m1"+"Schützentabelle"."w2m2"+"Schützentabelle"."w2m3"+
             "Schützentabelle"."w2m4"+"Schützentabelle"."w2m5"+"Schützentabelle"."w2m6"
               +"Schützentabelle"."w2m7" > 0
          THEN 1
        ELSE
          0
      END
  END
from
  prod_data_migration."Schützentabelle",
  prod_data_migration."Ligen",
  prod_data_migration."vereine",
  prod_data_migration."vereinsanschriften",
  public.verein,
  public.veranstaltung,
  public.mannschaft,
  public.dsb_mitglied
where "Schützentabelle"."SchVNR" = "vereine"."VNR"
and "vereine"."VNR" = "vereinsanschriften"."Vnr"
and   "vereine"."Verein" = verein.verein_name
and "vereinsanschriften"."Wettkampfklasse" = "Ligen"."Wregion"
and "Ligen"."Liga" = veranstaltung.veranstaltung_name
and to_number("vereine"."Mannschaft", '9') = mannschaft.mannschaft_nummer
and verein.verein_id = mannschaft.mannschaft_verein_id
and "Schützentabelle"."Mitgliedsnummer" = dsb_mitglied.dsb_mitglied_mitgliedsnummer
;

