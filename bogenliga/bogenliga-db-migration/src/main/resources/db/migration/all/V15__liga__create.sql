-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_liga_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE liga (
  liga_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_liga_id'), -- DECIMAL(19,0) = unsigned long
  liga_region_id        DECIMAL(19,0)   NOT NULL, --Fremdschluessel zur Region
  liga_name             VARCHAR(200)    NOT NULL,
  liga_uebergeordnet    DECIMAL(19,0)   NULL,  -- Verweis auf die uebergeordnete Liga - bei Bundesliga (ganz oben) leer


  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_liga_id PRIMARY KEY (liga_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_liga_name UNIQUE (liga_name),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_liga_region FOREIGN KEY (liga_region_id) REFERENCES region (region_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_liga_liga FOREIGN KEY (liga_uebergeordnet) REFERENCES liga (liga_id)
    ON DELETE CASCADE
);