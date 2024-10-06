
ALTER TABLE mannschaft
ADD COLUMN mannschaft_sportjahr NUMERIC(4);

UPDATE mannschaft
SET mannschaft_sportjahr = sub.veranstaltung_sportjahr
FROM (
    SELECT veranstaltung_id, veranstaltung_sportjahr
    FROM veranstaltung
    )sub
WHERE mannschaft.mannschaft_veranstaltung_id = sub.veranstaltung_id;
