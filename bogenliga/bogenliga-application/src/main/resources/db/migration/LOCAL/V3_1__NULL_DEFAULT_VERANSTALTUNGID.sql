ALTER TABLE mannschaft DROP CONSTRAINT fk_mannschaft_veranstaltung;
ALTER TABLE mannschaft ALTER COLUMN mannschaft_veranstaltung_id SET DEFAULT NULL;
ALTER TABLE mannschaft ADD CONSTRAINT fk_mannschaft_veranstaltung FOREIGN KEY (mannschaft_veranstaltung_id) references veranstaltung (veranstaltung_id);