INSERT INTO ligatabelle ( -- Recurve Ligen
  ligatabelle_veranstaltung_id,
  ligatabelle_wettkampf_tag,
  ligatabelle_mannschaft_id,
  ligatabelle_tabellenplatz,
  ligatabelle_matchpkt,
  ligatabelle_matchpkt_gegen,
  ligatabelle_satzpkt,
  ligatabelle_satzpkt_gegen
  )
VALUES
(0, 0, 101, 1, 0,0,0,0),-- initial
(0, 0, 102, 2, 0,0,0,0),
(0, 0, 103, 3, 0,0,0,0),
(0, 0, 104, 4, 0,0,0,0),
(0, 0, 105, 5, 0,0,0,0),
(0, 0, 106, 6, 0,0,0,0),
(0, 0, 107, 7, 0,0,0,0),
(0, 0, 108, 8, 0,0,0,0),
(0, 1, 105, 1, 12,2,36,14),-- Wettkampftag1
(0, 1, 103, 2, 10,4,36,24),
(0, 1, 102, 3, 10,4,34,26),
(0, 1, 107, 4, 8,6,34,24),
(0, 1, 101, 5, 6,8,28,34),
(0, 1, 106, 6, 6,8,22,30),
(0, 1, 108, 7, 4,10,23,37),
(0, 1, 104, 8, 0,14,15,43)
;


/*
 *Willkommen auf der Seite der Württembergliga des WSV
 *
 *Folgende Mannschaften nehmen im Sportjahr 2018 an der Württembergliga Bogen des WSV teil:
 *
 *    SV Brochenzell
 *    SGes Gerstetten
 *    SSV Ehingen
 *    SGes Bempflingen
 *    BSC Stuttgart
 *    BC Magstadt
 *    SV Schwieberdingen
 *    SV Kirchberg / Iller
 *
 *Termine und Austragungsorte:
 *1. Wettkampftag
 *
 *19.11.2017
 *Sporthalle, 88454 Hochdorf/Riss
 *13:30 Uhr Einschießen
 *14:00 Uhr Wettkampfbeginn
 *2. Wettkampftag
 *
 *09.12.2017
 *Franz-Baum Bogenhalle, 73642 Welzheim
 *13:30 Uhr Einschießen
 *14:00 Uhr Wettkampfbeginn
 *3. Wettkampftag
 *
 *14.01.2018
 *Bogenhalle am Schützenhaus, Gurgelbergweg, 88499 Altheim-Waldhausen
 *13:30 Uhr Einschießen
 *14:00 Uhr Wettkampfbeginn
 *4. Wettkampftag
 *
 *03.02.2018
 *Franz-Baum Bogenhalle, 73642 Welzheim
 *09:30 Uhr Einschießen
 *10:00 Uhr Wettkampfbeginn
*/