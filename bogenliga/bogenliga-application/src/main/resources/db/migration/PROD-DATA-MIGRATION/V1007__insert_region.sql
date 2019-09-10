SET search_path = 'prod'
;

DELETE from verein;
DELETE from region;

INSERT INTO region (region_id, region_name, region_kuerzel, region_typ, region_uebergeordnet)
VALUES
  (0, 'Deutscher Schützenbund',             'DSB',      'BUNDESVERBAND', NULL),
  (1, 'Württembergischer Schützenverband',  'WT',       'LANDESVERBAND', 0),
--
  (3, 'Bezirk Unterland',                   'BZ UL',     'BEZIRK', 1),
  (4, 'Kreis Backnang',                     'K BKNG',    'KREIS', 3),
  (5, 'Kreis Heilbronn',                    'K HN',      'KREIS', 3),
  (6, 'Kreis Ludwigsburg',                  'K LB',      'KREIS', 3),
  (7, 'Kreis Vaihingen',                    'K VAI',     'KREIS', 3),
--
  (8, 'Bezirk Hohenlohe',                   'BZ HL',     'BEZIRK', 1),
  (9, 'Kreis Bad Mergentheim',              'K MGH',     'KREIS', 8),
  (10, 'Kreis Crailsheim',                  'K CR',      'KREIS', 8),
  (11, 'Kreis Künzelsau',                   'K KÜN',     'KREIS', 8),
  (12, 'Kreis Öhringen',                    'K ÖHR',     'KREIS', 8),
  (13, 'Kreis Schwäbisch Hall',             'K SHA',     'KREIS', 8),
--
  (14, 'Bezirk Mittelschwaben',             'BZ MS',     'BEZIRK', 1),
  (15, 'Kreis Aalen',                       'K AA',      'KREIS', 14),
  (16, 'Kreis Heidenheim',                  'K HDH',     'KREIS', 14),
  (17, 'Kreis Hohenstaufen',                'K HHST',    'KREIS', 14),
  (18, 'Kreis Schwäbisch Gmünd',            'K GD',      'KREIS', 14),
--
  (19, 'Bezirk Neckar',                     'BZ NE',     'BEZIRK', 1),
  (20, 'Kreis Echaz-Neckar',                'K ECZN',    'KREIS', 19),
  (21, 'Kreis Hohenurach',                  'K HURA',    'KREIS', 19),
  (22, 'Kreis Teck',                        'K TECK',    'KREIS', 19),
  (23, 'Kreis Uhland',                      'K UHLD',    'KREIS', 19),
--
  (24, 'Bezirk Stuttgart',                  'BZ S',     'BEZIRK', 1),
  (25, 'Kreis Böblingen',                   'K BB',      'KREIS', 24),
  (26, 'Kreis Calw',                        'K CW',      'KREIS', 24),
  (27, 'Kreis Esslingen',                   'K ES',      'KREIS', 24),
  (28, 'Kreis Leonberg',                    'K LEO',     'KREIS', 24),
  (29, 'Kreis Stuttgart',                   'K S',       'KREIS', 24),
  (30, 'Kreis Waiblingen',                  'K WN',      'KREIS', 24),
--
  (31, 'Bezirk Schwarzwald-Hohenzollern',   'BZ SWH',    'BEZIRK', 1),
  (32, 'Kreis Freudenstadt',                'K FDS',      'KREIS', 31),
  (33, 'Kreis Neckarzollern',               'K NKZL',     'KREIS', 31),
  (34, 'Kreis Rottweil',                    'K RW',       'KREIS', 31),
  (35, 'Kreis Tuttlingen',                  'K TUT',      'KREIS', 31),
  (36, 'Kreis Zollernalb',                  'K BL',       'KREIS', 31),
--
  (37, 'Bezirk Oberschwaben',               'BZ OS',    'BEZIRK', 1),
  (38, 'Kreis Biberach-Iller',              'K BC',       'KREIS', 37),
  (39, 'Kreis Ehingen',                     'K EHI',      'KREIS', 37),
  (40, 'Kreis Ravensburg',                  'K RV',       'KREIS', 37),
  (41, 'Kreis Saulgau',                     'K SLG',      'KREIS', 37),
  (42, 'Kreis Bodensee',                    'K FN',       'KREIS', 37),
  (43, 'Kreis Ulm',                         'K UL',       'KREIS', 37),
  (44, 'Kreis Wangen',                      'K WG',       'KREIS', 37);
--

Insert INTO region
    (region_name, region_kuerzel, region_typ, region_uebergeordnet)
SELECT
    "lvtest",
    "lv",
    'LANDESVERBAND',
    '1'
from prod_data_migration."landesverbände"
where "lv"<>'WT';

    