-- limit the number of copies in table mannschaften
-- by adding a constraint of a unique key (verein_id & nummer & veranstaltung_id
-- this ia a week limit, but it will limit the numer of copies without assigment to a league to 1

ALTER TABLE mannschaft
    ADD CONSTRAINT IF NOT EXISTS cu_mannschaft_veranstaltung UNIQUE (mannschaft_verein_id, mannschaft_nummer, mannschaft_veranstaltung_id);
