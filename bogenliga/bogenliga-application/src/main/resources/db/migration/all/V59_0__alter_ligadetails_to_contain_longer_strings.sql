
ALTER TABLE liga
DROP COLUMN IF EXISTS liga_detail;

alter table liga
ADD COLUMN liga_detail varchar(5000)
default '';