INSERT INTO benutzer_rolle(
  benutzer_rolle_benutzer_id,
  benutzer_rolle_rolle_id
)
/*VALUES (1, 1), -- admin
       (2, 2), -- ligaleiter
       (3, 2),
       (4, 2), -- ligaleiter
       (4, 3),
       (5, 4), -- ausrichter
       (1, 4),
       (2, 5),
       (3, 5),
       (5, 5)
;*/
/*VALUES (1, 101), -- admin
       (2, 106), -- ligaleiter
       (3, 103),
       (4, 106), -- ligaleiter
       (4, 103),
       (5, 104), -- ausrichter
       (1, 104),
       (2, 105),
       (5, 105),
       (6, 105),
       (7, 106), -- ligaleiter (Team)
       (8, 104), -- sportleiter (Team)
       (9, 102) -- moderator (Team)
;*/

VALUES (1, 1), -- admin
       (2, 2), -- ligaleiter
       (3, 6),
       (4, 2), -- ligaleiter
       (4, 3),
       (5, 4), -- ausrichter
       (1, 4),
       (2, 5),
       (3, 5),
       (5, 5),
       (6, 8), -- default user
       (7, 2), -- ligaleiter (Team)
       (8, 5), -- sportleiter (Team)
       (9, 102) -- moderator (Team)
;

-- user
