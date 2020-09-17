# Hinweise zur Datenmigration

Dieser Ordner enthält SQL-Skripte, die speziell für eine Umgebung gelten.
Dies sind in der Regel Skripte zum Anlegen oder Ändern von Daten, z.B. `INSERT`, `UPDATE` oder `DELETE`.

## Datei-Schema

`V{Versionsnummer der Tabelle}_0__insert_into_{Beschreibung der Daten}.sql`

Beispiele:

- `V1_1__insert_into_benutzer_create_default_user.sql` 
   
   für `V1_0__create_table_benutzer.sql` im all/ Ordner
- `V2_1__insert_into_rolle_create_default_roles.sql`
   
   für `V2_0__create_table_rolle.sql` im all/ Ordner
- `V3_1__insert_into_benutzer_rolle_grant_default_access_privileges.sql`
   
   für `V3_0__create_table_benutzer_rolle.sql` im all/ Ordner
- `V4_1__insert_into_dsb_mitglied_import_test_data.sql`
   
   für `V4_0__create_table_dsb_mitglied.sql` im all/ Ordner

# Hinweise zur Baseline

Die Skripte aus all/ und PROD/ wurden zusammen kopiert und in eine Datei geschrieben:

```
mkdir all+PROD                                                                 
cp all/* all+PROD 
cp PROD/* all+PROD
for i in `ls ./all+PROD/*.sql | sort -V`; do cat $i >> V1_0__baseline_ss2020_all_and_PROD.sql; done;
```

1. Auf der PROD werden per DB Migration keine Benutzer angelegt.
   
   Das Script
   
   ```
   INSERT INTO benutzer_rolle(
       benutzer_rolle_benutzer_id,
       benutzer_rolle_rolle_id
   )
   VALUES  (2, 9);
   ```
   
   zum Anlegen eines Moderators wurde entfernt, da es den Benutzer mit der ID = 2 nicht gibt.

