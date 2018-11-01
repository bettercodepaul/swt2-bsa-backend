
INSERT INTO public.verein (verein_name, verein_dsb_identifier, verein_region_id)
SELECT SUBSTRING ("vereine"."Verein",'([A-Z,a-z, ,-]{1,200})'),
       "vereine"."VNR",
       1
FROM prod_data_migration."vereine"
WHERE "VNR" not in ('36WT919999', '36WT929999', '36WT939999', '36WT949999')
AND "aktiviert" = '1'
AND SUBSTRING ("vereine"."Verein",'([0-9]{1,1})')=''
;


