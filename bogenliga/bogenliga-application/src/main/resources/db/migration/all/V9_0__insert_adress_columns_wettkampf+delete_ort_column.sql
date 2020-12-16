/**
 * This script is necessary, to store the adress of a "Wettkampftag",
 * because we splitted the adress in 4 parts and deleted the single column "wettkampf_ort"
 **/

ALTER TABLE wettkampf
    DROP COLUMN wettkampf_ort,                     -- delete column wettkampf_ort
    ADD COLUMN wettkampf_strasse VARCHAR (30),     -- The street for the "Wettkampf Tag" will be stored in this column
    ADD COLUMN wettkampf_plz VARCHAR (10),         -- The plz for the "Wettkampf Tag" will be stored in this column
    ADD COLUMN wettkampf_ortsname VARCHAR (25),    -- The city name for the "Wettkampf Tag" will be stored in this column
    ADD COLUMN wettkampf_ortsinfo VARCHAR (200);   -- Some additional adress information for the "Wettkampf Tag" will be stored in this column
;

