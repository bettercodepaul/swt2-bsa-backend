INSERT INTO rolle_recht(
  rolle_recht_rolle_id,
  rolle_recht_recht_id
)
VALUES (1, 18),
       (1, 17),
       (1, 21),
       (1, 22),
       (1, 23),
       (1, 24),
       (1, 25),
       (2,8),
       (2,4),
       (2,11),
       (2,12),
       (2,21),
       (2,22),
       (2,23),
       (2,25),
       (3,4),
       (3,1),
       (3,7),
       (3,21),
       (4,4),
       (4,21),
       (4,13),
       (4,14),
       (5,4),
       (5,1),
       (5,21),
       (5,26),
       (5,27);

delete from rolle_recht
where rolle_recht_rolle_id = 1 and rolle_recht_recht_id BETWEEN 9 and 14;
