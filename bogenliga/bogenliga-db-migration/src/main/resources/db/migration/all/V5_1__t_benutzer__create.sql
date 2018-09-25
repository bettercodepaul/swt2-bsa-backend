/*
 * general conventions:
 * - lower case
 * - use "_"
 * - use table name for column prefixes
 *
 * example:
 * table name = "benutzer"
 * column prefix = "benutzer_"
 */


-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_benutzer_id START WITH 1000 INCREMENT BY 1;

-- Userdaten für die technische Userverwaltung (Authentifizierung, Sessionmanagement, etc)
CREATE TABLE benutzer (
  benutzer_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_benutzer_id'), -- DECIMAL(19,0) = unsigned long
  benutzer_email            VARCHAR(200)    NOT NULL,
  benutzer_salt            VARCHAR(200)    NOT NULL, -- TODO uuid für salt verwenden (in java uuid.generate)
  benutzer_password            VARCHAR(200)    NOT NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_benutzer_id PRIMARY KEY (benutzer_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_benutzer_email UNIQUE (benutzer_email)
);