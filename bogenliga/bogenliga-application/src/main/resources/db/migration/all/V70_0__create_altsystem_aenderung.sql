CREATE TABLE altsystem_aenderung_operation (
    operation_id   INT     NOT NULL PRIMARY KEY,
    operation_name VARCHAR NOT NULL
)
;

INSERT INTO altsystem_aenderung_operation(operation_id, operation_name)
VALUES (1, 'CREATE'),
       (2, 'UPDATE')
;


CREATE TABLE altsystem_aenderung_status (
    status_id   INT     NOT NULL PRIMARY KEY,
    status_name VARCHAR NOT NULL
)
;

INSERT INTO altsystem_aenderung_status(status_id, status_name)
VALUES (1, 'NEW'),
       (2, 'IN_PROGRESS'),
       (3, 'ERROR'),
       (4, 'SUCCESS')
;


CREATE SEQUENCE
    IF NOT EXISTS
    altsystem_aenderung_id
    START WITH 1
    INCREMENT BY 1
;

CREATE TABLE altsystem_aenderung (
    aenderung_id   INT       NOT NULL PRIMARY KEY DEFAULT NEXTVAL('altsystem_aenderung_id'),
    kategorie      VARCHAR   NOT NULL,
    altsystem_id   INT       NOT NULL,
    operation      INT       NOT NULL, -- references a key in enum altsystem_aenderung_operation
    status         INT       NOT NULL, -- references a key in enum altsystem_aenderung_status
    nachricht      VARCHAR,

    created_at_utc TIMESTAMP NOT NULL             DEFAULT (NOW() AT TIME ZONE 'utc'),
    run_at_utc     TIMESTAMP
)
;
