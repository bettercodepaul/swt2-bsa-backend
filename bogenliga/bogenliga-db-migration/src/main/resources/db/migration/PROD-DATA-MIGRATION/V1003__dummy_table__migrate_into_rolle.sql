INSERT INTO public.rolle (rolle_id, rolle_name)
SELECT id,
       title
FROM prod_data_migration.dummy
;