
DELETE from rolle_recht;

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
       (8, 0) -- DEFAULT
 ;
