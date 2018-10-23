-- define new default database schema for all sql queries in this file
-- alternative: prod_data_migration.<table>
SET search_path = 'prod_data_migration'
;

INSERT INTO dummy (id, title)
VALUES (1, 'Title')
;