INSERT INTO region (region_id, region_name, region_kuerzel, region_typ, region_uebergeordnet)
VALUES
  (0, 'Deutscher Schützenbund',             'DSB',      'BUNDESVERBAND', NULL),
  (1, 'Württembergischer Schützenverband',  'WT',       'LANDESVERBAND', 0),
  (2, 'Badischer Sportschützenverband',     'BD',       'LANDESVERBAND', 0),
  (3, 'Bezirk Stuttgart',                   'BZ S',     'BEZIRKSVERBAND', 1),
  (4, 'Bezirk Ludwigsburg',                 'BZ LB',    'BEZIRKSVERBAND', 1),
  (5, 'Kreis Stuttgart',                    'KR S',     'KREISVERBAND', 3),
  (6, 'Kreis Ludwigsburg',                  'KR LB',    'KREISVERBAND', 4), -- TODO kein unique Kürzel?
  (7, 'Kreis Welzheim',                     'KR LB',    'KREISVERBAND', 8), -- TODO kein unique Kürzel?
  (8, 'Bezirk Welzheim',                    'BZ LB',    'BEZIRKSVERBAND', 1),
  (9, 'Bezirk Baden',                       'BZ BA',    'BEZIRKSVERBAND', 2),
  (10, 'Kreis Baden-Baden',                 'KR B-B',   'KREISVERBAND', 9),
  (11, 'Kreis Nürtingen',                   'KR LB',    'KREISVERBAND', 8),
  (12, 'Bezirk Nürtingen',                  'BZ LB',    'BEZIRKSVERBAND', 1)
;