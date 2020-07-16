INSERT INTO rolle_recht(
  rolle_recht_rolle_id,
  rolle_recht_recht_id
)
VALUES (101, 1), -- admin = all permissions (technical and business)
       (101, 2),
       (101, 3),
       (101, 4),
       (101, 5),
       (101, 6),
       (101, 7),
       (101, 8),
       (101, 9),
       (101, 10),
       (101, 11),
       (102, 1), -- moderator = all business read/ write permissions
       (102, 2),
       (102, 3),
       (102, 4),
       (102, 5),
       (102, 6),
       (102, 9),
       (102, 10),
       (102, 11),
       (103, 1), -- user = all business read permissions
       (103, 3),
       (103, 5),
       (103, 9),
       (104, 1), -- SPORTLEITER = all business read permissions & modify his club  (verein)
       (104, 3),
       (104, 5),
       (104, 9),
       (104, 10),
       (105, 1), -- KAMPFRICHTER = all business read permissions & modify his event  (wettkampf)
       (105, 3),
       (105, 4),
       (105, 5),
       (105, 9),
       (106, 1), -- LIGALEITER = all business read permissions & modify events and all clubs  (verein, wettkampf)
       (106, 3),
       (106, 4),
       (106, 5),
       (106, 9),
       (106, 11)
 ;
