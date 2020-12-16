-- delete two KAMPFRICHTER that were only created for testing purposes
-- ATTENTION: THIS FILE SHOULD ONLY BE EXECUTED IN THE LOCAL ENVIROMENT
-- https://www.exxcellent.de/jira/browse/BSAPP-123
DELETE FROM kampfrichter WHERE kampfrichter_benutzer_id = 4 AND kampfrichter_wettkampf_id = 32;
