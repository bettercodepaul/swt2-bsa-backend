--leider mischt die alter Datenstruktur die Informationen Vereinsname und Mannschaftsnummer
--dass müssen wir für die Migration trennen
--dazu fügen wir eine Spalte Mannschaft hinzu und füllen für alle den Default 0
--danach schneiden wir aus dem Vereinsnamen evtl. vorhandenen Nummern aus
--und über schreiben die 0
SET search_path = 'prod'
;
-- spalte hinzufügen



ALTER TABLE prod_data_migration."vereine"
  ADD COLUMN "Mannschaft" text DEFAULT '1'
;
Commit;

-- übernehmen der Mannschaftsnummer in die neue Spalte, wenn es eine gibt
Update prod_data_migration."vereine"
SET "Mannschaft" = SUBSTRING ("Verein",'([1-9]{1,1})')
WHERE SUBSTRING ("vereine"."Verein",'([0-9]{1,1})')<>''
;


-- reduzieren der Vereinsnamen - ohne Nummer und ohne training Spaces
Update prod_data_migration."vereine"
SET "Verein" = RTRIM(SUBSTRING ("Verein",'([A-Z,a-z,ä,ö,ü,Ä,Ü,Ö,ß, ,-]{1,200})'))
;

-- jetzt schmeißen wir noch die "unbekannten Vereine" raus
Delete from prod_data_migration."vereine"
where "VNR" in ('36WT919999', '36WT929999', '36WT939999', '36WT949999')
;

DELETE from  verein;

-- weil wir die Mannschaftsnummer entfernt haben, sind ide Namen der Vereine
-- jetzt nicht mehr eindeutig...
-- daher select distinct - hier reicht uns ein Eintrag
INSERT INTO verein (verein_name, verein_dsb_identifier, verein_region_id)
SELECT DISTINCT ON
       ("Verein") "Verein",
       "VNR",
       1
FROM prod_data_migration."vereine"
WHERE "aktiviert" = '1'
ORDER BY "Verein",
    "VNR"
;


