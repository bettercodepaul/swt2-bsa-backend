-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_passe_id START WITH 1000 INCREMENT BY 1
;

ALTER TABLE passe
    ADD COLUMN
        -- an auto-incremented unique attribute, for easier internal access
        passe_id DECIMAL(19, 0) NOT NULL DEFAULT nextval('sq_passe_id'),

    -- make match_id unique
    ADD CONSTRAINT pk_alias_passe_id UNIQUE (passe_id),

    ADD COLUMN passe_match_id DECIMAL(19, 0) DEFAULT 1000,

    ADD CONSTRAINT fk_match_id FOREIGN KEY (passe_match_id) REFERENCES match (match_id)
        ON DELETE CASCADE
;

--remove the default value from the column
ALTER TABLE passe
    ALTER COLUMN passe_match_id
        DROP DEFAULT;