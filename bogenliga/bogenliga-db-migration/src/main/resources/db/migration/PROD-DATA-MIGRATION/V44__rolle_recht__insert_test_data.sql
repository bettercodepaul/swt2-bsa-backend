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
       (2, 1), -- moderator = all business read/ write permissions
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (3, 1), -- user = all business read permissions
       (3, 3),
       (3, 5)
;
