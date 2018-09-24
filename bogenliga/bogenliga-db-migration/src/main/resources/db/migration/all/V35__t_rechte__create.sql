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
CREATE SEQUENCE sq_rechte_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE rechte (
  rechte_id           DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_rechte_id'), -- DECIMAL(19,0) = unsigned long
  rechte_name           VARCHAR(200)  NOT NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_rechte_id  PRIMARY KEY (rechte_id),

  CONSTRAINT uc_rechte_name UNIQUE (rechte_name)
);