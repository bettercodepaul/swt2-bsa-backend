INSERT INTO benutzer_rolle(
  benutzer_rolle_benutzer_id,
  benutzer_rolle_rolle_id
)
VALUES (1, 1), -- admin
       (2, 2), -- ligaleiter
       (3, 2),
       (4, 2), -- ligaleiter
       (4, 3),
       (5, 4), -- ausrichter
       (1, 4),
       (2, 5),
       (3, 5),
       (5, 5)
;

-- user