

INSERT INTO benutzer(
benutzer_id,
benutzer_email,
benutzer_salt,
benutzer_password
)
VALUES
/*-- password = admin
       (1,
        'admin@bogenliga.de',
        'a9a2ef3c5a023acd2fc79ebd9c638e0ebb62db9c65fa42a6ca43d5d957a4bdf5413c8fc08ed8faf7204ba0fd5805ca638220b84d07c0690aed16ab3a2413142d',
        '0d31dec64a1029ea473fb10628bfa327be135d34f8013e7238f32c019bd0663a7feca101fd86ccee1339e79db86c5494b6e635bb5e21456f40a6722952c53079'),
*/
  -- password = moderator

       (2,
        'moderator@bogenliga.de',
        'dbed56d612f8fc8397a79a9e63cc67236ac63027e092adda7b02cbe7c65a4916683a572d71d3cefbcdcf86ee42136b1882ce75b189b1fe3a1457cc72ced3c6ea',
        '3afca75fad3ea4e11e3e1f4274221acb4f0a833e765b21c87098c18c9ebea67eec16f849cffc4f0010ea0f6879d0a8b88c4cfd64abfcd4762cf5c123e87f0a45'),
-- password = user
       (3,
        'user@bogenliga.de',
        '99967378dbe9dcc2f13111a8d031b34918a3e45e311961587d34bdedf897d521e4e9c1808d3f393c351d6f09648feba88436573ba5019ca15d00050bf4d5857e',
        'eac206585d6589abd083febda343f6ce00504791ef915da992dd09f695cecce06ddc92d58feb1967f95b8af2a78f0a78b79ba143870b16e14f62740d95dad594'),
-- password = swt
       (4,
        'Nicholas.Corle@bogenliga.de',
        '7e287c3b7f9f8a7193f007347d7fc784a647536892bd35fc7daee318917aa7544dff21b6454e56786cf8b9263df89811c3bf14309e04025d6b8e369240f61746',
        '4c2b281392d5667a043864ab4231f558eb686c4b28fd10797de60fee89a9e3d808ad31ec0d455f4f89fdc4db84efccdfa4f9d820e5ed5f577aab5e9288bf9196'),
-- password = swt
       (5,
        'Malorie.Artman@bogenliga.de',
        '7e287c3b7f9f8a7193f007347d7fc784a647536892bd35fc7daee318917aa7544dff21b6454e56786cf8b9263df89811c3bf14309e04025d6b8e369240f61746',
        '4c2b281392d5667a043864ab4231f558eb686c4b28fd10797de60fee89a9e3d808ad31ec0d455f4f89fdc4db84efccdfa4f9d820e5ed5f577aab5e9288bf9196'),
-- password = user
       (6,
       'ligadefault',
       '99967378dbe9dcc2f13111a8d031b34918a3e45e311961587d34bdedf897d521e4e9c1808d3f393c351d6f09648feba88436573ba5019ca15d00050bf4d5857e',
        'eac206585d6589abd083febda343f6ce00504791ef915da992dd09f695cecce06ddc92d58feb1967f95b8af2a78f0a78b79ba143870b16e14f62740d95dad594'),
-- password = swt
       (7,
        'TeamLigaleiter@bogenliga.de',
        '7e287c3b7f9f8a7193f007347d7fc784a647536892bd35fc7daee318917aa7544dff21b6454e56786cf8b9263df89811c3bf14309e04025d6b8e369240f61746',
        '4c2b281392d5667a043864ab4231f558eb686c4b28fd10797de60fee89a9e3d808ad31ec0d455f4f89fdc4db84efccdfa4f9d820e5ed5f577aab5e9288bf9196'),
-- password = swt
       (8,
        'TeamSportleiter@bogenliga.de',
        '7e287c3b7f9f8a7193f007347d7fc784a647536892bd35fc7daee318917aa7544dff21b6454e56786cf8b9263df89811c3bf14309e04025d6b8e369240f61746',
        '4c2b281392d5667a043864ab4231f558eb686c4b28fd10797de60fee89a9e3d808ad31ec0d455f4f89fdc4db84efccdfa4f9d820e5ed5f577aab5e9288bf9196'),
-- password = swt
       (9,
        'TeamModerator@bogenliga.de',
        '7e287c3b7f9f8a7193f007347d7fc784a647536892bd35fc7daee318917aa7544dff21b6454e56786cf8b9263df89811c3bf14309e04025d6b8e369240f61746',
        '4c2b281392d5667a043864ab4231f558eb686c4b28fd10797de60fee89a9e3d808ad31ec0d455f4f89fdc4db84efccdfa4f9d820e5ed5f577aab5e9288bf9196')
;

-- DSB Mitglieder ohne Benutzer Account
INSERT INTO dsb_mitglied (dsb_mitglied_id,
                          dsb_mitglied_vorname,
                          dsb_mitglied_nachname,
                          dsb_mitglied_geburtsdatum,
                          dsb_mitglied_nationalitaet,
                          dsb_mitglied_mitgliedsnummer,
                          dsb_mitglied_verein_id)
VALUES (28, 'Nicholas', 'Corle', '1988-01-01', 'D', '89298571', 0),
       (30, 'Malorie', 'Artman', '1978-01-01', 'D', '80179421', 11),
       (31, 'Kassie', 'Hysmith', '1986-01-01', 'D', '87721795', 0),
       (32, 'Phebe', 'Biddle', '1990-01-01', 'D', '92039767', 11),
       (35, 'Stephaine', 'Tovar', '1998-01-01', 'D', '14168219', 5),
       (36, 'Shanae', 'Balding', '1999-01-01', 'D', '20401429', 6),
       (44, 'Shiloh', 'Pegues', '1965-01-01', 'D', '664B0918', 7),
       (45, 'Vicente', 'Strand', '1957-01-01', 'D', '59455978', 5),
       (46, 'Darrell', 'Woodham', '1973-01-01', 'D', '23770976', 6),
       (47, 'Hai', 'Antle', '1964-01-01', 'D', '66075169', 0),
       (48, 'Cammy', 'Scola', '1990-01-01', 'D', '91896623', 11),
       (51, 'Susann', 'Dowdell', '1980-01-01', 'D', '81075269', 7),
       (70, 'Jena', 'Gingras', '1967-01-01', 'D', '69400591', 0),
       (71, 'Jacqui', 'Voltz', '1991-01-01', 'D', '94154216', 11),
       (72, 'Beverley', 'Germany', '1986-01-01', 'D', '26738514', 5),
       (73, 'Rhea', 'Woodward', '1969-01-01', 'D', '71882193', 6),
       (76, 'Tosha', 'Ingles', '1949-01-01', 'D', '510815A3', 5),
       (77, 'Meghann', 'John', '1978-01-01', 'D', '80071582', 6),
       (78, 'Latrina', 'Grooms', '1992-01-01', 'D', '93807031', 7),
       (79, 'Karie', 'Minott', '1978-01-01', 'D', '79781826', 0),
       (80, 'Barbra', 'Thiessen', '1980-01-01', 'D', '81913821', 11),
       (91, 'Jeraldine', 'Remigio', '1969-01-01', 'D', '70340570', 10),
       (103, 'Lissa', 'Veach', '1995-01-01', 'D', '96527009', 11),
       (104, 'Cathryn', 'Ebron', '1962-01-01', 'D', '64230502', 5),
       (109, 'Wilbur', 'Corter', '1980-01-01', 'D', '16321064', 6),
       (110, 'Darci', 'Crist', '1964-01-01', 'D', '16124527', 7),
       (121, 'Vina', 'Fite', '2000-01-01', 'D', '11997585', 10),
       (122, 'Sherilyn', 'Kind', '1964-01-01', 'D', '65853805', 11),
       (123, 'Bailey', 'Michelsen', '1968-01-01', 'D', '69482200', 5),
       (127, 'Cortez', 'Twiggs', '1978-01-01', 'D', '79821028', 12),
       (129, 'Barb', 'Digiovanni', '1953-01-01', 'D', '54533330', 7),
       (132, 'Velda', 'Truax', '1954-01-01', 'D', '55856311', 10),
       (174, 'Nona', 'Akbar', '1963-01-01', 'D', '65017877', 11),
       (176, 'Cindy', 'Parise', '1959-01-01', 'D', '61430786', 10),
       (177, 'Jong', 'Robey', '1999-01-01', 'D', '11503580', 11),
       (178, 'Bonita', 'Preas', '1998-01-01', 'D', '99525317', 5),
       (180, 'Shizuko', 'Berndt', '1991-01-01', 'D', '93635103', 12),
       (182, 'Florence', 'Riggle', '1985-01-01', 'D', '87444779', 7),
       (186, 'Vernita', 'Costa', '1960-01-01', 'D', '62026375', 10),
       (187, 'Lue', 'Caudillo', '1957-01-01', 'D', '58703571', 11),
       (188, 'Sherley', 'Pelc', '1950-01-01', 'D', '52646729', 5),
       (189, 'Ranae', 'Coache', '1985-01-01', 'D', '87390776', 3),
       (190, 'Billy', 'Munro', '1959-01-01', 'D', '60883700', 9),
       (192, 'Collin', 'Gallup', '1987-01-01', 'D', '88667686', 5),
       (193, 'Bernita', 'Rutt', '1967-01-01', 'D', '68789531', 3),
       (216, 'Noella', 'Leung', '1985-01-01', 'D', '86660392', 9),
       (217, 'Jaye', 'Boer', '1969-01-01', 'D', '71012688', 10),
       (218, 'April', 'Holzer', '1995-01-01', 'D', '96867609', 11),
       (219, 'Jaime', 'Mastronardi', '1961-01-01', 'D', '63045871', 5),
       (220, 'Twyla', 'Marcella', '1973-01-01', 'D', '74841017', 12),
       (240, 'Ricarda', 'Terhune', '1963-01-01', 'D', '65207326', 9),
       (244, 'Milton', 'Fassett', '1962-01-01', 'D', '64745105', 10),
       (248, 'Julius', 'Scrivens', '1973-01-01', 'D', '76924684', 11),
       (249, 'Marcos', 'Ringo', '1973-01-01', 'D', '76460106', 3),
       (250, 'Dale', 'Barranco', '1973-01-01', 'D', '17405616', 7),
       (364, 'Hester', 'Wigger', '1975-01-01', 'D', '76460006', 10),
       (383, 'Sharan', 'Ratchford', '1968-01-01', 'D', '71653622', 11),
       (384, 'Shantae', 'Upshur', '1974-01-01', 'D', '76319812', 10),
       (385, 'Connie', 'Keala', '1996-01-01', 'D', '98064586', 1),
       (386, 'Birdie', 'Kulig', '1979-01-01', 'D', '81074095', 5),
       (387, 'Latosha', 'Cheung', '1976-01-01', 'D', '78736627', 6),
       (390, 'Martina', 'Kirkley', '1980-01-01', 'D', '44072584', 7),
       (392, 'Steve', 'Lohman', '1979-01-01', 'D', '80618272', 10),
       (393, 'Cindie', 'Proulx', '1989-01-01', 'D', '91442478', 1),
       (394, 'Florentina', 'Binns', '1992-01-01', 'D', '94649417', 5),
       (396, 'Vicki', 'Timmer', '1962-01-01', 'D', '64027692', 6),
       (406, 'Allegra', 'Bumgarner', '2001-01-01', 'D', '47734647', 7),
       (407, 'Vertie', 'Speier', '1979-01-01', 'D', '77822807', 5),
       (408, 'Barrie', 'Peet', '1980-01-01', 'D', '44172584', 6),
       (440, 'Shirley', 'Bierce', '2002-01-01', 'D', '14655109', 10),
       (441, 'Carletta', 'Hayden', '1984-01-01', 'D', '85999688', 1),
       (442, 'Love', 'Hogan', '2002-01-01', 'D', '35480090', 7),
       (443, 'Cierra', 'Marshell', '1982-01-01', 'D', '12991780', 5),
       (447, 'Ouida', 'Shkreli', '1987-01-01', 'D', '35621209', 6),
       (448, 'Burl', 'Hopkins', '1976-01-01', 'D', '77689293', 7),
       (449, 'Scottie', 'Rickenbacker', '1961-01-01', 'D', '63481366', 10),
       (450, 'Valarie', 'Mchugh', '1955-01-01', 'D', '73G69595', 5),
       (451, 'Elin', 'Leitner', '1967-01-01', 'D', '68660172', 6)
;

-- DSB Mitglieder mit Benutzer Account
UPDATE dsb_mitglied
SET dsb_mitglied_benutzer_id = 4
WHERE dsb_mitglied_id = 28
;

UPDATE dsb_mitglied
SET dsb_mitglied_benutzer_id = 5
WHERE dsb_mitglied_id = 30
;

INSERT INTO lizenz (
    lizenz_id,
    lizenz_nummer,
    lizenz_region_id,
    lizenz_dsb_mitglied_id,
    lizenz_typ,
    lizenz_disziplin_id)
VALUES

(0, 'WT1234567', 1, 71, 'Liga', 0),
(1, 'WT12340007', 1, 103, 'Liga', 0),
(2, '0012348990', 1, 450, 'Liga', 0),
(3, '12345899', 1, 441, 'Kampfrichter', NULL),
(4, '1234589001', 1, 442, 'Kampfrichter', NULL),
(5, '123458910', 1, 450, 'Kampfrichter', NULL)
;
INSERT INTO veranstaltung (-- Recurve Ligen
                           veranstaltung_id,
                           veranstaltung_wettkampftyp_id,
                           veranstaltung_name,
                           veranstaltung_sportjahr,
                           veranstaltung_ligaleiter_id,
                           veranstaltung_liga_id,
                           veranstaltung_meldedeadline)
VALUES
(0, 1, 'Würtembergliga 2018', 2018, 7,2, '2017-10-31'),
(1, 1, 'Landesliga Nord 2018', 2018, 7,3, '2017-10-31'),
(2, 1, 'Landesliga Süd 2018', 2018, 7,4, '2017-10-31'),
(3, 1, 'Relegation Landesliga Nord 2019', 2019, 7,3, '2017-10-31'),
(4, 1, 'Relegation Landesliga Süd 2019', 2019, 7,4, '2017-10-31'),
(5, 1, 'Bezirksoberliga Neckar 2019', 2019, 7,5, '2017-10-31'),
(6, 1, 'Bezirksliga A Neckar 2020', 2020, 7,6, '2017-10-31'),
(7, 1, 'Bezirksoberliga Oberschwaben 2024', 2024, 7,5, '2017-10-31'),
(8, 1, 'Bezirksliga A Oberschwaben 2024', 2024, 7,6, '2017-10-31'),
(9, 1, 'Bezirksliga B Oberschwaben 2024', 2024, 7,7, '2017-10-31')
;

INSERT INTO veranstaltung (-- Compound Ligen
                           veranstaltung_id,
                           veranstaltung_wettkampftyp_id,
                           veranstaltung_name,
                           veranstaltung_sportjahr,
                           veranstaltung_ligaleiter_id,
                           veranstaltung_liga_id,
                           veranstaltung_meldedeadline)
VALUES
(20, 1, 'Bawü Compound Finale 2021', 2021, 2,1, '2017-10-31'),
(21, 1, 'Württembergliga Compound 2021', 2021, 2,2, '2017-10-31'),
(22, 1, 'Landesliga A Compound 2021', 2021, 2,6, '2017-10-31'),
(23, 1, 'Landesliga B Compound 2021', 2021, 2,7, '2017-10-31')
;

INSERT INTO wettkampf ( -- Recurve Ligen
  wettkampf_id,
  wettkampf_veranstaltung_id,
  wettkampf_datum,
  wettkampf_strasse,
  wettkampf_plz,
  wettkampf_ortsname,
  wettkampf_beginn,
  wettkampf_tag,
  wettkampf_disziplin_id,
  wettkampf_wettkampftyp_id)
VALUES
(38, 0, '1900-01-01', ' - ', ' - ', ' - ', ' - ', 0, 0, 0),
(30, 0, '2017-11-19', 'Sporthalle', '88454', 'Hochdorf/Riss', '13:30', 1, 0, 0),
(31, 0, '2017-12-09', 'Franz-Baum Bogenhalle', '73642', 'Welzheim', '13:30', 2, 0, 0),
(32, 0, '2018-01-14', 'Bogenhalle, Gurgelbergweg', '88499', 'Altheim-Waldhausen', '13:30', 3, 0, 1),
(33, 0, '2018-02-03', 'Franz-Baum Bogenhalle', '73642', ' Welzheim', '09:30', 4, 0, 1),
(34, 1, '2019-11-19', 'Sporthalle', '88454', 'Hochdorf/Riss', '13:30', 1, 0, 0),
(35, 1, '2019-12-09', 'Franz-Baum Bogenhalle', '73642', 'Welzheim', '13:30', 2, 0, 0),
(36, 1, '2020-01-14', 'Bogenhalle, Gurgelbergweg', '88499', 'Altheim-Waldhausen', '13:30', 3, 0, 1),
(37, 1, '2020-02-03', 'Franz-Baum Bogenhalle', '73642', 'Welzheim', '09:30', 4, 0, 1),
(39, 1, '1900-01-01', ' - ', ' - ', ' - ', ' - ', 0, 0, 0),
(40, 2, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(41, 3, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(42, 4, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(43, 5, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(44, 6, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(45, 7, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(46, 8, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(47, 9, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(48, 20, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(49, 21, '1900-01-01',' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(50, 22, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0),
(51, 23, '1900-01-01', ' - ', ' - ', ' - ', ' - ',  0, 0, 0)
;

INSERT INTO mannschaft (mannschaft_id,
                        mannschaft_verein_id,
                        mannschaft_nummer,
                        mannschaft_benutzer_id,
                        mannschaft_veranstaltung_id)
VALUES (101, 0, 1, 4, 0),
       (102, 1, 1, 4, 0),
       (103, 11, 1, 4, 0),
       (104, 3, 1, 4, 0),
       (105, 5, 1, 5, 0),
       (106, 7, 1, 5, 0),
       (107, 9, 1, 5, 0),
       (108, 12, 1, 5, 0)
;

INSERT INTO match (match_nr,
                   match_wettkampf_id,
                   match_begegnung,
                   match_mannschaft_id,
                   match_scheibennummer,
                   match_matchpunkte,
                   match_satzpunkte)
VALUES (1, 30, 1, 101, 1, 2, 6),
       (1, 30, 1, 102, 2, 0, 2),
       (1, 30, 2, 103, 3, 2, 6),
       (1, 30, 2, 104, 4, 0, 4),
       (1, 30, 3, 105, 5, 2, 6),
       (1, 30, 3, 106, 6, 0, 2),
       (1, 30, 4, 107, 7, 2, 7),
       (1, 30, 4, 108, 8, 0, 1),
       (2, 30, 1, 102, 1, 2, 6),
       (2, 30, 1, 107, 2, 0, 4),
       (2, 30, 2, 106, 3, 2, 6),
       (2, 30, 2, 101, 4, 0, 4),
       (2, 30, 3, 105, 5, 2, 6),
       (2, 30, 3, 103, 6, 0, 2),
       (2, 30, 4, 108, 7, 2, 7),
       (2, 30, 4, 104, 8, 0, 3),
       (3, 30, 1, 101, 1, NULL, NULL),
       (3, 30, 1, 103, 2, NULL, NULL),
       (3, 30, 2, 104, 3, NULL, NULL),
       (3, 30, 2, 102, 4, NULL, NULL),
       (3, 30, 3, 108, 5, NULL, NULL),
       (3, 30, 3, 105, 6, NULL, NULL),
       (3, 30, 4, 107, 7, NULL, NULL),
       (3, 30, 4, 106, 8, NULL, NULL),
       (4, 30, 1, 102, 1, NULL, NULL),
       (4, 30, 1, 105, 2, NULL, NULL),
       (4, 30, 2, 106, 3, NULL, NULL),
       (4, 30, 2, 104, 4, NULL, NULL),
       (4, 30, 3, 108, 5, NULL, NULL),
       (4, 30, 3, 101, 6, NULL, NULL),
       (4, 30, 4, 107, 7, NULL, NULL),
       (4, 30, 4, 103, 8, NULL, NULL)
;

INSERT INTO passe (passe_lfdnr,
                   passe_wettkampf_id,
                   passe_match_nr,
                   passe_mannschaft_id,
                   passe_dsb_mitglied_id,
                   passe_ringzahl_pfeil1,
                   passe_ringzahl_pfeil2,
                   passe_ringzahl_pfeil3,
                   passe_ringzahl_pfeil4,
                   passe_ringzahl_pfeil5,
                   passe_ringzahl_pfeil6)
VALUES (1, 30, 1, 101, 28, 9, 9, NULL, NULL, NULL, NULL),
       (1, 30, 1, 101, 31, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 101, 47, 10, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 101, 28, 9, 6, NULL, NULL, NULL, NULL),
       (2, 30, 1, 101, 31, 10, 10, NULL, NULL, NULL, NULL),
       (2, 30, 1, 101, 47, 10, 8, NULL, NULL, NULL, NULL),
       (3, 30, 1, 101, 28, 9, 6, NULL, NULL, NULL, NULL),
       (3, 30, 1, 101, 31, 10, 8, NULL, NULL, NULL, NULL),
       (3, 30, 1, 101, 47, 10, 8, NULL, NULL, NULL, NULL),
       (4, 30, 1, 101, 28, 9, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 101, 31, 10, 10, NULL, NULL, NULL, NULL),
       (4, 30, 1, 101, 47, 10, 8, NULL, NULL, NULL, NULL),
       (1, 30, 1, 102, 441, 8, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 102, 393, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 102, 385, 8, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 102, 441, 10, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 102, 393, 10, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 102, 385, 8, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 102, 441, 10, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 102, 393, 10, 8, NULL, NULL, NULL, NULL),
       (3, 30, 1, 102, 385, 9, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 102, 441, 10, 7, NULL, NULL, NULL, NULL),
       (4, 30, 1, 102, 393, 10, 8, NULL, NULL, NULL, NULL),
       (4, 30, 1, 102, 385, 10, 8, NULL, NULL, NULL, NULL),
       (1, 30, 1, 103, 71, 8, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 103, 80, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 103, 103, 8, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 103, 71, 10, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 103, 80, 10, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 103, 103, 10, 10, NULL, NULL, NULL, NULL),
       (3, 30, 1, 103, 71, 7, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 103, 80, 10, 8, NULL, NULL, NULL, NULL),
       (3, 30, 1, 103, 103, 9, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 103, 71, 10, 10, NULL, NULL, NULL, NULL),
       (4, 30, 1, 103, 80, 10, 8, NULL, NULL, NULL, NULL),
       (4, 30, 1, 103, 103, 10, 10, NULL, NULL, NULL, NULL),
       (5, 30, 1, 103, 71, 10, 7, NULL, NULL, NULL, NULL),
       (5, 30, 1, 103, 80, 10, 8, NULL, NULL, NULL, NULL),
       (5, 30, 1, 103, 103, 10, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 104, 193, 8, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 104, 189, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 104, 249, 8, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 104, 193, 8, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 104, 189, 7, 6, NULL, NULL, NULL, NULL),
       (2, 30, 1, 104, 249, 8, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 104, 193, 10, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 104, 189, 9, 6, NULL, NULL, NULL, NULL),
       (3, 30, 1, 104, 249, 10, 7, NULL, NULL, NULL, NULL),
       (4, 30, 1, 104, 193, 10, 7, NULL, NULL, NULL, NULL),
       (4, 30, 1, 104, 189, 7, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 104, 249, 8, 7, NULL, NULL, NULL, NULL),
       (5, 30, 1, 104, 193, 10, 7, NULL, NULL, NULL, NULL),
       (5, 30, 1, 104, 189, 10, 6, NULL, NULL, NULL, NULL),
       (5, 30, 1, 104, 249, 8, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 105, 45, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 105, 72, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 105, 104, 10, 9, NULL, NULL, NULL, NULL),
       (2, 30, 1, 105, 45, 7, 6, NULL, NULL, NULL, NULL),
       (2, 30, 1, 105, 72, 10, 10, NULL, NULL, NULL, NULL),
       (2, 30, 1, 105, 104, 9, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 105, 45, 10, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 105, 72, 9, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 105, 104, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 105, 45, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 105, 72, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 105, 104, 9, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 106, 44, 10, 8, NULL, NULL, NULL, NULL),
       (1, 30, 1, 106, 78, 10, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 106, 110, 10, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 106, 44, 10, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 106, 78, 10, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 106, 110, 9, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 106, 44, 10, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 106, 78, 9, 6, NULL, NULL, NULL, NULL),
       (3, 30, 1, 106, 110, 10, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 106, 44, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 106, 78, 10, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 106, 110, 9, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 107, 190, 10, 8, NULL, NULL, NULL, NULL),
       (1, 30, 1, 107, 216, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 107, 240, 10, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 107, 190, 8, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 107, 216, 8, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 107, 240, 6, 6, NULL, NULL, NULL, NULL),
       (3, 30, 1, 107, 190, 10, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 107, 216, 9, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 107, 240, 10, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 107, 190, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 107, 216, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 107, 240, 10, 9, NULL, NULL, NULL, NULL),
       (1, 30, 1, 108, 180, 10, 8, NULL, NULL, NULL, NULL),
       (1, 30, 1, 108, 220, 10, 6, NULL, NULL, NULL, NULL),
       (1, 30, 1, 108, 127, 10, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 108, 180, 10, 9, NULL, NULL, NULL, NULL),
       (2, 30, 1, 108, 220, 7, 6, NULL, NULL, NULL, NULL),
       (2, 30, 1, 108, 127, 6, 0, NULL, NULL, NULL, NULL),
       (3, 30, 1, 108, 180, 10, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 108, 220, 9, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 108, 127, 10, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 108, 180, 10, 8, NULL, NULL, NULL, NULL),
       (4, 30, 1, 108, 220, 10, 8, NULL, NULL, NULL, NULL),
       (4, 30, 1, 108, 127, 10, 9, NULL, NULL, NULL, NULL)

;


INSERT INTO kampfrichter (kampfrichter_benutzer_id, kampfrichter_wettkampf_id, kampfrichter_leitend)
VALUES (4, 32, TRUE),
       (4, 32, FALSE)
;

INSERT INTO benutzer_rolle(
  benutzer_rolle_benutzer_id,
  benutzer_rolle_rolle_id
)
VALUES -- (1, 1) -- admin im all-skript enthalten
       (2, 2), -- ligaleiter
       (2, 9), -- ligaleiter
       (3, 6),
       (4, 9), -- ligaleiter
       (4, 6),
       (5, 6), -- ausrichter
       (1, 4),
       (2, 5),
       (3, 5),
       (5, 5),
       (6, 8), -- default user
       (7, 2), -- ligaleiter (Team)
       (8, 5), -- sportleiter (Team)
       (9, 9) -- moderator (Team)
;
