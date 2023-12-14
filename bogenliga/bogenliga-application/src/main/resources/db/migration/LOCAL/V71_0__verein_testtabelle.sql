CREATE SEQUENCE  IF NOT EXISTS verein_id_test START WITH 1 INCREMENT BY 1;

CREATE TABLE verein_testtabelle(
    verein_id_test int NOT NULL  PRIMARY KEY DEFAULT nextval('verein_id_test'),
    verein_name_test VARCHAR(200),
    verein_dsb_identifier_test VARCHAR(200)
);