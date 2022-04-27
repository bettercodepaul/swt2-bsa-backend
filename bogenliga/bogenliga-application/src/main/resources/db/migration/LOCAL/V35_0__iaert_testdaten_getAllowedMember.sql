DO $do$

BEGIN
    return;
    --insert test data into table verein
    INSERT INTO verein (verein_id, verein_name, verein_dsb_identifier, verein_region_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2000, 'SWT2_Test_Verein9', 'SWT2_Vereinsnummer9', 3, '2021-01-28 20:54:10.225483', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO verein (verein_id, verein_name, verein_dsb_identifier, verein_region_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2001, 'SWT2_Test_Verein10', 'SWT2_Vereinsnummer10', 3, '2021-01-28 20:54:32.134826', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO verein (verein_id, verein_name, verein_dsb_identifier, verein_region_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2002, 'SWT2_Test_Verein11', 'SWT2_Vereinsnummer11', 3, '2021-01-28 20:54:50.493881', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO verein (verein_id, verein_name, verein_dsb_identifier, verein_region_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2003, 'SWT2_Test_Verein12', 'SWT2_Vereinsnummer12', 3, '2021-01-28 20:54:50.493881', 0, null, null, 0) ON CONFLICT DO NOTHING;


    --insert test data into table dsb_mitglied
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2000, 'SWT2_Test_Vorname25', 'SWT2_Test_Nachname25', '2000-12-25', 'DE', 'SWT2_Mitgliedsnummer25', 2000, null, '2021-01-28 21:05:40.587592', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2001, 'SWT2_Test_Vorname26', 'SWT2_Test_Nachname26', '2000-04-15', 'DE', 'SWT2_Mitgliedsnummer26', 2000, null, '2021-01-28 21:06:16.633635', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2002, 'SWT2_Test_Vorname27', 'SWT2_Test_Nachname27', '2000-04-15', 'DE', 'SWT2_Mitgliedsnummer27', 2000, null, '2021-01-28 21:06:57.191383', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2003, 'SWT2_Test_Vorname28', 'SWT2_Test_Nachname28', '2001-04-15', 'DE', 'SWT2_Mitgliedsnummer28', 2001, null, '2021-01-28 21:07:36.878604', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2004, 'SWT2_Test_Vorname29', 'SWT2_Test_Nachname29', '2001-12-15', 'DE', 'SWT2_Mitgliedsnummer29', 2001, null, '2021-01-28 21:08:06.834457', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2005, 'SWT2_Test_Vorname30', 'SWT2_Test_Nachname30', '2001-04-15', 'DE', 'SWT2_Mitgliedsnummer30', 2001, null, '2021-01-28 21:08:49.604368', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2006, 'SWT2_Test_Vorname31', 'SWT2_Test_Nachname31', '2002-12-25', 'DE', 'SWT2_Mitgliedsnummer31', 2002, null, '2021-01-28 21:09:20.959538', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2007, 'SWT2_Test_Vorname32', 'SWT2_Test_Nachname32', '2002-04-25', 'DE', 'SWT2_Mitgliedsnummer32', 2002, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2008, 'SWT2_Test_Vorname33', 'SWT2_Test_Nachname33', '2002-04-25', 'DE', 'SWT2_Mitgliedsnummer33', 2002, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2009, 'SWT2_Test_Vorname34', 'SWT2_Test_Nachname34', '2003-04-25', 'DE', 'SWT2_Mitgliedsnummer34', 2003, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2010, 'SWT2_Test_Vorname35', 'SWT2_Test_Nachname35', '2003-04-25', 'DE', 'SWT2_Mitgliedsnummer35', 2003, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2011, 'SWT2_Test_Vorname36', 'SWT2_Test_Nachname36', '2003-04-25', 'DE', 'SWT2_Mitgliedsnummer36', 2003, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2012, 'SWT2_Test_Vorname37', 'SWT2_Test_Nachname37', '2002-04-25', 'DE', 'SWT2_Mitgliedsnummer37', 2002, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2013, 'SWT2_Test_Vorname38', 'SWT2_Test_Nachname38', '2002-04-25', 'DE', 'SWT2_Mitgliedsnummer38', 2002, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2014, 'SWT2_Test_Vorname39', 'SWT2_Test_Nachname39', '2003-04-25', 'DE', 'SWT2_Mitgliedsnummer39', 2003, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2015, 'SWT2_Test_Vorname40', 'SWT2_Test_Nachname40', '2003-04-25', 'DE', 'SWT2_Mitgliedsnummer40', 2003, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2016, 'SWT2_Test_Vorname41', 'SWT2_Test_Nachname41', '2003-04-25', 'DE', 'SWT2_Mitgliedsnummer41', 2003, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;
    INSERT INTO dsb_mitglied (dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum, dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id, dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
    VALUES (2017, 'SWT2_Test_Vorname42', 'SWT2_Test_Nachname42', '2003-04-25', 'DE', 'SWT2_Mitgliedsnummer41', 2003, null, '2021-01-28 21:09:56.870332', 0, null, null, 0) ON CONFLICT DO NOTHING;


    --insert test data into table veranstaltung
    INSERT INTO veranstaltung (veranstaltung_id, veranstaltung_wettkampftyp_id, veranstaltung_name, veranstaltung_sportjahr, veranstaltung_meldedeadline, veranstaltung_ligaleiter_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, veranstaltung_liga_id)
    VALUES (2000, 1, 'SWT2_Test_Veranstaltung_Liga_13', 2022, '2017-11-01', 2, '2021-01-28 20:51:23.509994', 0, null, null, 0, 13) ON CONFLICT DO NOTHING;
    INSERT INTO veranstaltung (veranstaltung_id, veranstaltung_wettkampftyp_id, veranstaltung_name, veranstaltung_sportjahr, veranstaltung_meldedeadline, veranstaltung_ligaleiter_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, veranstaltung_liga_id)
    VALUES (2001, 1, 'SWT2_Test_Veranstaltung_Liga_11', 2022, '2017-11-01', 2, '2021-01-28 20:51:23.509994', 0, null, null, 0, 11) ON CONFLICT DO NOTHING;

    --insert test data into table mannschaft
    --veranstaltung 2000 liga 13
    INSERT INTO mannschaft (mannschaft_id, mannschaft_verein_id, mannschaft_nummer, mannschaft_benutzer_id, mannschaft_veranstaltung_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaft_sortierung)
    VALUES (2010, 2000, 1, 1, 2000, '2021-01-28 21:00:11.394075', 0, '2021-01-28 21:48:09.556698', 0, 1, 1) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaft (mannschaft_id, mannschaft_verein_id, mannschaft_nummer, mannschaft_benutzer_id, mannschaft_veranstaltung_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaft_sortierung)
    VALUES (2011, 2001, 1, 1, 2000, '2021-01-28 21:00:43.142338', 0, '2021-01-28 21:48:17.524456', 0, 1, 2) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaft (mannschaft_id, mannschaft_verein_id, mannschaft_nummer, mannschaft_benutzer_id, mannschaft_veranstaltung_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaft_sortierung)
    VALUES (2012, 2002, 1, 1, 2000, '2021-01-28 21:01:08.275465', 0, '2021-01-28 21:48:23.795492', 0, 1, 3) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaft (mannschaft_id, mannschaft_verein_id, mannschaft_nummer, mannschaft_benutzer_id, mannschaft_veranstaltung_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaft_sortierung)
    VALUES (2013, 2003, 1, 1, 2000, '2021-01-28 21:01:08.275465', 0, '2021-01-28 21:48:23.795492', 0, 1, 4) ON CONFLICT DO NOTHING;

    --veranstaltung 2001 liga 11
    INSERT INTO mannschaft (mannschaft_id, mannschaft_verein_id, mannschaft_nummer, mannschaft_benutzer_id, mannschaft_veranstaltung_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaft_sortierung)
    VALUES (2014, 2000, 1, 1, 2001, '2021-01-28 21:01:08.275465', 0, '2021-01-28 21:48:23.795492', 0, 1, 1) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaft (mannschaft_id, mannschaft_verein_id, mannschaft_nummer, mannschaft_benutzer_id, mannschaft_veranstaltung_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaft_sortierung)
    VALUES (2015, 2001, 1, 1, 2001, '2021-01-28 21:01:08.275465', 0, '2021-01-28 21:48:23.795492', 0, 1, 1) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaft (mannschaft_id, mannschaft_verein_id, mannschaft_nummer, mannschaft_benutzer_id, mannschaft_veranstaltung_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaft_sortierung)
    VALUES (2016, 2002, 1, 1, 2001, '2021-01-28 21:01:08.275465', 0, '2021-01-28 21:48:23.795492', 0, 1, 1) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaft (mannschaft_id, mannschaft_verein_id, mannschaft_nummer, mannschaft_benutzer_id, mannschaft_veranstaltung_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaft_sortierung)
    VALUES (2017, 2003, 1, 1, 2001, '2021-01-28 21:01:08.275465', 0, '2021-01-28 21:48:23.795492', 0, 1, 1) ON CONFLICT DO NOTHING;

    --insert test data into table mannschaftsmitglied
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2000, 2010, 2000, 0, '2021-01-28 21:44:00.847745', 0, null, null, 0, 1) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2001, 2010, 2001, 0, '2021-01-28 21:44:06.218008', 0, null, null, 0, 2) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2002, 2010, 2002, 0, '2021-01-28 21:44:10.421685', 0, null, null, 0, 3) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2003, 2011, 2003, 0, '2021-01-28 21:44:29.136280', 0, null, null, 0, 1) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2004, 2011, 2004, 0, '2021-01-28 21:44:32.891021', 0, null, null, 0, 2) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2005, 2011, 2005, 0, '2021-01-28 21:44:32.891021', 0, null, null, 0, 3) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2006, 2012, 2006, 0, '2021-01-28 21:44:32.891021', 0, null, null, 0, 1) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2007, 2012, 2007, 0, '2021-01-28 21:44:32.891021', 0, null, null, 0, 2) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2008, 2012, 2008, 0, '2021-01-28 21:44:32.891021', 0, null, null, 0, 3) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2009, 2013, 2009, 0, '2021-01-28 21:44:32.891021', 0, null, null, 0, 1) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2010, 2013, 2010, 0, '2021-01-28 21:44:32.891021', 0, null, null, 0, 2) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2011, 2013, 2011, 0, '2021-01-28 21:44:32.891021', 0, null, null, 0, 3) ON CONFLICT DO NOTHING;
    --Testperson 2000 wird in neue Mannschaft eingesetzt
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2012, 2014, 2000, 0, '2021-01-28 21:44:00.847745', 0, null, null, 0, 1) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2013, 2014, 2012, 0, '2021-01-28 21:44:00.847745', 0, null, null, 0, 2) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2014, 2014, 2013, 0, '2021-01-28 21:44:00.847745', 0, null, null, 0, 3) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2015, 2015, 2014, 0, '2021-01-28 21:44:00.847745', 0, null, null, 0, 1) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2016, 2015, 2015, 0, '2021-01-28 21:44:00.847745', 0, null, null, 0, 2) ON CONFLICT DO NOTHING;
    INSERT INTO mannschaftsmitglied (mannschaftsmitglied_id, mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, mannschaftsmitglied_rueckennummer)
    VALUES (2017, 2015, 2016, 0, '2021-01-28 21:44:00.847745', 0, null, null, 0, 3) ON CONFLICT DO NOTHING;

    --insert test data into table wettkampf

    --wettkampf in liga 13
    INSERT INTO wettkampf (wettkampf_id, wettkampf_veranstaltung_id, wettkampf_datum, wettkampf_beginn, wettkampf_tag, wettkampf_disziplin_id, wettkampf_wettkampftyp_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, wettkampfausrichter, wettkampf_strasse, wettkampf_plz, wettkampf_ortsname)
    VALUES (2050, 2000, '2022-05-01', '11:30', 1, 0, 0, '2021-01-28 21:27:42.305203', 0, null, null, 7, null, 'Bahnhofstrasse', 72764, 'Reutlingen') ON CONFLICT DO NOTHING;
    INSERT INTO wettkampf (wettkampf_id, wettkampf_veranstaltung_id, wettkampf_datum, wettkampf_beginn, wettkampf_tag, wettkampf_disziplin_id, wettkampf_wettkampftyp_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, wettkampfausrichter, wettkampf_strasse, wettkampf_plz, wettkampf_ortsname)
    VALUES (2051, 2000, '2022-05-02', '12:30', 2, 0, 0, '2021-01-28 21:27:42.305203', 0, null, null, 7, null, 'Bahnhofstrasse', 72764, 'Reutlingen') ON CONFLICT DO NOTHING;
    INSERT INTO wettkampf (wettkampf_id, wettkampf_veranstaltung_id, wettkampf_datum, wettkampf_beginn, wettkampf_tag, wettkampf_disziplin_id, wettkampf_wettkampftyp_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, wettkampfausrichter, wettkampf_strasse, wettkampf_plz, wettkampf_ortsname)
    VALUES (2052, 2000, '2022-05-03', '13:30', 3, 0, 0, '2021-01-28 21:27:42.305203', 0, null, null, 6, null, 'Bahnhofstrasse', 72764, 'Reutlingen') ON CONFLICT DO NOTHING;
    INSERT INTO wettkampf (wettkampf_id, wettkampf_veranstaltung_id, wettkampf_datum, wettkampf_beginn, wettkampf_tag, wettkampf_disziplin_id, wettkampf_wettkampftyp_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, wettkampfausrichter, wettkampf_strasse, wettkampf_plz, wettkampf_ortsname)
    VALUES (2053, 2000, '2022-05-04', '14:30', 4, 0, 0, '2021-01-28 21:27:42.305203', 0, null, null, 9, null, 'Bahnhofstrasse', 72764, 'Reutlingen') ON CONFLICT DO NOTHING;
    --wettkampf in liga 11
    INSERT INTO wettkampf (wettkampf_id, wettkampf_veranstaltung_id, wettkampf_datum, wettkampf_beginn, wettkampf_tag, wettkampf_disziplin_id, wettkampf_wettkampftyp_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, wettkampfausrichter, wettkampf_strasse, wettkampf_plz, wettkampf_ortsname)
    VALUES (2054, 2001, '2022-05-01', '14:30', 1, 0, 0, '2021-01-28 21:27:42.305203', 0, null, null, 9, null, 'Bahnhofstrasse', 72764, 'Reutlingen') ON CONFLICT DO NOTHING;
    INSERT INTO wettkampf (wettkampf_id, wettkampf_veranstaltung_id, wettkampf_datum, wettkampf_beginn, wettkampf_tag, wettkampf_disziplin_id, wettkampf_wettkampftyp_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, wettkampfausrichter, wettkampf_strasse, wettkampf_plz, wettkampf_ortsname)
    VALUES (2055, 2001, '2022-05-02', '14:30', 2, 0, 0, '2021-01-28 21:27:42.305203', 0, null, null, 9, null, 'Bahnhofstrasse', 72764, 'Reutlingen') ON CONFLICT DO NOTHING;
    INSERT INTO wettkampf (wettkampf_id, wettkampf_veranstaltung_id, wettkampf_datum, wettkampf_beginn, wettkampf_tag, wettkampf_disziplin_id, wettkampf_wettkampftyp_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, wettkampfausrichter, wettkampf_strasse, wettkampf_plz, wettkampf_ortsname)
    VALUES (2056, 2001, '2022-05-03', '14:30', 3, 0, 0, '2021-01-28 21:27:42.305203', 0, null, null, 9, null, 'Bahnhofstrasse', 72764, 'Reutlingen') ON CONFLICT DO NOTHING;
    INSERT INTO wettkampf (wettkampf_id, wettkampf_veranstaltung_id, wettkampf_datum, wettkampf_beginn, wettkampf_tag, wettkampf_disziplin_id, wettkampf_wettkampftyp_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, wettkampfausrichter, wettkampf_strasse, wettkampf_plz, wettkampf_ortsname)
    VALUES (2057, 2001, '2022-05-04', '14:30', 4, 0, 0, '2021-01-28 21:27:42.305203', 0, null, null, 9, null, 'Bahnhofstrasse', 72764, 'Reutlingen') ON CONFLICT DO NOTHING;

    --insert test data into table match

    --first comp day liga 13

    --first match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 1, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2550) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 1, 1, 2011, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2551) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 1, 2, 2012, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2552) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 1, 2, 2013, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2553) ON CONFLICT DO NOTHING;
    --second match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 2, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2554) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 2, 1, 2012, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2555) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 2, 2, 2011, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2556) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 2, 2, 2013, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2557) ON CONFLICT DO NOTHING;
    --third match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 3, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2558) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 3, 1, 2013, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2559) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 3, 2, 2011, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2560) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2050, 3, 2, 2012, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2561) ON CONFLICT DO NOTHING;

    --second comp day liga 13

    --first match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 1, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2562) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 1, 1, 2011, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2563) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 1, 2, 2012, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2564) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 1, 2, 2013, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2565) ON CONFLICT DO NOTHING;
    --second match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 2, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2566) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 2, 1, 2012, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2567) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 2, 2, 2011, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2568) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 2, 2, 2013, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2569) ON CONFLICT DO NOTHING;
    --third match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 3, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2570) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 3, 1, 2013, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2571) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 3, 2, 2011, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2572) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2051, 3, 2, 2012, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2573) ON CONFLICT DO NOTHING;

    --third comp day liga 13

    --first match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 1, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2574) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 1, 1, 2011, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2575) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 1, 2, 2012, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2576) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 1, 2, 2013, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2577) ON CONFLICT DO NOTHING;
    --second match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 2, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2578) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 2, 1, 2012, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2579) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 2, 2, 2011, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2580) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 2, 2, 2013, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2581) ON CONFLICT DO NOTHING;
    --third match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 3, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2582) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 3, 1, 2013, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2583) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 3, 2, 2011, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2584) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2052, 3, 2, 2012, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2585) ON CONFLICT DO NOTHING;

    --fourth comp day liga 13

    --first match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 1, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2586) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 1, 1, 2011, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2587) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 1, 2, 2012, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2588) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 1, 2, 2013, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2589) ON CONFLICT DO NOTHING;
    --second match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 2, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2590) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 2, 1, 2012, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2591) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 2, 2, 2011, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2592) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 2, 2, 2013, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2593) ON CONFLICT DO NOTHING;
    --third match
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 3, 1, 2010, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2594) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 3, 1, 2013, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2595) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 3, 2, 2011, 3, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2596) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2053, 3, 2, 2012, 4, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2597) ON CONFLICT DO NOTHING;

    -- first comp day liga 11
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2054, 1, 1, 2014, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2598) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2054, 1, 1, 2015, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2599) ON CONFLICT DO NOTHING;

    -- second comp day liga 11
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2055, 1, 1, 2014, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2602) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2055, 1, 1, 2015, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2603) ON CONFLICT DO NOTHING;

    -- third comp day liga 11
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2056, 1, 1, 2014, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2600) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2056, 1, 1, 2015, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2601) ON CONFLICT DO NOTHING;

    -- first comp day liga 11
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2057, 1, 1, 2014, 1, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2604) ON CONFLICT DO NOTHING;
    INSERT INTO match (match_wettkampf_id, match_nr, match_begegnung, match_mannschaft_id, match_scheibennummer, match_matchpunkte, match_satzpunkte, match_strafpunkte_satz_1, match_strafpunkte_satz_2, match_strafpunkte_satz_3, match_strafpunkte_satz_4, match_strafpunkte_satz_5, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version, match_id)
    VALUES (2057, 1, 1, 2015, 2, null, null, null, null, null, null, null, '2020-03-01 17:14:35.105428', 0, null, null, 3, 2605) ON CONFLICT DO NOTHING;


END
$do$