INSERT INTO public.verein (verein_name, verein_dsb_identifier)
SELECT Verein,
       vnr,
       1
FROM prod_data_migration.Vereine
WHERE vnr not in ('36WT919999', '36WT929999', '36WT939999', '36WT949999')
;