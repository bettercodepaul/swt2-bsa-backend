# Datenbankmigration

Die Datenmigration erfolgt über SQL-Skripte. 

- Der `all`-Ordner enthält alle Änderungen am Schema:
  `CREATE ...`, `ALTER ...`, `DROP ...`
- Der `LOCAL`-Ordner enthält die Daten und deren Änderungen: `INSERT INTO ...`, `UPDATE ...`, `DELETE ...`


Dateinamen: `V<Version>_<Sub-Version>__<Beschreibung>` 
ergibt Version `<Version>.<Sub-Version>`, z.B. Version 1.2



Beispiele:

```
Reihenfolge:

1   all/V1__create_mytable.sql
2   LOCAL/V1_1__insert_data_to_mytable.sql

-> Version 1 legt eine Tabelle an und 
Version 1.1 befüllt die Tabelle.
  

1   all/V2__create_othertable.sql
2   LOCAL/V2_1__insert_data_to_othertable.sql
3   all/V2_2__alter_othertable_add_newcolumn.sql
4   LOCAL/V2_3__update_othertable_and_define_default_values_of_newcolumn.sql

-> Version 2 kümmert sich um Tabelle "othertable". 
Änderungen am Schema liegen unter 'all' und 
Änderungen an den Daten unter 'LOCAL'.
  

1   all/V3__create_table.sql
2   all/V4__create_othertable.sql
3   LOCAL/V3_1__insert_data_to_table.sql
4   all/V3_2__alter_table_add_newcolumn.sql
5   LOCAL/V3_3__update_table_and_define_default_values_of_newcolumn.sql

-> Da eine out-of-order Migrationsstrategie genutzt wird, 
kann die Version 4 noch vor Version 3.1 angelegt werden.
```

Jedes Mal, wenn ein SQL-Skript angelegt 
oder GEÄNDERT(!) wird, führt Flyway das Skript aus (out-of-order Migrationsstrategie). Daher unbedingt darauf achten, 
dass nur vor einem Push eure Skripte geändert werden dürfen. 

Alle Skripte im GitHub-Repository dürfen nicht mehr geändert werden. 
Falls ihr etwas ändern wollt, müsst ihr ein neues Skript anlegen. 
**Daher testet die Datenbankmigration vor einem Push.** 


Zum Beispiel könnt ihr mit 
```
$ docker-compose stop db && docker-compose rm -f db && docker-compose up -d db
```
im `swt2-bsa-infrastructure/docker` Ordner die Datenbank komplett neu erstellen oder 
ihr führt eine `flyway clean` aus.

Danach könnt ihr das Backend neu starten, um die Migration auszulösen.