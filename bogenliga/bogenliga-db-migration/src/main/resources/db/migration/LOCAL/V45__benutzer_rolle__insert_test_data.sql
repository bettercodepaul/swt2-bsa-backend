INSERT INTO benutzer_rolle(
  benutzer_rolle_benutzer_id,
  benutzer_rolle_rolle_id
)
VALUES (1, 1), -- admin
       (2, 2), -- moderator
       (3, 3), -- user
       (4, 2), -- moderator and user
       (4, 3),
       (5, 3),
       (1, 5), -- Kampfrichter
       (2, 5),
       (3, 5),
       (5, 5)
;

-- user