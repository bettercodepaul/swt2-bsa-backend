SET search_path = 'prod'
;

INSERT INTO klasse (klasse_id, klasse_name, klasse_alter_min, klasse_alter_max, klasse_nr)
VALUES

(0,'Herren' ,21, 49 ,10),
(1,'Damen' ,21 ,49 ,11),
(2,'Schüler A männl.' ,13 ,14 ,20),
(3,'Schüler A weibl.' ,13 ,14 ,21),
(4,'Schüler B männl.' ,11 ,12 ,22),
(5,'Schüler B weibl.' ,11 ,12 ,23),
(6,'Jugend männl.' ,15 ,17 ,30),
(7,'Jugend weibl.' ,15 ,17 ,31),
(8,'Junioren' ,18 ,20 ,40),
(9,'Junioren weibl.' ,18 ,20 ,41),
(10,'Master m' ,50 ,65 ,12),
(11,'Master w' ,50 ,65 ,13),
(12,'Senioren' ,66 ,99 ,14),
(13,'Senioren weibl.' ,66 ,99 ,15),
(14,'unbestimmt' ,00 ,99 ,16)
;


