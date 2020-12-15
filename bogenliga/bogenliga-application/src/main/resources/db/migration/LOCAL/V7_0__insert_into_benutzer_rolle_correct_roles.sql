-- insert correct user roles for the specific id's
-- see https://www.exxcellent.de/jira/browse/BSAPP-741 for more information
-- ATTENTION: THIS FILE SHOULD ONLY BE EXECUTED IN THE LOCAL ENVIROMENT
INSERT INTO benutzer_rolle
    (benutzer_rolle_benutzer_id, benutzer_rolle_rolle_id)
VALUES (1, 1), (2, 9), (3, 6), (4, 9), (5, 6), (6, 8), (7, 2), (8, 5), (9,9);