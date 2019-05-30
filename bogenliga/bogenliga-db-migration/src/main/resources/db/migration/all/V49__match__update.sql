-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_match_id START WITH 1000 INCREMENT BY 1
;

ALTER TABLE match
    ADD COLUMN
        -- an auto-incremented unique attribute, for easier internal access
        match_id DECIMAL(19, 0) NOT NULL DEFAULT nextval('sq_match_id'),
    -- make match_id unique
    ADD CONSTRAINT pk_alias_match_id UNIQUE (match_id)
;
