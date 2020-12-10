-- temporary deletes all values from table 'benutzer_rolle' to build them up later
-- because most id's are assigned to the wrong user-roles or one user more than one user role
-- see https://www.exxcellent.de/jira/browse/BSAPP-741 for more information
-- ATTENTION: THIS FILE SHOULD ONLY BE EXECUTED IN THE LOCAL ENVIROMENT
DELETE FROM benutzer_rolle;