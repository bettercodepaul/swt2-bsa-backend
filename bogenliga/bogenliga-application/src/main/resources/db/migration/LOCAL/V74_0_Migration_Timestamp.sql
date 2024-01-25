CREATE SEQUENCE sq_migrationTimestamp_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE Migrationtimestamp (
    id DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_migrationTimestamp_id'),
    timestamp TIMESTAMP
);