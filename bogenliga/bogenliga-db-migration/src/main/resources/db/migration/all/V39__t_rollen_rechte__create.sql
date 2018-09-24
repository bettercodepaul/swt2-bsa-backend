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
CREATE TABLE rollen_rechte (
  rollen_rechte_rollenId           DECIMAL(19,0)   NOT NULL,
  rollen_rechte_rechteId         DECIMAL(19,0)  NOT NULL,

  CONSTRAINT fk_rollen_rechte_rollenId FOREIGN KEY (rollen_rechte_rollenId) REFERENCES rollen (rollen_id),
  CONSTRAINT fk_rollen_rechte_rechteId FOREIGN KEY (rollen_rechte_rechteId) REFERENCES rechte (rechte_id)
);