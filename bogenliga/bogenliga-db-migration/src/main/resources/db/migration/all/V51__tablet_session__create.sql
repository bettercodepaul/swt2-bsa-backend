/**
 * Eine TabletSession wird an einem Wettkampftag für eine Scheibe gestartet.
 * Die Kombination aus Scheibennr. und Wettkampt ist damit unique.
 **/

CREATE TABLE tablet_session (
    tablet_session_wettkampf_id   DECIMAL(19, 0) NOT NULL,
    tablet_session_scheibennummer DECIMAL(1, 0)  NOT NULL,
    tablet_session_match_id       DECIMAL(19, 0),
    tablet_session_satznr         DECIMAL(1, 0) DEFAULT 1,
    is_active                     BOOLEAN NOT NULL DEFAULT TRUE,

    created_at_utc                TIMESTAMP      NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    created_by                    DECIMAL(19, 0) NOT NULL DEFAULT 0,
    last_modified_at_utc          TIMESTAMP      NULL     DEFAULT NULL,
    last_modified_by              DECIMAL(19, 0) NULL     DEFAULT NULL,
    version                       DECIMAL(19, 0) NOT NULL DEFAULT 0,

    CONSTRAINT pk_tablet_session PRIMARY KEY (tablet_session_wettkampf_id, tablet_session_scheibennummer),

    CONSTRAINT fk_tablet_session_match FOREIGN KEY (tablet_session_match_id) REFERENCES match (match_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_tablet_session_wettkampf FOREIGN KEY (tablet_session_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
        ON DELETE CASCADE
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_tablet_session_update_version
    BEFORE UPDATE
    ON tablet_session
    FOR EACH ROW
EXECUTE PROCEDURE update_row_version();
