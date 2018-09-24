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

CREATE TABLE sessiondaten (
  sessiondaten_benutzerId               DECIMAL(19,0)   NOT NULL, -- DECIMAL(19,0) = unsigned long
  sessiondaten_token          VARCHAR(200)    NULL,
  sessiondaten_timestamp         VARCHAR(200)    NULL,
  sessiondaten_ip            VARCHAR(200)    NOT NULL,

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_sessiondaten_userId UNIQUE (sessiondaten_benutzerId),
  CONSTRAINT uc_sessiondaten_token UNIQUE (sessiondaten_token), -- TODO datentypen/validierung für token, ip etc?
  CONSTRAINT uc_sessiondaten_timestamp UNIQUE (sessiondaten_timestamp),
  CONSTRAINT uc_sessiondaten_ip UNIQUE (sessiondaten_ip),

  CONSTRAINT fk_sessiondaten_benutzerId FOREIGN KEY (sessiondaten_benutzerId) REFERENCES benutzer (benutzer_id)
);