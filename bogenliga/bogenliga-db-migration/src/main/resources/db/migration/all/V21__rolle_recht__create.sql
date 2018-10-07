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
CREATE TABLE rolle_recht (
  rolle_recht_rolle_id         DECIMAL(19,0)  NOT NULL,
  rolle_recht_recht_id         DECIMAL(19,0)  NOT NULL,

  CONSTRAINT fk_rolle_recht_rolle FOREIGN KEY (rolle_recht_rolle_id) REFERENCES rolle (rolle_id),
  CONSTRAINT fk_rolle_recht_recht FOREIGN KEY (rolle_recht_recht_id) REFERENCES recht (recht_id)
);