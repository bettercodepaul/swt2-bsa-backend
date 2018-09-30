INSERT INTO region (region_id, region_name, region_kuerzel, region_typ, region_uebergeordnet)
VALUES
  (0, 'Deutscher Schützenbund',             'DSB',      'BUNDESVERBAND', NULL),
  (1, 'Württembergischer Schützenverband',  'WT',       'LANDESVERBAND', 0),
  (2, 'Badischer Sportschützenverband',     'BD',       'LANDESVERBAND', 0),
  (3, 'Bezirk Stuttgart',                   'BZ S',     'BEZIRK', 1),
  (4, 'Bezirk Ludwigsburg',                 'BZ LB',    'BEZIRK', 1),
  (5, 'Kreis Stuttgart',                    'KR S',     'KREIS', 3),
  (6, 'Kreis Ludwigsburg',                  'KR LB',    'KREIS', 4),
  (7, 'Kreis Welzheim',                     'KR WZ',    'KREIS', 8),
  (8, 'Bezirk Welzheim',                    'BZ WZ',    'BEZIRK', 1),
  (9, 'Bezirk Baden',                       'BZ BA',    'BEZIRK', 2),
  (10, 'Kreis Baden-Baden',                 'KR B-B',   'KREIS', 9),
  (11, 'Kreis Nürtingen',                   'KR NT',    'KREIS', 8),
  (12, 'Bezirk Nürtingen',                  'BZ NT',    'BEZIRK', 1)
;