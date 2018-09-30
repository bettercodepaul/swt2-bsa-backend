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

CREATE TABLE session (
  session_benutzer_id   DECIMAL(19,0)   NOT NULL, -- DECIMAL(19,0) = unsigned long
  session_token         VARCHAR(200)    NULL,
  session_timestamp     VARCHAR(200)    NULL,
  session_ip            VARCHAR(200)    NOT NULL,

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_session_user UNIQUE (session_benutzer_id),
  CONSTRAINT uc_session_token UNIQUE (session_token), -- TODO datentypen/validierung für token, ip etc?

  CONSTRAINT fk_session_benutzer FOREIGN KEY (session_benutzer_id) REFERENCES benutzer (benutzer_id)
);