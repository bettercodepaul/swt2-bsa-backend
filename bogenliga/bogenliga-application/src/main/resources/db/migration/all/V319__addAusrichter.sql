alter table Wettkampf
add WettkampfAusrichter DECIMAL (19,0);
alter table Wettkampf
add CONSTRAINT fk_Wettkampf_Ausrichter FOREIGN KEY (WettkampfAusrichter) REFERENCES benutzer(benutzer_id);