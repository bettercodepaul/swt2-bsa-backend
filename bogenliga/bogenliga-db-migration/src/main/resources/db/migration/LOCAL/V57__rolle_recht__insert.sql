INSERT INTO rolle_recht(
  rolle_recht_rolle_id,
  rolle_recht_recht_id
)
VALUES (1, 0), -- admin = all permissions (technical and business)
       (1, 1),
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
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15),
       (1, 16),
       (2, 0), -- LIGALEITER
       (2, 1),
       (2, 7),
       (2, 11),
       (2, 12),
       (3, 0), -- KAMPFRICHTER
       (3, 7),
       (3, 11),
       (4, 0), -- Ausrichter
       (4, 1),
       (4, 7),
       (4, 13),
       (4, 14),
       (4, 15),
       (5, 0), -- SPORTLEITER
       (5, 7),
       (5, 9),
       (5, 10),
       (5, 15),
       (6, 0), -- USER
       (7, 0), -- TECHNISCHER-USER
       (7, 17),
       (8, 0), -- DEFAULT
       (9, 0) --MODERATOR
 ;
