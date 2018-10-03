/*
 * general conventions:
 * - lower case
 * - use "_"
 * - use table name for column prefixes
 *
 * example:
 * table name = "userdata"
 * column prefix = "userdata_"
 */

CREATE TABLE benutzer_login_verlauf (
  benutzer_login_verlauf_benutzer_id    DECIMAL(19, 0) NOT NULL, -- DECIMAL(19,0) = unsigned long
  benutzer_login_verlauf_timestamp      TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  benutzer_login_verlauf_login_ergebnis VARCHAR(50)    NOT NULL,

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_benutzer_login_verlauf UNIQUE (benutzer_login_verlauf_benutzer_id, benutzer_login_verlauf_timestamp),

  CONSTRAINT fk_benutzer_login_verlauf_benutzer_id FOREIGN KEY (benutzer_login_verlauf_benutzer_id) REFERENCES benutzer (benutzer_id)
);