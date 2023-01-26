-- Add new discipline element with initial value 0 (recurve)
ALTER TABLE liga
ADD COLUMN liga_disziplin_id numeric(19) NOT NULL
DEFAULT 0;


ALTER TABLE liga
ADD FOREIGN KEY (liga_disziplin_id)
REFERENCES disziplin(disziplin_id);