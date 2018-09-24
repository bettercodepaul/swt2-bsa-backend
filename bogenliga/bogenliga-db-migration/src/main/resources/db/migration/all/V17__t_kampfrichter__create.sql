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
CREATE SEQUENCE sq_kampfrichter_id START WITH 1000 INCREMENT BY 1;

/**
 * Ein Kampfrichter enthält die notwendigen personenbezogenen Daten.
 **/
CREATE TABLE kampfrichter (
  kampfrichter_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_kampfrichter_id'), -- DECIMAL(19,0) = unsigned long
  kampfrichter_anrede          VARCHAR(200)    NOT NULL,
  kampfrichter_vorname          VARCHAR(200)    NOT NULL,
  kampfrichter_nachname         VARCHAR(200)    NOT NULL,
  kampfrichter_straße        VARCHAR(200),
  kampfrichter_ort        VARCHAR(200),
  kampfrichter_telefon        VARCHAR(200), -- TODO Format
  kampfrichter_fax       VARCHAR(200), -- TODO Format wie bei Telefon
  kampfrichter_email            VARCHAR(200)    NULL,
  kampfrichter_mobil           VARCHAR(200)    NULL, -- TODO Format wie bei Telefon
  kampfrichter_land           VARCHAR(200)    NULL,
  kampfrichter_geburtsdatum           VARCHAR(200)    NULL, -- TODO Format
  kampfrichter_lzi_nr               DECIMAL(19,0)     NULL, -- TODO Unique?

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_kampfrichter_id PRIMARY KEY (kampfrichter_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_kampfrichter_email UNIQUE (kampfrichter_email)

);