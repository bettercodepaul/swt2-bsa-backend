/*
Add Role Can create DSB-Mitglied for Ausrichter for their own Veranstaltung
*/
/* Ausrichter, DSB-Mitglied anlegen und DSB-Mitglied bearbeiten für seinen eigenen Verein*/
INSERT INTO rolle_recht(rolle_recht_rolle_id,
                        rolle_recht_recht_id)
VALUES (4, 26);
INSERT INTO rolle_recht(rolle_recht_rolle_id,
                        rolle_recht_recht_id)
VALUES (4, 27);

/*  (Admin), Ligaleiter, Ausrichter brauchen die Berechtigung für das Spotter-Interface   */
INSERT INTO rolle_recht(rolle_recht_rolle_id,
                        rolle_recht_recht_id)
VALUES (2, 17);
INSERT INTO rolle_recht(rolle_recht_rolle_id,
                        rolle_recht_recht_id)
VALUES (4, 17);
