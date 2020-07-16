/**
 * Tabelle zum Abbilden einer n:m-Beziehung
 *
 * Nur ein Kampfrichter kann eine leitende Rolle bei einem Wettkampf einnehmen.
 */
CREATE TABLE kampfrichter (
  kampfrichter_benutzer_id  DECIMAL(19, 0) NOT NULL,
  kampfrichter_wettkampf_id DECIMAL(19, 0) NOT NULL,
  kampfrichter_leitend      BOOLEAN        NOT NULL    DEFAULT FALSE,

  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc            TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by                DECIMAL(19, 0) NOT NULL    DEFAULT 0,
  last_modified_at_utc      TIMESTAMP      NULL        DEFAULT NULL,
  last_modified_by          DECIMAL(19, 0) NULL        DEFAULT NULL,
  version                   DECIMAL(19, 0) NOT NULL    DEFAULT 0,


  CONSTRAINT pk_kampfrichter_wettkampf_leitend PRIMARY KEY (kampfrichter_wettkampf_id, kampfrichter_benutzer_id, kampfrichter_leitend),

    -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_kampfrichter_wettkampf FOREIGN KEY (kampfrichter_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
  ON DELETE CASCADE,

  CONSTRAINT fk_kampfrichter_benutzer FOREIGN KEY (kampfrichter_benutzer_id) REFERENCES benutzer (benutzer_id)
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_kampfrichter_update_version
  BEFORE UPDATE ON kampfrichter
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
