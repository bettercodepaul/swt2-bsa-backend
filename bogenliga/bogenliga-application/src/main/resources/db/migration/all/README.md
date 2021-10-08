# Hinweise zur Schemata-Migration

Dieser Ordner enthält SQL-Skripte, die für alle Umgebungen migriert werden sollen.
Dies sind in der Regel Skripte zum Anlegen oder Ändern des Datenbank-Schemas, z.B. `CREATE TABLE` oder `ALTER TABLE`.
Aber auch allgemeingültige Daten können hier eingefügt werden, z.B. Konstanten für die Rollen und Rechte.

## Datei-Schema

`V{Versionsnummer}_0__create_table_{Name der Tabelle und Beschreibung}.sql`

Beispiele:

- `V1_0__create_table_benutzer.sql`
- `V2_0__create_table_rolle.sql`
- `V3_0__create_table_benutzer_rolle.sql`
- `V4_0__create_table_dsb_mitglied.sql`
- `V1_1__alter_table_benutzer_add_column_last_name.sql`
- `V1_2__alter_table_benutzer_remove_column_last_name.sql`
- `V1_3__alter_table_benutzer_alter_column_first_name.sql`
