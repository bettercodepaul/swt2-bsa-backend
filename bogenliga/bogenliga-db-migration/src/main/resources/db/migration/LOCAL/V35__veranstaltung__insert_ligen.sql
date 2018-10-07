INSERT INTO veranstaltung (-- Recurve Ligen
                           veranstaltung_id,
                           veranstaltung_wettkampftyp_id,
                           veranstaltung_name,
                           veranstaltung_sportjahr,
                           veranstaltung_ligaleiter_id,
                           veranstaltung_meldedeadline,
                           veranstaltung_kampfrichter_anzahl,
                           veranstaltung_hoehere)
VALUES
(0, 1, 'Würtembergliga', 2018, 2, '2017-10-31', 5, NULL),
(1, 1, 'Landesliga Nord', 2018, 2, '2017-10-31', 5, 0),
(2, 1, 'Landesliga Süd', 2018, 2, '2017-10-31', 5, 0),
(3, 1, 'Relegation Landesliga Nord', 2018, 2, '2017-10-31', 5, 1),
(4, 1, 'Relegation Landesliga Süd', 2018, 2, '2017-10-31', 5, 2),
(5, 1, 'Bezirksoberliga Neckar', 2018, 2, '2017-10-31', 5, 4),
(6, 1, 'Bezirksliga A Neckar', 2018, 2, '2017-10-31', 5, 5),
(7, 1, 'Bezirksoberliga Oberschwaben', 2018, 2, '2017-10-31', 5, 4),
(8, 1, 'Bezirksliga A Oberschwaben', 2018, 2, '2017-10-31', 5, 7),
(9, 1, 'Bezirksliga B Oberschwaben', 2018, 2, '2017-10-31', 5, 8)
;

INSERT INTO veranstaltung (-- Compound Ligen
                           veranstaltung_id,
                           veranstaltung_wettkampftyp_id,
                           veranstaltung_name,
                           veranstaltung_sportjahr,
                           veranstaltung_ligaleiter_id,
                           veranstaltung_meldedeadline,
                           veranstaltung_kampfrichter_anzahl,
                           veranstaltung_hoehere)
VALUES
(20, 1, 'Bawü Compound Finale', 2018, 2, '2017-10-31', 5, NULL),
(21, 1, 'Württembergliga Compound', 2018, 2, '2017-10-31', 5, 20),
(22, 1, 'Landesliga A Compound', 2018, 2, '2017-10-31', 5, 21),
(23, 1, 'Landesliga B Compound', 2018, 2, '2017-10-31', 5, 22)
;

