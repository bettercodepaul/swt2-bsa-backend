/* Change the name of the Auffuellmannschaft to Platzhalter (Placeholder) */
UPDATE verein
SET verein_name = 'Platzhalter', verein_dsb_identifier = 'Platzhalter'
WHERE verein_name = 'Auffüllmannschaft';

INSERT INTO dsb_mitglied (
    dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum,
    dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id,
    dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_by, version
)
VALUES
    (1, 'Platzhaltermitglied_Vorname1', 'Platzhaltermitlgied_Nachname1', '2021-06-30', 'DE', 69696969, 99, null, '2023-06-30 14:16:57.048792', 0, null, 0),
    (2, 'Platzhaltermitglied_Vorname2', 'Platzhaltermitglied_Nachname2', '2021-06-30', 'DE', 69696970, 99, null, '2023-06-30 14:16:57.048792', 0, null, 0),
    (3, 'Platzhaltermitglied_Vorname3', 'PlatzhaltermitgliedS_Nachname3', '2021-06-30', 'DE', 69696971, 99, null, '2023-06-30 14:16:57.048792', 0, null, 0)
ON CONFLICT DO NOTHING;




