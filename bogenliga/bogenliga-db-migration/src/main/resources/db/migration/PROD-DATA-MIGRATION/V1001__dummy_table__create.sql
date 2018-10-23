-- define new default database schema for all sql queries in this file
-- alternative: prod_data_migration.<table>
SET search_path = 'prod_data_migration'
;

CREATE TABLE dummy (
  title VARCHAR(100),
  id    DECIMAL(19, 0)
)
;