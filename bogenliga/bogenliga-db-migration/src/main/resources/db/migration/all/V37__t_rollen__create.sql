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
CREATE SEQUENCE sq_rollen_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE rollen (
  rollen_id           DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_rollen_id'), -- DECIMAL(19,0) = unsigned long
  rollen_name           VARCHAR(200)  NOT NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_rollen_id  PRIMARY KEY (rollen_id) -- TODO rollen_name auf enum mit Rollen einschränken?
);