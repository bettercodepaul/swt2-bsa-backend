ALTER TABLE liga
    DROP COLUMN IF EXISTS liga_detail;

ALTER TABLE liga
    ADD COLUMN liga_detail varchar(5000) NOT NULL
    DEFAULT '';
