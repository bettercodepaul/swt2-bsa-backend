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


  CONSTRAINT fk_benutzer_rolle_benutzer FOREIGN KEY (benutzer_rolle_benutzer_id) REFERENCES benutzer (benutzer_id),
  CONSTRAINT fk_benutzer_rolle_rolle FOREIGN KEY (benutzer_rolle_rolle_id) REFERENCES rolle (rolle_id)
);