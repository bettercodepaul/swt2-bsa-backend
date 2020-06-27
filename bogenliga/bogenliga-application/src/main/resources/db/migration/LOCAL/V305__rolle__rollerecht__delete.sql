/* Annahme: leere Menge, die Rollen werden nicht verwendet */
select * from benutzer_rolle
where benutzer_rolle_rolle_id in (101,102,103,104,105,106)
;

/* Zuordnung von Rechten löschen */
DELETE from rolle_recht
where rolle_recht_rolle_id in (101, 102, 103, 104, 105, 106)
;

/* Rollen löschen*/
DELETE from rolle
where rolle_id in (101, 102, 103, 104, 105, 106)
;



