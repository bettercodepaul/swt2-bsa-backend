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
CREATE TABLE benutzer_rollen (
  benutzer_rollen_benutzerId         DECIMAL(19,0)  NOT NULL,
  benutzer_rollen_rollenId           DECIMAL(19,0)   NOT NULL,


  CONSTRAINT fk_benutzer_rollen_benutzerId FOREIGN KEY (benutzer_rollen_benutzerId) REFERENCES benutzer (benutzer_id),
  CONSTRAINT fk_benutzer_rollen_rollenId FOREIGN KEY (benutzer_rollen_rollenId) REFERENCES rollen (rollen_id)
);