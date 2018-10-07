/*
 * general conventions:
 * - lower case
 * - use "_"
 * - use table name for column prefixes
 *
 * example:
 * table name = "user"
 * column prefix = "user_"
 */

-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE TABLE benutzer_rolle (
  benutzer_rolle_benutzer_id        DECIMAL(19,0)  NOT NULL,
  benutzer_rolle_rolle_id           DECIMAL(19,0)  NOT NULL,


  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
  last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
  last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
  version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,


  CONSTRAINT fk_benutzer_rolle_benutzer FOREIGN KEY (benutzer_rolle_benutzer_id) REFERENCES benutzer (benutzer_id),
  CONSTRAINT fk_benutzer_rolle_rolle FOREIGN KEY (benutzer_rolle_rolle_id) REFERENCES rolle (rolle_id)
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_benutzer_rolle_update_version
  BEFORE UPDATE ON benutzer_rolle
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
