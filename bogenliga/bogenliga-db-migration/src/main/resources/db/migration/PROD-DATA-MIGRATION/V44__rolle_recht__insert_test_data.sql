SET search_path = 'prod'
;
INSERT INTO rolle_recht(rolle_recht_rolle_id,
                        rolle_recht_recht_id)
VALUES (1, 1), -- admin = all permissions (technical and business)
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (2, 1), -- moderator = all business read/ write permissions
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 9),
       (2, 10),
       (2, 11),
       (3, 1), -- user = all business read permissions
       (3, 3),
       (3, 5),
       (3, 9),
       (4, 1), -- SPORTLEITER = all business read permissions & modify his club  (verein)
       (4, 3),
       (4, 5),
       (4, 9),
       (4, 10),
       (5, 1), -- KAMPFRICHTER = all business read permissions & modify his event  (wettkampf)
       (5, 3),
       (5, 4),
       (5, 5),
       (5, 9),
       (6, 1), -- LIGALEITER = all business read permissions & modify events and all clubs  (verein, wettkampf)
       (6, 3),
       (6, 4),
       (6, 5),
       (6, 9),
       (6, 11)
 ;
