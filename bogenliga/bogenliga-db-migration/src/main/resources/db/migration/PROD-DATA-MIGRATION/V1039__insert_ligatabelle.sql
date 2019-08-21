
-- bei der Migration der Vereine haben wir die Mannschaftsnummer bestimmt
-- Prämisse: es existieren nur Vereine, die auch Mannschaften stellen
-- somit erzeugen wir für jeden einzelnen Eintrag in der "vereine" Tabelle eine Mannschaft.

-- die ID geben wir nicht vor, die wird beim Insert automatisch generiert

-- alle mal lachen: die Zuordnung zwischen Liga und Mannschaft ist in der Tabelle vereinsanschriften
-- übringens stehen da keine Adressen drin ;-))
SET search_path = 'prod'
;



insert INTO ligatabelle
    (ligatabelle_veranstaltung_id,
    ligatabelle_wettkampf_tag,
    ligatabelle_mannschaft_id,
    ligatabelle_tabellenplatz,
    ligatabelle_matchpkt,
    ligatabelle_matchpkt_gegen,
    ligatabelle_satzpkt,
    ligatabelle_satzpkt_gegen
    )
SELECT
  veranstaltung.veranstaltung_id,
  to_number("Bogenvorbereitung"."Wettkampf", '9'),
  mannschaft.mannschaft_id,
  "Bogenvorbereitung"."lfd",
  SUM(to_number("Tabelle"."Pluspunkte", '9')),
  SUM(to_number("Tabelle"."Minuspunkte", '9')),
  SUM("Tabelle"."satzplus"),
  SUM("Tabelle"."satzminus")
from
  prod_data_migration."Bogenvorbereitung",
  prod_data_migration."Ligen",
  prod_data_migration."vereine",
  prod_data_migration."Tabelle",
  verein,
  veranstaltung,
  mannschaft
where "Bogenvorbereitung"."Vereinsnummer" = "vereine"."VNR"
and "vereine"."VNR" = "Tabelle"."VNR"
and   "vereine"."Verein" = verein.verein_name
and "Bogenvorbereitung"."Wettkampfklasse" = "Ligen"."Wregion"
and "Bogenvorbereitung"."Wettkampfklasse" = "Tabelle"."Region"
and "Bogenvorbereitung"."Wettkampf" = "Tabelle"."Wettkampf"
and "Ligen"."Liga" = veranstaltung.veranstaltung_name
and to_number("vereine"."Mannschaft", '9') = mannschaft.mannschaft_nummer
and mannschaft.mannschaft_veranstaltung_id = veranstaltung.veranstaltung_id
and verein.verein_id = mannschaft.mannschaft_verein_id
GROUP by
  veranstaltung.veranstaltung_id,
  to_number("Bogenvorbereitung"."Wettkampf", '9'),
  mannschaft.mannschaft_id,
  "Bogenvorbereitung"."lfd"
;


