CREATE SEQUENCE sq_anzeigen_id START WITH 1 INCREMENT BY 1;

create table anzeigen (
                          anzeigen_id                 NUMERIC(19)     NOT NULL     DEFAULT nextval('sq_anzeigen_id'),
                          physische_bildschirm_id     VARCHAR(4)      NOT NULL,
                          table_typ                   VARCHAR(10)     NOT NULL,
                          veranstaltungs_id           NUMERIC(19),

                          CONSTRAINT pk_anzeigen_id PRIMARY KEY (anzeigen_id),

                          CONSTRAINT fk_veranstaltungs_id FOREIGN KEY (veranstaltungs_id) REFERENCES veranstaltung (veranstaltung_id),

                          CONSTRAINT uc_physische_bildschirm_id UNIQUE (physische_bildschirm_id)
);