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
-- foreign key (fk)
-- schema: fk_{current table name}_{foreign key origin table name}


CREATE OR REPLACE FUNCTION update_row_version()
  returns TRIGGER AS $update_row_version_trigger$
BEGIN
  new.version = old.version + 1;
  return new;
END
$update_row_version_trigger$
LANGUAGE plpgsql;


/**
 * Technische Entität zum Speichern von Konfigurationsparameter
 * und Defaults
 **/

CREATE TABLE configuration (
  configuration_key   VARCHAR(200) NOT NULL,
  configuration_value TEXT         NOT NULL,
  configuration_hidden BOOLEAN DEFAULT false,
  configuration_regex  VARCHAR(600),
  CONSTRAINT pk_configuration_key PRIMARY KEY (configuration_key)
);

/**
 * Eine Region dient der räumlichen Einteilung des Deutscher Schützenbundes (DSB).
 * Regionen sind hierarisch in einer Baumstruktur eingeordnet.
 * Der DSB bildet den Root-Knoten.
 **/

-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_region_id START WITH 2000 INCREMENT BY 1;
CREATE TABLE region (
  region_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_region_id'), -- DECIMAL(19,0) = unsigned long
  region_name           VARCHAR(200)  NOT NULL,
  region_kuerzel        VARCHAR(10)   NOT NULL,
  region_typ            VARCHAR(200)  NOT NULL,
  region_uebergeordnet  DECIMAL(19,0) NULL,  -- Verweis auf die uebergeordnete Region - bei DSB (ganz oben) Bezug leer
  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
  last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
  last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
  version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,
  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_region_id PRIMARY KEY (region_id),
  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_region_name UNIQUE (region_name),
  CONSTRAINT uc_region_kuerzel UNIQUE (region_kuerzel),
  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_region_region FOREIGN KEY (region_uebergeordnet) REFERENCES region (region_id)
    ON DELETE SET NULL -- das Löschen einer Region würde alle untergeordneten Regionen, Vereine und Schützen entfernen
);
-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_region_update_version
  BEFORE UPDATE ON region
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();

-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_verein_id START WITH 1200 INCREMENT BY 1;

/**
 * Ein Verein besitzt einen Namen und eine ID beim DSB.
 * Ein Verein ist genau einer Region zugeordnet.
 * Es muss sichergestellt werden, dass der Verein mit einer Region auf Kreis-Ebene verknüpft ist.
 **/
CREATE TABLE verein (
  verein_id             DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_verein_id'), -- DECIMAL(19,0) = unsigned long
  verein_name           VARCHAR(200)    NOT NULL,
  verein_dsb_identifier VARCHAR(200)    NOT NULL, -- fachlicher Schluessel: Identifikator beim DBS
  verein_region_id      DECIMAL(19,0)   NOT NULL, -- Fremdschluessel zur Regionszuordnung auf Ebene "Kreis"
  -- include script V3_0
  verein_website        VARCHAR(200), -- html Reference der Homepage des Vereins
  verein_description    VARCHAR(300), -- Vereinsbeschreibung
  verein_icon           VARCHAR(10485760), -- reference zum Vereinslogo
  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
  last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
  last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
  version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_verein_id PRIMARY KEY (verein_id),

  -- unique constraint (uc)
  -- scheme: uc_{column name}
  CONSTRAINT uc_verein_name UNIQUE (verein_name),

  CONSTRAINT uc_verein_dsb_identifier UNIQUE (verein_dsb_identifier),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_verein_region FOREIGN KEY (verein_region_id) REFERENCES region (region_id)
    ON DELETE SET NULL -- das Loeschen einer Region wuerde alle Vereine und deren dsb_mitgliedn entfernen
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_verein_update_version
  BEFORE UPDATE ON verein
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
/*
 * general conventions:
 * - lower case
 * - use "_"
 * - use table name for column prefixes
 *
 * example:
 * table name = "benutzer"
 * column prefix = "benutzer_"
 */




/**
 * Ein Schütze enthält die notwendigen personenbezogenen Daten.
 * Ein Schütze ist immer einem Verein zugewiesen.
 **/

-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_dsb_mitglied_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE dsb_mitglied (
   dsb_mitglied_id              DECIMAL(19, 0) NOT NULL    DEFAULT nextval('sq_dsb_mitglied_id'),
   dsb_mitglied_vorname         VARCHAR(200)   NOT NULL,
   dsb_mitglied_nachname        VARCHAR(200)   NOT NULL,
   dsb_mitglied_geburtsdatum    DATE           NOT NULL,
   dsb_mitglied_nationalitaet   VARCHAR(5)     NOT NULL, -- TODO Format in User Store gem. ISO festelegen und prüfen
   dsb_mitglied_mitgliedsnummer VARCHAR(200)   NOT NULL,
   dsb_mitglied_verein_id       DECIMAL(19, 0) NOT NULL, --Fremdschluessel zum Verein
   dsb_mitglied_benutzer_id     DECIMAL(19, 0) NULL,
    -- technical columns to track the lifecycle of each row
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
   created_at_utc               TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
   created_by                   DECIMAL(19, 0) NOT NULL    DEFAULT 0,
   last_modified_at_utc         TIMESTAMP      NULL,
   last_modified_by             DECIMAL(19, 0) NULL,
   version                      DECIMAL(19, 0) NOT NULL    DEFAULT 0,
    -- primary key (pk)
    -- scheme: pk_{column name}
   CONSTRAINT pk_dsb_mitglied_id PRIMARY KEY (dsb_mitglied_id),
    -- unique constraint (uc)
    -- scheme: uc_{column name}
   CONSTRAINT uc_dsb_mitglied_mitgliedsnummer UNIQUE (dsb_mitglied_mitgliedsnummer),
    -- foreign key (fk)
    -- schema: fk_{current table name}_{foreign key origin table name}
   CONSTRAINT fk_dsb_mitglied_verein FOREIGN KEY (dsb_mitglied_verein_id) REFERENCES verein (verein_id)
      ON DELETE CASCADE -- das Löschen eines Vereins löscht auch dessen Mitglieder

);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_dsb_mitglied_update_version
    BEFORE UPDATE ON dsb_mitglied
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();



-- Userdaten für die technische Userverwaltung (Authentifizierung, Sessionmanagement, etc)
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_benutzer_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE benutzer (
                          benutzer_id                   DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_benutzer_id'), -- DECIMAL(19,0) = unsigned long
                          benutzer_email                VARCHAR(200),  -- will be required for active users - see unique index
                          benutzer_salt                 VARCHAR(200)    NOT NULL, -- TODO uuid für salt verwenden (in java uuid.generate)
                          benutzer_password             VARCHAR(200)    NOT NULL,
                          benutzer_using_2fa            BOOLEAN   NOT NULL DEFAULT FALSE,
                          benutzer_secret               VARCHAR(200) NULL,
                          benutzer_active               BOOLEAN          NOT NULL    DEFAULT TRUE,
                          benutzer_dsb_mitglied_id      DECIMAL(19,0) DEFAULT 0,
    -- technical columns to track the lifecycle of each row
    -- the "_by" columns references a "benutzer_id" without foreign key constraint
    -- the "_at_utc" columns using the timestamp with the UTC timezone
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
                          created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
                          created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
                          last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
                          last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
                          version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,
    -- primary key (pk)
    -- scheme: pk_{column name}
                          CONSTRAINT pk_benutzer_id PRIMARY KEY (benutzer_id),
    -- unique constraint (uc)
    -- scheme: uc_{column name}
                          CONSTRAINT uc_benutzer_email UNIQUE (benutzer_email),
                          CONSTRAINT fk_benutzer_dsb_mitglied_id FOREIGN KEY (benutzer_dsb_mitglied_id)
                              REFERENCES dsb_mitglied (dsb_mitglied_id)
);
CREATE UNIQUE INDEX uc_benutzer_email_active ON benutzer (benutzer_email) WHERE (benutzer_active = TRUE);
-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_benutzer_update_version
    BEFORE UPDATE ON benutzer
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();-- auto increment sequence (sq)


/**
 * Eine Klasse dient der Defintion von Gruppen gleichen Alters
 * Relevant ist der Jahrgang und das daraus resultierende Alter des Schützen.
 **/

-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_klasse_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE klasse (
                        klasse_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_klasse_id'), -- DECIMAL(19,0) = unsigned long
                        klasse_name           VARCHAR(200)  NOT NULL, -- gem. Vorgaben DSB
                        klasse_alter_min      DECIMAL(2)  NOT NULL,
                        klasse_alter_max      DECIMAL(3)  NOT NULL,
                        klasse_nr             DECIMAL(2,0)  NOT NULL, -- Nummer der Klasse im DSB
    -- technical columns to track the lifecycle of each row
    -- the "_by" columns references a "benutzer_id" without foreign key constraint
    -- the "_at_utc" columns using the timestamp with the UTC timezone
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
                        created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
                        created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
                        last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
                        last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
                        version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,
    -- primary key (pk)
    -- scheme: pk_{column name}
                        CONSTRAINT pk_klasse_id PRIMARY KEY (klasse_id),
    -- unique constraint (uc)
    -- scheme: uc_{column name}
                        CONSTRAINT uc_klasse_name UNIQUE (klasse_name),
                        CONSTRAINT uc_klasse_nr UNIQUE (klasse_nr)
);
-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_klasse_update_version
    BEFORE UPDATE ON klasse
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();


/**
 * Eine Disziplin dient der Defintion von Bogentypen für die die gleichen Anforderung gelten.
 **/
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_disziplin_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE disziplin (
                           disziplin_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_disziplin_id'), -- DECIMAL(19,0) = unsigned long
                           disziplin_name           VARCHAR(200)  NOT NULL, -- gem. Vorgaben DSB
    -- technical columns to track the lifecycle of each row
    -- the "_by" columns references a "benutzer_id" without foreign key constraint
    -- the "_at_utc" columns using the timestamp with the UTC timezone
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
                           created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
                           created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
                           last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
                           last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
                           version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,
    -- primary key (pk)
    -- scheme: pk_{column name}
                           CONSTRAINT pk_disziplin_id PRIMARY KEY (disziplin_id),
    -- unique constraint (uc)
    -- scheme: uc_{column name}
                           CONSTRAINT uc_disziplin_name UNIQUE (disziplin_name)
);
-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_disziplin_update_version
    BEFORE UPDATE ON disziplin
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();


/**
 * Defintion von Wettkampftypen - Wettkämpfe mit identischen Regeln
 **/
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_wettkampftyp_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE wettkampftyp (
                              wettkampftyp_id             DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_wettkampftyp_id'), -- DECIMAL(19,0) = unsigned long
                              wettkampftyp_name           VARCHAR(200)  NOT NULL, -- gem. Vorgaben DSB
    -- technical columns to track the lifecycle of each row
    -- the "_by" columns references a "benutzer_id" without foreign key constraint
    -- the "_at_utc" columns using the timestamp with the UTC timezone
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
                              created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
                              created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
                              last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
                              last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
                              version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,
    -- primary key (pk)
    -- scheme: pk_{column name}
                              CONSTRAINT pk_wettkampftyp_id PRIMARY KEY (wettkampftyp_id),
    -- unique constraint (uc)
    -- scheme: uc_{column name}
                              CONSTRAINT uc_wettkampftyp_name UNIQUE (wettkampftyp_name)
);
-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_wettkampftyp_update_version
    BEFORE UPDATE ON wettkampftyp
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();



/**
 * Defintion von Liga - eine Hierarchische Anordnung von abstrakten Ligen, die die Reihenfolge zum
 * Auf- und ABsteig definieren, sowie jahresübergreifend den Verantwortlichen festlegt
 **/

-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_liga_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE liga (
   liga_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_liga_id'), -- DECIMAL(19,0) = unsigned long
   liga_region_id        DECIMAL(19,0)   NOT NULL, --Fremdschluessel zur Region
   liga_name             VARCHAR(200)    NOT NULL,
   liga_uebergeordnet    DECIMAL(19,0)   NULL,  -- Verweis auf die uebergeordnete Liga - bei Bundesliga (ganz oben) leer
   liga_verantwortlich   DECIMAL(19,0)   NULL,  -- Verweis auf den Verantwortlichen User für die Liga
   liga_disziplin_id     DECIMAL(19,0) NOT NULL,  -- technical columns to track the lifecycle of each row
   liga_detail           VARCHAR(5000)  NULL,
   liga_file_base64 varchar(10485760) DEFAULT '',
   liga_file_name varchar (200) DEFAULT '',
   liga_file_type varchar (202) DEFAULT '',
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
   created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
   created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
   last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
   last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
   version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,
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
         ON DELETE CASCADE,
   CONSTRAINT fk_liga_benutzer FOREIGN KEY (liga_verantwortlich) REFERENCES benutzer (benutzer_id),
   CONSTRAINT fk_liga_disziplin FOREIGN KEY (liga_disziplin_id) REFERENCES disziplin(disziplin_id)
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_liga_update_version
    BEFORE UPDATE ON liga
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();


/**
 * Einen Lizenz kann verschieden Berechtigungen fachlicher Art für den Schützen darstellen
 * wir unterscheiden aktuell zischenn der Kapmfprichter Lizenz
 * --> Berechtigung zur Übernahme der Rolle Kapmfrichter für einen Wetttkamp
 * und der Liga Lizenz --> Berechtigung zur Teilnahme an Liga Wettkämpfen einer Disziplin.
 **/
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_lizenz_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE lizenz (
                        lizenz_id               DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_lizenz_id'), -- DECIMAL(19,0) = unsigned long
                        lizenz_nummer           VARCHAR(200)  NOT NULL, --fachliche Lizenz-Nummer für Anzeige und Druck
                        lizenz_region_id        DECIMAL(19,0) NOT NULL , --Lizenz ausgebender Landeverband oder DSB
                        lizenz_dsb_mitglied_id  DECIMAL(19,0) NOT NULL, --Fremdschluessel zum dsb_mitgliedn
                        lizenz_typ              VARCHAR(200)  NOT NULL, -- aktuell nur Liga oder Kampfrichter
                        lizenz_disziplin_id     DECIMAL(19,0) NULL, --Fremdschluessel zur Disziplin, nur für Liga
    -- technical columns to track the lifecycle of each row
    -- the "_by" columns references a "benutzer_id" without foreign key constraint
    -- the "_at_utc" columns using the timestamp with the UTC timezone
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
                        created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
                        created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
                        last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
                        last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
                        version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,
    -- primary key (pk)
    -- scheme: pk_{column name}
                        CONSTRAINT pk_lizenz_id PRIMARY KEY (lizenz_id),
    -- fachlicher constraint: Lizenz eindeutig im Verband, d.h. region_ID + lizenznummer --> unique
                        CONSTRAINT fk_lizenz_dsb_mitglied FOREIGN KEY (lizenz_dsb_mitglied_id) REFERENCES dsb_mitglied (dsb_mitglied_id)
                            ON DELETE CASCADE, -- das Löschen eines dsb_mitgliedn löscht auch dessen Lizenzen

    -- foreign key (fk)
    -- schema: fk_{current table name}_{foreign key origin table name}
                        CONSTRAINT fk_lizenz_disziplin FOREIGN KEY (lizenz_disziplin_id) REFERENCES disziplin (disziplin_id)
                            ON DELETE CASCADE, -- das Löschen einer Disziplin löscht auch die Lizenzen

    -- foreign key (fk)
    -- schema: fk_{current table name}_{foreign key origin table name}
                        CONSTRAINT fk_lizenz_region FOREIGN KEY (lizenz_region_id) REFERENCES region (region_id)
                            ON DELETE CASCADE -- das Löschen einer Region löscht auch die Lizenzen
);


-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_lizenz_update_version
    BEFORE UPDATE ON lizenz
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();


/**
 * Eine Veranstaltung dient der Defintion einer Liga in einem Sportjahr
 **/
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_veranstaltung_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE veranstaltung (
    veranstaltung_id                  DECIMAL(19, 0) NOT NULL    DEFAULT nextval('sq_veranstaltung_id'),
    veranstaltung_wettkampftyp_id     DECIMAL(19, 0) NOT NULL, --bezug zu den Regeln
    veranstaltung_name                VARCHAR(200)   NOT NULL,
    veranstaltung_sportjahr           DECIMAL(4, 0)  NOT NULL, --Okt 2018 bis Okt 2019 wird als 2019 erfasst
    veranstaltung_meldedeadline       DATE           NOT NULL, --Termin zu dem die Anmeldungen abgeschlossen wird
    veranstaltung_ligaleiter_id       DECIMAL(19, 0) NOT NULL, -- Benutzer-id des Koordinators aller Wettkämpfe des Sportjahres
    veranstaltung_liga_id             DECIMAL(19,0) NOT NULL,
    veranstaltung_phase               DECIMAL(4,0)   default 1,  -- technical columns to track the lifecycle of each row
    veranstaltung_groesse INT DEFAULT 8 CHECK ( veranstaltung_groesse = 4 OR veranstaltung_groesse = 6 OR veranstaltung_groesse = 8 ), -- the "_by" columns references a "benutzer_id" without foreign key constraint
    -- the "_at_utc" columns using the timestamp with the UTC timezone
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
    created_at_utc                    TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
    created_by                        DECIMAL(19, 0) NOT NULL    DEFAULT 0,
    last_modified_at_utc              TIMESTAMP      NULL        DEFAULT NULL,
    last_modified_by                  DECIMAL(19, 0) NULL        DEFAULT NULL,
    version                           DECIMAL(19, 0) NOT NULL    DEFAULT 0,
    -- primary key (pk)
    -- scheme: pk_{column name}
    CONSTRAINT pk_veranstaltung_id PRIMARY KEY (veranstaltung_id),
    -- foreign key (fk)
    -- schema: fk_{current table name}_{foreign key origin table name}
    CONSTRAINT fk_veranstaltung_wettkampftyp FOREIGN KEY (veranstaltung_wettkampftyp_id) REFERENCES wettkampftyp (wettkampftyp_id)
        ON DELETE CASCADE, -- das Löschen eines wettkampftyps löscht auch die zugehörigen veranstaltungen

    CONSTRAINT fk_veranstaltung_liga_id FOREIGN KEY (veranstaltung_liga_id) REFERENCES liga (liga_id)
        ON DELETE CASCADE, -- on deleting an entry in liga, also deletes all dependent veranstaltung
    CONSTRAINT veranstaltung_name UNIQUE (veranstaltung_sportjahr, veranstaltung_wettkampftyp_id, veranstaltung_liga_id),
    CONSTRAINT fk_veranstaltung_benutzer FOREIGN KEY (veranstaltung_ligaleiter_id) REFERENCES benutzer (benutzer_id)

);
-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_veranstaltung_update_version
    BEFORE UPDATE ON veranstaltung
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();



/**
 * Ein Wettkampf ist ein Veranstaltungstag in einer Liga.
 * mit ihm werden die Verantwortlichen vor, der Veranstaltungsort und die Zeiten festgelegt
 * es gibt je Veranstaltung mehrere Wettkampftag, deren Informationen auf eineander aufbauen
 * die Ergebnisse werdne in Referenz zu einem Wettkmapftag abgelegt
 **/


-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_wettkampf_id START WITH 1200 INCREMENT BY 1;

CREATE TABLE wettkampf (
                           wettkampf_id                  DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_wettkampf_id'), -- DECIMAL(19,0) = unsigned long
                           wettkampf_veranstaltung_id    DECIMAL(19,0) NOT NULL, --bezug zum Sportjahr
                           wettkampf_datum               DATE NOT NULL, -- Termin der Durchführung
                           wettkampf_beginn              VARCHAR(5) NOT NULL, -- Uhrzeit im Format hh:mm
                           wettkampf_tag                 DECIMAL(1,0) NOT NULL, -- Liga hat 4 Wettkampftage, sonst 1
                           wettkampf_disziplin_id        DECIMAL(19,0) NOT NULL,
                           wettkampf_wettkampftyp_id     DECIMAL(19,0) NOT NULL,

                           created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
                           created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
                           last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
                           last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
                           version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,

                           wettkampfausrichter           DECIMAL (19,0), -- Benutzer-id des Ausrichters soll bei Gelegenheit umbenannt werden (_id fehlt)
                           wettkampf_strasse             VARCHAR (30),     -- The street for the "Wettkampf Tag" will be stored in this column
                           wettkampf_plz                 VARCHAR (10),         -- The plz for the "Wettkampf Tag" will be stored in this column
                           wettkampf_ortsname            VARCHAR (25),    -- The city name for the "Wettkampf Tag" will be stored in this column
                           wettkampf_ortsinfo            VARCHAR (200),   -- Some additional adress information for the "Wettkampf Tag" will be stored in this column
                           offlinetoken                  VARCHAR (200),

  -- technical columns to track the lifecycle of each row


  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_wettkampf_id PRIMARY KEY (wettkampf_id),


   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_wettkampf_wettkampftyp FOREIGN KEY (wettkampf_wettkampftyp_id ) REFERENCES wettkampftyp (wettkampftyp_id)
        ON DELETE CASCADE, -- das Löschen eines wettkampftyps löscht auch die zugehörigen wettkämpfe

           -- foreign key (fk)
           -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_wettkampf_veranstaltung_id FOREIGN KEY (wettkampf_veranstaltung_id ) REFERENCES veranstaltung (veranstaltung_id)
        ON DELETE CASCADE, -- das Löschen eines wettkampftyps löscht auch die zugehörigen wettkämpfe

  CONSTRAINT fk_Wettkampf_Ausrichter FOREIGN KEY (WettkampfAusrichter) REFERENCES benutzer(benutzer_id),

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_wettkampf_disziplin FOREIGN KEY (wettkampf_disziplin_id) REFERENCES disziplin (disziplin_id)
        ON DELETE CASCADE -- das Löschen einer Disziplin löscht auch die Lizenzen
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_wettkampf_update_version
    BEFORE UPDATE ON wettkampf
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_mannschaft_id START WITH 1200 INCREMENT BY 1;

/**
 * Einen Mannschaft legt die gemeldeten Schützen für eine Liga oder einen Wettkampf fest
 * aktuell werden die Anzahl der Schützen nicht limitiert
 * Mannschaften bestehen i.d.R. aus Vereinsname und Nummer
 * die Nummer wird durch die Rangfolge der Ligen definiert, in der höchsten Liga ist die Mannschaft 1
 **/

CREATE TABLE mannschaft (
   mannschaft_id               DECIMAL(19, 0) NOT NULL    DEFAULT nextval('sq_mannschaft_id'),
   mannschaft_verein_id        DECIMAL(19, 0) NOT NULL,
   mannschaft_nummer           DECIMAL(2, 0)  NOT NULL,
   mannschaft_benutzer_id      DECIMAL(19, 0) NOT NULL, -- der Ansprechpartner, Meldende der Mannschaft
   mannschaft_veranstaltung_id DECIMAL(19, 0), -- Anlegen der Mannschaft auch ohne Zuordnung zu einer Liga
   mannschaft_sortierung       DECIMAL(2, 0) NOT NULL DEFAULT 0, -- dient zur initialen Sortierung einer Ligatabelle
    -- technical columns to track the lifecycle of each row
  created_at_utc              TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by                  DECIMAL(19, 0) NOT NULL    DEFAULT 0,
  last_modified_at_utc        TIMESTAMP      NULL        DEFAULT NULL,
  last_modified_by            DECIMAL(19, 0) NULL        DEFAULT NULL,
  version                     DECIMAL(19, 0) NOT NULL    DEFAULT 0,
    -- primary key (pk)
    -- scheme: pk_{column name}
  CONSTRAINT pk_mannschaft_id PRIMARY KEY (mannschaft_id),
    -- foreign key (fk)
    -- schema: fk_{current table name}_{foreign key origin table name}
   CONSTRAINT fk_mannschaft_verein FOREIGN KEY (mannschaft_verein_id) REFERENCES verein (verein_id)
       ON DELETE CASCADE, -- das Löschen eines Vereins löscht auch die Mannschaften des Vereins
   CONSTRAINT fk_mannschaft_veranstaltung FOREIGN KEY (mannschaft_veranstaltung_id) REFERENCES veranstaltung (veranstaltung_id)
       ON DELETE CASCADE, -- das Löschen einer Veranstaltung löscht auch die dafür gemeldeten Mannschaften des Vereins

   CONSTRAINT fk_mannschaft_benutzer FOREIGN KEY (mannschaft_benutzer_id) REFERENCES benutzer (benutzer_id)
       ON DELETE SET NULL -- das Löschen eines Benutzers entfernt den Verweis
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_mannschaft_update_version
    BEFORE UPDATE ON mannschaft
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();



/**
 * Ein Match dient der Definition von Runden im Wettkampf -
 * es hat als wesentlichen Merkmal nur eine laufende Nummer für die Runde
 *
 * Der Aspekt Finalrunden ist hier noch nicht abgebildet, ebenso wenig das Stechen
 * Strafpunkte beeinflussen das Ergebnis eines Matches ohne die Pfeilwerte eines Schützen (Entität Passe)
 * zu verändern. Daher werden diese hier abgelegt.
 **/
CREATE SEQUENCE sq_match_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE match (
    -- composite key aus mehreren Fremd- und fachlichen Schlüsseln
   match_id DECIMAL(19, 0) NOT NULL DEFAULT nextval('sq_match_id'),
   match_wettkampf_id   DECIMAL(19, 0) NOT NULL, --Fremdschlüsselbezug zum Wettkampf
   match_nr             DECIMAL(1, 0)  NOT NULL,
   match_begegnung      DECIMAL(1, 0)  NOT NULL,
   match_mannschaft_id  DECIMAL(19, 0) NULL, --Fremdschlüsselbezug zum Mannschaft
   match_scheibennummer DECIMAL(2, 0)  NOT NULL,
   match_matchpunkte    DECIMAL(1, 0)  NULL,
   match_satzpunkte     DECIMAL(1, 0)  NULL,
   match_strafpunkte_satz_1 DECIMAL(2, 0) NULL,
   match_strafpunkte_satz_2 DECIMAL(2, 0) NULL,
   match_strafpunkte_satz_3 DECIMAL(2, 0) NULL,
   match_strafpunkte_satz_4 DECIMAL(2, 0) NULL,
   match_strafpunkte_satz_5 DECIMAL(2, 0) NULL,
    -- technical columns to track the lifecycle of each row
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
   created_at_utc       TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
   created_by           DECIMAL(19, 0) NOT NULL    DEFAULT 0,
   last_modified_at_utc TIMESTAMP      NULL        DEFAULT NULL,
   last_modified_by     DECIMAL(19, 0) NULL        DEFAULT NULL,
   version              DECIMAL(19, 0) NOT NULL    DEFAULT 0,
    -- primary key (pk)
    -- scheme: pk_{column name}
   CONSTRAINT pk_alias_match_id UNIQUE (match_id),

   CONSTRAINT pk_match PRIMARY KEY (match_wettkampf_id, match_nr, match_begegnung, match_scheibennummer, match_mannschaft_id),
    -- foreign key (fk)
    -- schema: fk_{current table name}_{foreign key origin table name}

   CONSTRAINT fk_match_wettkampf FOREIGN KEY (match_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
       ON DELETE CASCADE, -- das Löschen eines wettkampfs löscht auch die zugehörigen matches

   CONSTRAINT fk_match_mannschaft FOREIGN KEY (match_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
       ON DELETE CASCADE -- das Löschen einer mannschaft löscht auch die zugehörigen matches
);
-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_match_update_version
    BEFORE UPDATE ON match
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();




/**
 * Eine Passe dient der Erfassung der Ergebnisse im Wettkampf
 * wir erwarten entweder 2, 3 oder 6 Pfeilwerte für ein dsb_mitglied
 * Die Tabelle ist daher dünn besetzt.
 *
 * Die Passe ist eine schwache Entität und basiert auf dem Wettkampf, dem Match und dem Mannschaftsmitglied.
 * Die Spalten sind denormalisiert, um den Zugriff zu beschleunigen (weniger Joins)
 **/

-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_passe_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE passe (

    passe_id DECIMAL(19, 0) NOT NULL DEFAULT nextval('sq_passe_id'),
    passe_match_id DECIMAL(19, 0) NULL,
    passe_mannschaft_id   DECIMAL(19, 0) NULL,     --Fremdschlüsselbezug zum Mannschaft
    passe_wettkampf_id    DECIMAL(19, 0) NOT NULL, --Fremdschlüsselbezug zum wettkampf
    passe_match_nr        DECIMAL(1, 0)  NOT NULL,
    passe_lfdnr           DECIMAL(4, 0)  NOT NULL,
    passe_dsb_mitglied_id DECIMAL(19, 0) NOT NULL, --Fremdschlüsselbezug zum dsb_mitglied

    passe_ringzahl_pfeil1 DECIMAL(2, 0),           -- die geschossenen Ringe. Werte: [0, 10]
    passe_ringzahl_pfeil2 DECIMAL(2, 0),           -- die geschossenen Ringe. Werte: [0, 10]
    passe_ringzahl_pfeil3 DECIMAL(2, 0),           -- die geschossenen Ringe. Werte: [0, 10]
    passe_ringzahl_pfeil4 DECIMAL(2, 0),           -- die geschossenen Ringe. Werte: [0, 10]
    passe_ringzahl_pfeil5 DECIMAL(2, 0),           -- die geschossenen Ringe. Werte: [0, 10]
    passe_ringzahl_pfeil6 DECIMAL(2, 0),           -- die geschossenen Ringe. Werte: [0, 10]
    -- technical columns to track the lifecycle of each row
    -- the "_by" columns references a "benutzer_id" without foreign key constraint
    -- the "_at_utc" columns using the timestamp with the UTC timezone
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
    created_at_utc        TIMESTAMP      NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    created_by            DECIMAL(19, 0) NOT NULL DEFAULT 0,
    last_modified_at_utc  TIMESTAMP      NULL     DEFAULT NULL,
    last_modified_by      DECIMAL(19, 0) NULL     DEFAULT NULL,
    version               DECIMAL(19, 0) NOT NULL DEFAULT 0,

    -- primary key (pk)
    -- scheme: pk_{column name}
    CONSTRAINT pk_alias_passe_id UNIQUE (passe_id),

    CONSTRAINT fk_match_id FOREIGN KEY (passe_match_id) REFERENCES match (match_id)
        ON DELETE CASCADE,

    CONSTRAINT pk_passe PRIMARY KEY (passe_wettkampf_id, passe_match_nr, passe_mannschaft_id, passe_lfdnr,
        passe_dsb_mitglied_id),

    -- foreign key (fk)
    -- schema: fk_{current table name}_{foreign key origin table name}
    CONSTRAINT fk_passe_wettkampf FOREIGN KEY (passe_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
        ON DELETE CASCADE,                         -- das Löschen eines Wettkampfs löscht auch die zugehörigen Passen

    CONSTRAINT fk_passe_dsb_mitglied FOREIGN KEY (passe_dsb_mitglied_id) REFERENCES dsb_mitglied (dsb_mitglied_id)
        ON DELETE CASCADE,                         -- das Löschen eines dsb_mitglieds löscht auch dessen Pfeilwerte

    CONSTRAINT fk_passe_mannschaft FOREIGN KEY (passe_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
        ON DELETE CASCADE                          -- das Löschen einer mannschaft löscht auch die zugehörigen Passen
)
;

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_passe_update_version
    BEFORE UPDATE
    ON passe
    FOR EACH ROW
EXECUTE PROCEDURE update_row_version();


/**
 * Tabelle zum Abbilden einer n:m-Beziehung
 *
 * Nur ein Kampfrichter kann eine leitende Rolle bei einem Wettkampf einnehmen.
 */
CREATE TABLE kampfrichter (
   kampfrichter_benutzer_id  DECIMAL(19, 0) NOT NULL,
   kampfrichter_wettkampf_id DECIMAL(19, 0) NOT NULL,
   kampfrichter_leitend      BOOLEAN        NOT NULL    DEFAULT FALSE,
    -- technical columns to track the lifecycle of each row
   created_at_utc            TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
   created_by                DECIMAL(19, 0) NOT NULL    DEFAULT 0,
   last_modified_at_utc      TIMESTAMP      NULL        DEFAULT NULL,
   last_modified_by          DECIMAL(19, 0) NULL        DEFAULT NULL,
   version                   DECIMAL(19, 0) NOT NULL    DEFAULT 0,

   CONSTRAINT pk_kampfrichter_wettkampf_leitend PRIMARY KEY (kampfrichter_wettkampf_id, kampfrichter_benutzer_id, kampfrichter_leitend),
    -- foreign key (fk)
    -- schema: fk_{current table name}_{foreign key origin table name}
   CONSTRAINT fk_kampfrichter_wettkampf FOREIGN KEY (kampfrichter_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
       ON DELETE CASCADE,

   CONSTRAINT fk_kampfrichter_benutzer FOREIGN KEY (kampfrichter_benutzer_id) REFERENCES benutzer (benutzer_id)
       ON DELETE CASCADE
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_kampfrichter_update_version
    BEFORE UPDATE ON kampfrichter
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();


/**
 * Einführung eines Rollen-Rechte Konzepts
 * Das Recht wird im Codee geprüft. Der Name muss eindeutig sein.
 * wir führen hier eine Version/ Tech-Attribute ein, da diese Tabelle
 * per Skript gepflegt wird.
 */
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_recht_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE recht (
  recht_id             DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_recht_id'), -- DECIMAL(19,0) = unsigned long
  recht_name           VARCHAR(200)  NOT NULL,
    -- primary key (pk)
    -- scheme: pk_{column name}
  CONSTRAINT pk_recht_id  PRIMARY KEY (recht_id),
  CONSTRAINT uc_recht_name UNIQUE (recht_name)
);

/**
 * Einführung eines Rollen-Rechte Konzepts
 * Das Rolle wird hier definiert und aggregiert später Rechte. Der Name muss eindeutig sein.
 * wir führen hier eine Version/ Tech-Attribute ein, da diese Tabelle
 * per Skript gepflegt wird.
 */
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_rolle_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE rolle (
   rolle_id           DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_rolle_id'), -- DECIMAL(19,0) = unsigned long
   rolle_name         VARCHAR(200)    NOT NULL,
    -- primary key (pk)
    -- scheme: pk_{column name}
   CONSTRAINT pk_rolle_id  PRIMARY KEY (rolle_id)
);

/**
 * Einführung eines Rollen-Rechte Konzepts
 * Zuordnung von Rolle und Rechten
 * wir führen hier eine Version/ Tech-Attribute ein, da diese Tabelle
 * per Skript gepflegt wird.
 */
CREATE TABLE rolle_recht (
   rolle_recht_rolle_id         DECIMAL(19,0)  NOT NULL,
   rolle_recht_recht_id         DECIMAL(19,0)  NOT NULL,

   CONSTRAINT fk_rolle_recht_rolle FOREIGN KEY (rolle_recht_rolle_id) REFERENCES rolle (rolle_id),
   CONSTRAINT fk_rolle_recht_recht FOREIGN KEY (rolle_recht_recht_id) REFERENCES recht (recht_id)
);

/**
 * Einführung eines Rollen-Rechte Konzepts
 * Zuordnung von Rolle und Benutzer
 */

-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE TABLE benutzer_rolle (
  benutzer_rolle_benutzer_id        DECIMAL(19,0)  NOT NULL,
  benutzer_rolle_rolle_id           DECIMAL(19,0)  NOT NULL,

    -- technical columns to track the lifecycle of each row
    -- the "_by" columns references a "benutzer_id" without foreign key constraint
    -- the "_at_utc" columns using the timestamp with the UTC timezone
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
   created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
   created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
   last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
   last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
   version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,

   CONSTRAINT fk_benutzer_rolle_benutzer FOREIGN KEY (benutzer_rolle_benutzer_id) REFERENCES benutzer (benutzer_id),
   CONSTRAINT uniquectm_ben_rol UNIQUE (benutzer_rolle_benutzer_id, benutzer_rolle_rolle_id),
   CONSTRAINT fk_benutzer_rolle_rolle FOREIGN KEY (benutzer_rolle_rolle_id) REFERENCES rolle (rolle_id)
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_benutzer_rolle_update_version
    BEFORE UPDATE ON benutzer_rolle
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();
/*
 * general conventions:
 * - lower case
 * - use "_"
 * - use table name for column prefixes
 *
 * example:
 * table name = "userdata"
 * column prefix = "userdata_"
 */

CREATE TABLE benutzer_login_verlauf (
   benutzer_login_verlauf_benutzer_id    DECIMAL(19, 0) NOT NULL, -- DECIMAL(19,0) = unsigned long
   benutzer_login_verlauf_timestamp      TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
   benutzer_login_verlauf_login_ergebnis VARCHAR(50)    NOT NULL,

    -- unique constraint (uc)
    -- scheme: uc_{column name}
    CONSTRAINT uc_benutzer_login_verlauf UNIQUE (benutzer_login_verlauf_benutzer_id, benutzer_login_verlauf_timestamp),
    CONSTRAINT fk_benutzer_login_verlauf_benutzer_id FOREIGN KEY (benutzer_login_verlauf_benutzer_id) REFERENCES benutzer (benutzer_id)
);


/**
 * Zuordnung von Schützen zu Mannnschaften

 * Es ist zu unterscheiden ob ein dsb_mitglied für eine Mannschaft gemeldet wurde
 * und ob er in dieser Mannschaft wirklich eingesetzt wurde.
 * ein dsb_mitglied kann in mehreren Mannschaften gemeldet sein und auch in diesen eingesetzt werden.
 * genauere Prüfung erfolgt fachlich bei der Zuordnung

 Wichtig: jedes Mannschaftsmitglied bekommt eine Nummer (auch eine "Startnummer" zum Befestigen an der Kleidung)
 diese Nummer ergibt sich aus der Reihenfolge der Mannschaftsmitglieder - es wird einfach durchnummeriert...
 daher sollten keine Mannschaftsmitglieder gelöscht werden - es gibt ja auch keine Grenze der Anzahl...
 Lesen der Mannschaftmitlgieder für Tabellen sollte daher als "Sort by" mannschaftsmitglied_id erfolgen.
 **/
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE mannschaftsmitglied_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE mannschaftsmitglied (
    mannschaftsmitglied_id              DECIMAL(19, 0) NOT NULL    DEFAULT nextval('mannschaftsmitglied_id'), -- DECIMAL(19,0) = unsigned long
    mannschaftsmitglied_mannschaft_id                 DECIMAL(19,0) NOT NULL,
    mannschaftsmitglied_dsb_mitglied_id               DECIMAL(19,0) NOT NULL,
    mannschaftsmitglied_dsb_mitglied_eingesetzt       INTEGER       NOT NULL, -- 0= kein Einsatz, >0 #Einsatze
    mannschaftsmitglied_rueckennummer                 DECIMAL (19,0) NOT NULL DEFAULT 0,

    -- technical columns to track the lifecycle of each row
    -- the "_by" columns references a "benutzer_id" without foreign key constraint
    -- the "_at_utc" columns using the timestamp with the UTC timezone
    -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
    created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
    created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
    last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
    last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
    version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,

    -- primary key (pk)
    -- scheme: pk_{column name}
    CONSTRAINT pk_mannschaftsmitglied_id PRIMARY KEY (mannschaftsmitglied_id),

    -- foreign key (fk)
    -- schema: fk_{current table name}_{foreign key origin table name}
    CONSTRAINT fk_mannschaftsmitglied_mannschaft FOREIGN KEY (mannschaftsmitglied_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
        ON DELETE CASCADE, -- das Löschen einer Mannschaft löscht auch die Zuordnung der dsb_mitgliedn zur Mannschaft

    -- foreign key (fk)
    -- schema: fk_{current table name}_{foreign key origin table name}
    CONSTRAINT fk_mannschaftsmitglied_dsb_mitglied FOREIGN KEY (mannschaftsmitglied_dsb_mitglied_id) REFERENCES dsb_mitglied (dsb_mitglied_id)
        ON DELETE CASCADE -- das Löschen eines dsb_mitglieds löscht auch die Zuordnung des dsb_mitglieds zu Mannschaften
);


-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_mannschaftsmitglied_update_version
    BEFORE UPDATE ON mannschaftsmitglied
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();




/* die Tabelle bildet die Zuordnung von Spotter Tabletts zu den Sessions ab
 * die hier abgelegten Informationen dienen zum Weiterschalten
 */
CREATE TABLE tablet_session (
   tablet_session_wettkampf_id   DECIMAL(19, 0) NOT NULL,
   tablet_session_scheibennummer DECIMAL(1, 0)  NOT NULL,
   tablet_session_match_id       DECIMAL(19, 0),
   tablet_session_satznr         DECIMAL(1, 0) DEFAULT 1,
   is_active                     BOOLEAN NOT NULL DEFAULT TRUE,
   access_token                  DECIMAL(19,0) DEFAULT 0,
   --- tech attributes
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
CREATE TRIGGER tr_tablet_session_update_version
    BEFORE UPDATE
    ON tablet_session
    FOR EACH ROW
EXECUTE PROCEDURE update_row_version();


/**
 * Die Ligatabelle wird durch einen view auf die Tabelle Match ersetzt.
 * die Sortierung der Tabelle wird erst beim Lesen durch Generiern einer weiteren Spalte erzeugt.
 **/
CREATE VIEW ligatabelle (
          ligatabelle_veranstaltung_id,     -- Bezug zum Sportjahr
          ligatabelle_veranstaltung_name,   -- Name der Veranstaltung
          ligatabelle_wettkampf_id,        -- Daten des einzelnen Wettkampfs
          ligatabelle_wettkampf_tag,        -- Liga hat 4 Wettkampftage, initiale Ligatabelle für Tag 0
          ligatabelle_mannschaft_id,        -- Bezug zur Mannschaft
          ligatabelle_mannschaft_nummer,     -- Nummer der Mannschaft
          ligatabelle_verein_id,             -- Bezug zum Verein
          ligatabelle_verein_name,           -- Name des Vereins
          ligatabelle_matchpkt,             -- Summe aller eigene Matchpunkte
          ligatabelle_matchpkt_gegen,       -- Summe aller gegnerischen Matchpunkte
          ligatabelle_satzpkt,              -- Summe aller eigenen Satzpunkte
          ligatabelle_satzpkt_gegen,        -- Summe aller gegnerischen Satzpunkte
          ligatabelle_satzpkt_differenz,    -- Differrenz der Satzpunkte
          ligatabelle_sortierung            -- editierbares Attribut der Mannschaft für Sortierung bei Punkte-Gleichstand
    )
AS (
   select
       veranstaltung_id,
       veranstaltung_name,
       wettkampf_id,
       wettkampf_tag,
       mannschaft_id,
       mannschaft_nummer,
       verein_id,
       verein_name,
       sum(match.match_matchpunkte) as matchpkt,
       sum(match_gegen.match_matchpunkte) as matchpkt_gegen,
       sum(match.match_satzpunkte) as satzpkt,
       sum(match_gegen.match_satzpunkte) as satzpkt_gegen,
       (sum(match.match_satzpunkte) - sum(match_gegen.match_satzpunkte)) as satzpkt_differenz,
       mannschaft_sortierung

   from match, veranstaltung, wettkampf, mannschaft, verein, match as match_gegen
   where
           match.match_wettkampf_id = wettkampf_id
     and wettkampf_veranstaltung_id = veranstaltung_id
     and match.match_mannschaft_id = mannschaft_id
     and mannschaft_verein_id = verein_id
     and match.match_wettkampf_id = match_gegen.match_wettkampf_id
     and match.match_nr = match_gegen.match_nr
     and match.match_begegnung = match_gegen.match_begegnung
     and match.match_mannschaft_id != match_gegen.match_mannschaft_id
   group by veranstaltung_id,
            veranstaltung_name,
            wettkampf_id,
            wettkampf_tag,
            mannschaft_id,
            mannschaft_nummer,
            verein_id,
            verein_name,
            mannschaft_sortierung
       );



---- first Data

DELETE from configuration;

INSERT INTO configuration (configuration_key, configuration_value)
VALUES
    -- Comment
    ('app.bogenliga.frontend.autorefresh.active', 'true'),
    ('app.bogenliga.frontend.autorefresh.interval', '10')
;

DELETE from rolle_recht;
DELETE from recht;

INSERT INTO recht(
    recht_id,
    recht_name
)
VALUES
     (0,'CAN_READ_DEFAULT'),
     (1,'CAN_READ_STAMMDATEN'),
     (2,'CAN_MODIFY_STAMMDATEN'),
     (3,'CAN_DELETE_STAMMDATEN'),
     (4,'CAN_READ_SYSTEMDATEN'),
     (5,'CAN_MODIFY_SYSTEMDATEN'),
     (6,'CAN_DELETE_SYSTEMDATEN'),
     (7,'CAN_READ_WETTKAMPF'),
     (8,'CAN_MODIFY_WETTKAMPF'),
     (9,'CAN_READ_MY_VEREIN'),
     (10,'CAN_MODIFY_MY_VEREIN'),
     (11,'CAN_READ_MY_VERANSTALTUNG'),
     (12,'CAN_MODIFY_MY_VERANSTALTUNG'),
     (13,'CAN_READ_MY_ORT'),
     (14,'CAN_MODIFY_MY_ORT'),
     (15,'CAN_READ_SPORTJAHR'),
     (16,'CAN_MODIFY_SPORTJAHR'),
     (17,'CAN_OPERATE_SPOTTING'),
     (18,'CAN_CREATE_STAMMDATEN'),
     (19,'CAN_CREATE_SYSTEMDATEN'),
     (20,'CAN_CREATE_WETTKAMPF'),
     (21,'CAN_READ_DSBMITGLIEDER'),
     (22,'CAN_CREATE_DSBMITGLIEDER'),
     (23,'CAN_MODIFY_DSBMITGLIEDER'),
     (24,'CAN_DELETE_DSBMITGLIEDER'),
     (25,'CAN_CREATE_MANNSCHAFT'),
     (26,'CAN_CREATE_VEREIN_DSBMITGLIEDER'),
     (27,'CAN_MODIFY_VEREIN_DSBMITGLIEDER'),
     (28,'CAN_DELETE_MANNSCHAFT'),
     (29,'CAN_MODIFY_MANNSCHAFT'),
     (31,'CAN_MODIFY_SYSTEMDATEN_LIGALEITER'),
     (32,'CAN_CREATE_SYSTEMDATEN_LIGALEITER'),
     (33,'CAN_CREATE_STAMMDATEN_LIGALEITER'),
     (34,'CAN_MODIFY_STAMMDATEN_LIGALEITER')
;

DELETE from benutzer_rolle;
DELETE from rolle;

INSERT INTO rolle(
    rolle_id,
    rolle_name
)
VALUES (1, 'ADMIN'),
       (2, 'LIGALEITER'),
       (3, 'KAMPFRICHTER'),
       (4, 'AUSRICHTER'),
       (5, 'SPORTLEITER'),
       (6, 'USER'),
       (7, 'TECHNICAL-ACCOUNT'),
       (8, 'DEFAULT'),
       (9, 'MODERATOR')
;

DELETE from rolle_recht;

INSERT INTO rolle_recht(
    rolle_recht_rolle_id,
    rolle_recht_recht_id
)
VALUES
    (1,0),(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),
    (1,15),(1,16),(1,17),(1,18),(1,19),(1,21),(1,22),(1,23),
    (1,24),(1,25),(1,28),(1,29),
    (2,0),(2,1),(2,4),(2,7),(2,8),(2,10),(2,11),(2,12),
    (2,21),(2,22),(2,23),(2,25),(2,29),(2,31),(2,32),(2,33),(2,34),
    (3,0),(3,1),(3,4),(3,7),(3,11),(3,21),
    (4,0),(4,1),(4,4),(4,7),(4,13),(4,14),(4,21),
    (5,0),(5,1),(5,4),(5,7),(5,9),(5,10),(5,15),(5,21),(5,26),(5,27),
    (6,0),
    (7,0),(7,17),
    (8,0),
    (9,0),(9,7),(9,8),(9,17)
;

/* Neue Spalte korrekt befüllen - im ersten Schritt sind alle existierenden Benutzer == Gero */
UPDATE
    benutzer
SET
    benutzer_dsb_mitglied_id = T2.dsb_mitglied_id
FROM
    dsb_mitglied T2
WHERE
        T2.dsb_mitglied_nachname = 'Gras'
  AND T2.dsb_mitglied_vorname = 'Gero';


/* für den Fall. dass die Rückennummern zwar als Spalte angelegt wurde, aber keine Befüllung erfolgt ist
   werden jetzt defaults per Funktionen erstellt
 */
CREATE SEQUENCE rNrSeq START 1;
CREATE FUNCTION initRueckennummern ()
    RETURNS void AS $$
declare
    mannschaft integer;
    mitglied integer;
BEGIN
    FOR mannschaft IN SELECT DISTINCT mannschaftsmitglied_mannschaft_id FROM mannschaftsmitglied
        LOOP
            FOR mitglied IN SELECT mannschaftsmitglied_id FROM mannschaftsmitglied WHERE mannschaftsmitglied_mannschaft_id = mannschaft
                LOOP
                    UPDATE mannschaftsmitglied
                    SET mannschaftsmitglied_rueckennummer = (SELECT nextval('rNrSeq'))
                    WHERE mannschaftsmitglied_id = mitglied;
                END LOOP;
            ALTER SEQUENCE rNrSeq RESTART WITH 1;
        END LOOP;
END
$$ LANGUAGE plpgsql;

SELECT initRueckennummern();
--DROP FUNCTION initRueckennummern;
COMMIT;
DROP FUNCTION initRueckennummern();
DROP SEQUENCE rNrSeq;




---V10

ALTER TABLE configuration ADD COLUMN configuration_id INTEGER;
DROP SEQUENCE IF EXISTS configuration_1;
CREATE SEQUENCE configuration_1 START 1;
ALTER TABLE configuration ALTER COLUMN configuration_id SET DEFAULT nextval('configuration_1');
UPDATE configuration SET configuration_id = nextval('configuration_1');

alter table configuration alter column configuration_id set not null;

create unique index configuration_configuration_id_uindex
    on configuration (configuration_id);

alter table configuration drop constraint pk_configuration_key;

create unique index configuration_key
    on configuration (configuration_key);


alter table configuration
    add constraint pk_configuration_id
        primary key (configuration_id);



ALTER TABLE IF EXISTS configuration
    ADD created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc');
ALTER TABLE IF EXISTS configuration
    ADD created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0;
ALTER TABLE IF EXISTS configuration
    ADD last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL;
ALTER TABLE IF EXISTS configuration
    ADD last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL;
ALTER TABLE IF EXISTS configuration
    ADD version               DECIMAL(19,0)    NOT NULL    DEFAULT 0;

INSERT INTO configuration (configuration_key, configuration_value)
VALUES('SMTPHost','mx2f1a.netcup.net')
ON CONFLICT (configuration_key) DO
    NOTHING;
INSERT INTO configuration (configuration_key, configuration_value)
VALUES('SMTPEmail','noreply@bsapp.de')
ON CONFLICT (configuration_key) DO
    NOTHING;
INSERT INTO configuration (configuration_key, configuration_value)
VALUES('SMTPBenutzer','noreply@bsapp.de')
ON CONFLICT (configuration_key) DO
    NOTHING;
INSERT INTO configuration (configuration_key, configuration_value)
VALUES('SMTPPort','465')
ON CONFLICT (configuration_key) DO
    NOTHING;
INSERT INTO configuration (configuration_key, configuration_value)
VALUES('SMTPPasswort','Passwort bitte ändern')
ON CONFLICT (configuration_key) DO
    NOTHING;


-- V19
INSERT INTO configuration (configuration_key, configuration_value)
VALUES('MaxWettkampfTage','4')
ON CONFLICT (configuration_key) DO NOTHING;


--- V28


-- Set SMTPEmail Regex
update configuration
SET configuration_regex = '[a-z0-9!#$%&''*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&''*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?'
where configuration_key='SMTPEmail';

-- Set SMTPPort Regex
update configuration
SET configuration_regex = '^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$'
where configuration_key='SMTPPort';

-- Set SMTPHost Regex
update configuration
SET configuration_regex = '^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)+([A-Za-z]|[A-Za-z][A-Za-z0-9\-]*[A-Za-z0-9])$'
where configuration_key='SMTPHost';


--- V32
create view schuetzenstatistik
            (schuetzenstatistik_veranstaltung_id, schuetzenstatistik_veranstaltung_name,
             schuetzenstatistik_wettkampf_id, schuetzenstatistik_wettkampf_tag, schuetzenstatistik_mannschaft_id,
             schuetzenstatistik_mannschaft_nummer, schuetzenstatistik_verein_id, schuetzenstatistik_verein_name,
             schuetzenstatistik_match_id, schuetzenstatistik_match_nr, schuetzenstatistik_dsb_mitglied_id, schuetzenstatistik_dsb_mitglied_name,
             schuetzenstatistik_rueckennummer,
             schuetzenstatistik_pfeilpunkte_schnitt)
as
SELECT veranstaltung.veranstaltung_id                                                                     AS schuetzenstatistik_veranstaltung_id,
       veranstaltung.veranstaltung_name                                                                   AS schuetzenstatistik_veranstaltung_name,
       wettkampf.wettkampf_id                                                                             AS schuetzenstatistik_wettkampf_id,
       wettkampf.wettkampf_tag                                                                            AS schuetzenstatistik_wettkampf_tag,
       mannschaft.mannschaft_id                                                                           AS schuetzenstatistik_mannschaft_id,
       mannschaft.mannschaft_nummer                                                                       AS schuetzenstatistik_mannschaft_nummer,
       verein.verein_id                                                                                   AS schuetzenstatistik_verein_id,
       verein.verein_name                                                                                 AS schuetzenstatistik_verein_name,
       match.match_id                                                                                     AS schuetzenstatistik_match_id,
       match.match_nr                                                                                     AS schuetzenstatistik_match_nr,
       dsb_mitglied.dsb_mitglied_id                                                                       AS schuetzenstatistik_dsb_mitglied_id,
       (dsb_mitglied.dsb_mitglied_vorname::text || ' '::text) ||
       dsb_mitglied.dsb_mitglied_nachname::text                                                           AS schuetzenstatistik_dsb_mitglied_name,
       mannschaftsmitglied.mannschaftsmitglied_rueckennummer                                              AS schuetzenstatistik_rueckennummer,

       (sum(COALESCE(passe.passe_ringzahl_pfeil1, 0::numeric)) +
        sum(COALESCE(passe.passe_ringzahl_pfeil2, 0::numeric)) +
        sum(COALESCE(passe.passe_ringzahl_pfeil3, 0::numeric)) +
        sum(COALESCE(passe.passe_ringzahl_pfeil4, 0::numeric)) +
        sum(COALESCE(passe.passe_ringzahl_pfeil5, 0::numeric)) +
        sum(COALESCE(passe.passe_ringzahl_pfeil6, 0::numeric))) / (sum(
                 CASE
                     WHEN passe.passe_ringzahl_pfeil1 IS NOT NULL
                         THEN 1
                     ELSE 0
                     END) + sum(
                 CASE
                     WHEN passe.passe_ringzahl_pfeil2 IS NOT NULL
                         THEN 1
                     ELSE 0
                     END) + sum(
                 CASE
                     WHEN passe.passe_ringzahl_pfeil3 IS NOT NULL
                         THEN 1
                     ELSE 0
                     END) + sum(
                 CASE
                     WHEN passe.passe_ringzahl_pfeil4 IS NOT NULL
                         THEN 1
                     ELSE 0
                     END) + sum(
                 CASE
                     WHEN passe.passe_ringzahl_pfeil5 IS NOT NULL
                         THEN 1
                     ELSE 0
                     END) + sum(
                 CASE
                     WHEN passe.passe_ringzahl_pfeil6 IS NOT NULL
                         THEN 1
                     ELSE 0
                     END))::numeric             AS schuetzenstatistik_pfeilpunkte_schnitt
FROM match,
     veranstaltung,
     wettkampf,
     mannschaft,
     mannschaftsmitglied,
     verein,
     dsb_mitglied,
     passe,
     match match_gegen
WHERE match.match_wettkampf_id = wettkampf.wettkampf_id
  AND wettkampf.wettkampf_veranstaltung_id = veranstaltung.veranstaltung_id
  AND match.match_mannschaft_id = mannschaft.mannschaft_id
  AND mannschaft.mannschaft_verein_id = verein.verein_id
  AND match.match_wettkampf_id = match_gegen.match_wettkampf_id
  AND match.match_nr = match_gegen.match_nr
  AND passe.passe_match_nr = match.match_nr
  AND mannschaftsmitglied_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
  AND mannschaftsmitglied.mannschaftsmitglied_mannschaft_id = mannschaft.mannschaft_id
  AND passe.passe_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
  AND passe.passe_wettkampf_id = wettkampf.wettkampf_id
  AND passe.passe_mannschaft_id = mannschaft.mannschaft_id
  AND match.match_begegnung = match_gegen.match_begegnung
  AND match.match_mannschaft_id <> match_gegen.match_mannschaft_id
GROUP BY veranstaltung.veranstaltung_id, veranstaltung.veranstaltung_name, wettkampf.wettkampf_id,
         wettkampf.wettkampf_tag, mannschaft.mannschaft_id, mannschaft.mannschaft_nummer, verein.verein_id,
         verein.verein_name, match.match_id, match.match_nr, dsb_mitglied.dsb_mitglied_id,
         ((dsb_mitglied.dsb_mitglied_vorname::text || ' '::text) || dsb_mitglied.dsb_mitglied_nachname::text),
         mannschaftsmitglied.mannschaftsmitglied_rueckennummer,
         (passe.passe_ringzahl_pfeil1 + passe.passe_ringzahl_pfeil2 + passe.passe_ringzahl_pfeil3 +
          passe.passe_ringzahl_pfeil4 + passe.passe_ringzahl_pfeil5 + passe.passe_ringzahl_pfeil6)
ORDER BY ((sum(COALESCE(passe.passe_ringzahl_pfeil1, 0::numeric)) +
           sum(COALESCE(passe.passe_ringzahl_pfeil2, 0::numeric)) +
           sum(COALESCE(passe.passe_ringzahl_pfeil3, 0::numeric)) +
           sum(COALESCE(passe.passe_ringzahl_pfeil4, 0::numeric)) +
           sum(COALESCE(passe.passe_ringzahl_pfeil5, 0::numeric)) +
           sum(COALESCE(passe.passe_ringzahl_pfeil6, 0::numeric))) / (sum(
                                  CASE
                                      WHEN passe.passe_ringzahl_pfeil1 IS NOT NULL
                                          THEN 1
                                      ELSE 0
                                      END) + sum(
                                  CASE
                                      WHEN passe.passe_ringzahl_pfeil2 IS NOT NULL
                                          THEN 1
                                      ELSE 0
                                      END) + sum(
                                  CASE
                                      WHEN passe.passe_ringzahl_pfeil3 IS NOT NULL
                                          THEN 1
                                      ELSE 0
                                      END) + sum(
                                  CASE
                                      WHEN passe.passe_ringzahl_pfeil4 IS NOT NULL
                                          THEN 1
                                      ELSE 0
                                      END) + sum(
                                  CASE
                                      WHEN passe.passe_ringzahl_pfeil5 IS NOT NULL
                                          THEN 1
                                      ELSE 0
                                      END) + sum(
                                  CASE
                                      WHEN passe.passe_ringzahl_pfeil6 IS NOT NULL
                                          THEN 1
                                      ELSE 0
                                      END))::numeric) DESC;

alter table schuetzenstatistik
    owner to swt2;


--- V34

/* we will create a view to table PASSE to enchance performance
*  when reading data for a single schusszettel
*  by creating a view inluding all relevant data for disloge display
*  and the indicies to be able to save the data afterwards
*/

/* in case the view is already existing, we will drop it first */
Drop VIEW if exists ligapasse;

/* the view includes the name of the contestent - it by be diaplayed as a tooltip
 * to read the data for a single schusszettel make sure to read all entries
 * for a given combination of wettkampf_id and match_id
 * data is ordered by passe_lfdnr and rueckennummer
 */
CREATE VIEW ligapasse (
                       ligapasse_wettkampf_id,
                       ligapasse_match_id,
                       ligapasse_passe_id,
                       ligapasse_passe_lfdnr,
                       ligapasse_passe_mannschaft_id,
                       ligapasse_dsb_mitglied_id,
                       ligapasse_dsb_mitglied_name,
                       ligapasse_mannschaftsmitglied_rueckennummer,
                       ligapasse_passe_ringzahl_pfeil1,
                       ligapasse_passe_ringzahl_pfeil2,
                       ligapasse_passe_match_nr
    )
AS (
   select
       passe.passe_wettkampf_id,
       passe.passe_match_id,
       passe.passe_id,
       passe.passe_lfdnr,
       passe.passe_mannschaft_id,
       passe.passe_dsb_mitglied_id,
       dsb_mitglied.dsb_mitglied_nachname  || ','  || dsb_mitglied.dsb_mitglied_vorname as name,
       mannschaftsmitglied.mannschaftsmitglied_rueckennummer,
       passe.passe_ringzahl_pfeil1,
       passe.passe_ringzahl_pfeil2,
       passe.passe_match_nr
   from passe as passe,
        dsb_mitglied as dsb_mitglied,
        mannschaftsmitglied as mannschaftsmitglied

   where passe.passe_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id
     and passe.passe_mannschaft_id = mannschaftsmitglied.mannschaftsmitglied_mannschaft_id
     and mannschaftsmitglied.mannschaftsmitglied_dsb_mitglied_id = passe.passe_dsb_mitglied_id
   order by passe.passe_lfdnr, mannschaftsmitglied.mannschaftsmitglied_rueckennummer
       );


update passe
SET passe_match_id = (select match.match_id from match
                      where passe_mannschaft_id = match.match_mannschaft_id
                        and passe_wettkampf_id = match.match_wettkampf_id
                        and passe_match_nr = match.match_nr);



--- V37

-- lizenzen erstellen fuer alle mannschaftsmitglieder, die noch keine haben
-- lizenznummer besteht aus: Region Kürzel, Veranstaltung ID, Verein ID, Mannschaftsnummer, DSB Mitglied ID des Schützen
INSERT INTO lizenz (
    lizenz_nummer,
    lizenz_region_id,
    lizenz_dsb_mitglied_id,
    lizenz_typ,
    lizenz_disziplin_id
)
-- keine doppelungen: eine liga lizenz pro spieler, aber kampfrichter und liga möglich:
SELECT DISTINCT (
                                    r.region_kuerzel ||
                                    CAST(m.mannschaft_veranstaltung_id AS TEXT) ||
                                    CAST(m.mannschaft_verein_id AS TEXT) ||
                                    CAST(m.mannschaft_nummer AS TEXT) ||
                                    CAST(mg.mannschaftsmitglied_dsb_mitglied_id AS TEXT)
                    ) AS lizenz_nummer,
                v.verein_region_id AS lizenz_region_id,
                mg.mannschaftsmitglied_dsb_mitglied_id AS lizenz_dsb_mitglied_id,
                'Liga' AS lizenz_typ,
                w.wettkampf_disziplin_id AS lizenz_disziplin_id

FROM verein v JOIN mannschaft m ON v.verein_id = m.mannschaft_verein_id
              JOIN mannschaftsmitglied mg ON m.mannschaft_id = mg.mannschaftsmitglied_mannschaft_id
              JOIN region r ON v.verein_region_id = r.region_id
              JOIN wettkampf w ON w.wettkampf_veranstaltung_id = m.mannschaft_veranstaltung_id

WHERE NOT EXISTS (
        SELECT *
        FROM lizenz l
        WHERE l.lizenz_dsb_mitglied_id = mg.mannschaftsmitglied_dsb_mitglied_id
          AND l.lizenz_typ = 'Liga'
    )
;
--V38
INSERT INTO configuration (configuration_key, configuration_value)
VALUES('aktives-Sportjahr','2022')
ON CONFLICT (configuration_key) DO NOTHING;

-- V40
UPDATE configuration
SET configuration_regex = '[a-z0-9!#$%&''*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&''*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?'
WHERE configuration_key = 'SMTPEmail';

UPDATE configuration
SET configuration_regex = '^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$'
WHERE configuration_key = 'SMTPPort';

UPDATE configuration
SET configuration_regex = '^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)+([A-Za-z]|[A-Za-z][A-Za-z0-9\-]*[A-Za-z0-9])$'
WHERE configuration_key = 'SMTPHost';

UPDATE configuration
SET configuration_regex = '^(true|false)$'
WHERE configuration_key = 'app.bogenliga.frontend.autorefresh.active';

UPDATE configuration
SET configuration_regex = '^[1-9]$|^[1-9][0-9]$|^(100)$'
WHERE configuration_key = 'app.bogenliga.frontend.autorefresh.interval';

UPDATE configuration
SET configuration_regex = '[1-4]'
WHERE configuration_key = 'MaxWettkampfTage';

UPDATE configuration
SET configuration_regex = '^2[01][0-9]{2}'
WHERE configuration_key = 'aktives-Sportjahr';

update configuration
set configuration_hidden = true
where configuration_key in ('SMTPBenutzer', 'SMTPPort', 'SMTPHost', 'SMTPEmail', 'SMTPPasswort');




--- erste Testdaten

INSERT INTO region (region_id, region_name, region_kuerzel, region_typ, region_uebergeordnet)
VALUES
    (0, 'Deutscher Schützenbund',             'DSB',      'BUNDESVERBAND', NULL),
    (1, 'Württembergischer Schützenverband',  'WT',       'LANDESVERBAND', 0),
    (2, 'Badischer Sportschützenverband',     'BD',       'LANDESVERBAND', 0),
    (3, 'Bezirk Stuttgart',                   'BZ S',     'BEZIRK', 1),
    (4, 'Bezirk Ludwigsburg',                 'BZ LB',    'BEZIRK', 1),
    (5, 'Kreis Stuttgart',                    'KR S',     'KREIS', 3),
    (6, 'Kreis Ludwigsburg',                  'KR LB',    'KREIS', 4),
    (7, 'Kreis Welzheim',                     'KR WZ',    'KREIS', 8),
    (8, 'Bezirk Welzheim',                    'BZ WZ',    'BEZIRK', 1),
    (9, 'Bezirk Baden',                       'BZ BA',    'BEZIRK', 2),
    (10, 'Kreis Baden-Baden',                 'KR B-B',   'KREIS', 9),
    (11, 'Kreis Nürtingen',                   'KR NT',    'KREIS', 8),
    (12, 'Bezirk Nürtingen',                  'BZ NT',    'BEZIRK', 1)
;

INSERT INTO verein (verein_id, verein_name, verein_dsb_identifier, verein_region_id)
VALUES
    (0, 'SGes Gerstetten', '32WT525401', 6),
    (1, 'SV Schwieberdingen', '32WT535623', 7),
    (2, 'BoAbt Fasanenhof Stuttgart', 'XXWT717504', 5),
    (3, 'SGes Bempflingen', '33WT424108', 5),
    (4, 'SV Waldmössingen', '33WT616315', 6),
    (5, 'SSV Ehingen', '33WT414516', 7),
    (6, 'SSV Hechingen', '33WT616516', 6),
    (7, 'SBC Magstadt', '33WT414418', 6),
    (8, 'SV Baden-Baden', 'XXXT616516', 10),
    (9, 'SV Brochenzell', '33WT434524', 7),
    (10, 'BS Nürtingen', '33WT434424', 7),
    (11, 'BSC Stuttgart', 'XXWT424108', 5),
    (12, 'SV Kirchberg', 'XXWT4241XX', 5)
;


INSERT INTO dsb_mitglied (
    dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum,
    dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id,
    dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_by, version
)
VALUES
    (0, 'Gero', 'Gras', '2021.01.01', 'DE', 'WT007', 2, NULL, '2023-06-30 14:16:57.048792', 0, null, 0)
    ON CONFLICT DO NOTHING;


INSERT INTO benutzer(
    benutzer_id,
    benutzer_email,
    benutzer_dsb_mitglied_id,
    benutzer_salt,
    benutzer_password
)
VALUES
-- password = admin
(1,
 'admin@bogenliga.de',
 0,
 'a9a2ef3c5a023acd2fc79ebd9c638e0ebb62db9c65fa42a6ca43d5d957a4bdf5413c8fc08ed8faf7204ba0fd5805ca638220b84d07c0690aed16ab3a2413142d',
 '0d31dec64a1029ea473fb10628bfa327be135d34f8013e7238f32c019bd0663a7feca101fd86ccee1339e79db86c5494b6e635bb5e21456f40a6722952c53079')
    ON CONFLICT DO NOTHING;


INSERT INTO benutzer_rolle(
    benutzer_rolle_benutzer_id,
    benutzer_rolle_rolle_id
)
VALUES (1, 1); -- admin



INSERT INTO klasse (klasse_id, klasse_name, klasse_alter_min, klasse_alter_max, klasse_nr)
VALUES

    (0,'Herren' ,21, 49 ,10),
    (1,'Damen' ,21 ,49 ,11),
    (2,'Schüler A männl.' ,13 ,14 ,20),
    (3,'Schüler A weibl.' ,13 ,14 ,21),
    (4,'Schüler B männl.' ,11 ,12 ,22),
    (5,'Schüler B weibl.' ,11 ,12 ,23),
    (6,'Jugend männl.' ,15 ,17 ,30),
    (7,'Jugend weibl.' ,15 ,17 ,31),
    (8,'Junioren' ,18 ,20 ,40),
    (9,'Junioren weibl.' ,18 ,20 ,41),
    (10,'Master m' ,50 ,65 ,12),
    (11,'Master w' ,50 ,65 ,13),
    (12,'Senioren' ,66 ,99 ,14),
    (13,'Senioren weibl.' ,66 ,99 ,15),
    (14,'unbestimmt' ,00 ,99 ,16)
;

INSERT INTO disziplin (disziplin_id, disziplin_name)
VALUES

    (0,'Recurve'),
    (1,'Compound'),
    (2,'Blankbogen'),
    (3,'Langbogen'),
    (4,'Instinktiv')
;

INSERT INTO wettkampftyp (wettkampftyp_id, wettkampftyp_name)
VALUES

    (0,'Liga kummutativ'),
    (1,'Liga Satzsystem'),
    (2,'WA im Freien'),
    (3,'WA in der Halle'),
    (4,'WA Stern Turnier'),
    (5,'Feldbogen'),
    (6,'3D Turnier');

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, liga_disziplin_id, liga_detail)
VALUES
    (1,	0, 'Bundesliga', null, 1,0,'init'),
    (2,	1, 'Württembergliga Recurve',	1, 1,0,'init'),
    (3,	1, 'Landesliga Nord Recurve',	2, 1,0,'init'),
    (4,	1, 'Landesliga Süd Recurve',	2, 1,0,'init'),
    (5,	3, 'Bezirksoberliga',	3, 1,0,'init'),
    (6,	3, 'Bezirksliga A',	5, 1,0,'init'),
    (7,	3, 'Bezirksliga B',	6, 1,0,'init');

-- V51, V55, V60 - auffuellmannschaft/ platzhalter...
INSERT INTO verein (verein_id, verein_name, verein_dsb_identifier, verein_region_id, created_at_utc, created_by, last_modified_at_utc, last_modified_by, version)
VALUES (99, 'Platzhalter', 'Platzhalter', 0, '2023-05-02 14:16:57.048792', 0, null, null, 0) ON CONFLICT DO NOTHING;

INSERT INTO dsb_mitglied (
    dsb_mitglied_id, dsb_mitglied_vorname, dsb_mitglied_nachname, dsb_mitglied_geburtsdatum,
    dsb_mitglied_nationalitaet, dsb_mitglied_mitgliedsnummer, dsb_mitglied_verein_id,
    dsb_mitglied_benutzer_id, created_at_utc, created_by, last_modified_by, version
)
VALUES
    (1, 'Platzhaltermitglied_Vorname1', 'Platzhaltermitlgied_Nachname1', '2021-06-30', 'DE', 'Platzhalter1', 99, null, '2023-06-30 14:16:57.048792', 0, null, 0),
    (2, 'Platzhaltermitglied_Vorname2', 'Platzhaltermitglied_Nachname2', '2021-06-30', 'DE', 'Platzhalter2', 99, null, '2023-06-30 14:16:57.048792', 0, null, 0),
    (3, 'Platzhaltermitglied_Vorname3', 'PlatzhaltermitgliedS_Nachname3', '2021-06-30', 'DE', 'Platzhalter3', 99, null, '2023-06-30 14:16:57.048792', 0, null, 0)
ON CONFLICT DO NOTHING;


--V59
CREATE VIEW ligamatch (
                       ligamatch_match_wettkampf_id,
                       ligamatch_match_id,
                       ligamatch_match_nr,
                       ligamatch_match_scheibennummer,
                       ligamatch_match_mannschaft_id,
                       ligamatch_mannschaft_name,
                       ligamatch_mannschaft__name_gegner,
                       ligamatch_match_scheibennummer_gegner,
                       ligamatch_match_id_gegner,
                       ligamatch_naechste_match_id,
                       ligamatch_naechste_naechste_match_nr_match_id,
                       ligamatch_match_strafpunkte_satz_1,
                       ligamatch_match_strafpunkte_satz_2,
                       ligamatch_match_strafpunkte_satz_3,
                       ligamatch_match_strafpunkte_satz_4,
                       ligamatch_match_strafpunkte_satz_5,
                       ligamatch_begegnung,
                       ligamatch_wettkampftyp_id,
                       ligamatch_wettkampf_tag,
                       ligamatch_satzpunkte,
                       ligamatch_matchpunkte
    )
AS
(
select match1.match_wettkampf_id,
       match1.match_id,
       match1.match_nr,
       match1.match_scheibennummer,
       match1.match_mannschaft_id,
       verein.verein_name || ' ' || mannschaft.mannschaft_nummer   as mannschaft_name,
       --      match2.match_id as naechstes_match,
       verein3.verein_name || ' ' || mannschaft3.mannschaft_nummer as mannschaft__name_gegner,
       match3.match_scheibennummer                                 as scheibennummer_gegner,
       match3.match_id                                             as match_id_gegner,
       match2.match_id                                             as naechste_match_id,
       match4.match_id                                             as naechste_match_nr_match_id,
       match1.match_strafpunkte_satz_1,
       match1.match_strafpunkte_satz_2,
       match1.match_strafpunkte_satz_3,
       match1.match_strafpunkte_satz_4,
       match1.match_strafpunkte_satz_5,
       match1.match_begegnung,
       wett.wettkampf_wettkampftyp_id,
       wett.wettkampf_tag,
       match1.match_satzpunkte,
       match1.match_matchpunkte

from match as match1,
     mannschaft as mannschaft,
     verein as verein,
     veranstaltung as veranstaltung,
     wettkampf as wettkampf,
     match as match2,
     match as match3,
     mannschaft as mannschaft3,
     verein as verein3,
     match as match4,
     wettkampf as wett

where match1.match_mannschaft_id = mannschaft.mannschaft_id
  and mannschaft.mannschaft_verein_id = verein.verein_id
  and match3.match_wettkampf_id = match1.match_wettkampf_id
  and match3.match_nr = match1.match_nr
  and match3.match_begegnung = match1.match_begegnung
  and match3.match_mannschaft_id <> match1.match_mannschaft_id
  and match3.match_mannschaft_id = mannschaft3.mannschaft_id
  and mannschaft3.mannschaft_verein_id = verein3.verein_id
  and match1.match_wettkampf_id = wettkampf.wettkampf_id
  and wettkampf.wettkampf_veranstaltung_id = veranstaltung.veranstaltung_id
  and match2.match_wettkampf_id = match1.match_wettkampf_id
  and match2.match_nr = match1.match_nr
  and match2.match_scheibennummer = LEAST(match1.match_scheibennummer + 1, veranstaltung.veranstaltung_groesse)
  and match4.match_wettkampf_id = match1.match_wettkampf_id
  and match4.match_nr = CASE
                            WHEN veranstaltung.veranstaltung_groesse IN (8, 6) THEN LEAST(match1.match_nr + 1, veranstaltung.veranstaltung_groesse - 1)
                            WHEN veranstaltung.veranstaltung_groesse = 4 THEN LEAST(match1.match_nr + 1, 6)
    END
  and match4.match_scheibennummer = 1
  and wett.wettkampf_id = match1.match_wettkampf_id
order by match1.match_wettkampf_id, match1.match_nr, match1.match_scheibennummer
    )
;


/*
 die folgenden Tabellen werdne für die Datenübernahme aus dem Vorgängersystem (bogenliga.de) verwendet
 die Übernahme erzeugt über den Code weitere Tabellen temporär - bzw. beim Start der Übernahme jeweils neu
 Die Tabelle anederung dokumentiert je Datensatz eine Änderung und deren Status
 Die Tabelle uebersetzung bildet das gedächtnis um alle Übersetzungen der IDs zwischen den System
 konsistent und Transaktionsübergreifend konsistent zu halten.
 */

CREATE SEQUENCE  IF NOT EXISTS Uebersetzung_ID START WITH 1 INCREMENT BY 1;
CREATE TABLE altsystem_uebersetzung
(
    uebersetzung_id int NOT NULL PRIMARY KEY DEFAULT nextval('Uebersetzung_ID'),
    kategorie       VARCHAR,
    altsystem_id    INTEGER,
    bogenliga_id    INTEGER,
    wert            VARCHAR,
    CONSTRAINT unique_combination UNIQUE (kategorie, altsystem_id)
);


CREATE TABLE altsystem_aenderung_operation (
                                               operation_id   INT     NOT NULL PRIMARY KEY,
                                               operation_name VARCHAR NOT NULL
);

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


CREATE SEQUENCE  altsystem_aenderung_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE altsystem_aenderung (
                                     aenderung_id         INT            NOT NULL PRIMARY KEY DEFAULT NEXTVAL('altsystem_aenderung_id'),
                                     kategorie            VARCHAR        NOT NULL,
                                     altsystem_id         INT            NOT NULL,
                                     operation            INT            NOT NULL, -- references a key in enum altsystem_aenderung_operation
                                     status               INT            NOT NULL, -- references a key in enum altsystem_aenderung_status
                                     nachricht            VARCHAR,
                                     run_at_utc           TIMESTAMP,
    -- technical columns to track the lifecycle of each row
                                     created_at_utc       TIMESTAMP      NOT NULL             DEFAULT (NOW() AT TIME ZONE 'utc'),
                                     created_by           DECIMAL(19, 0) NOT NULL             DEFAULT 0,
                                     last_modified_at_utc TIMESTAMP      NULL                 DEFAULT NULL,
                                     last_modified_by     DECIMAL(19, 0) NULL                 DEFAULT NULL,
                                     version              DECIMAL(19, 0) NOT NULL             DEFAULT 0
);
-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
CREATE TRIGGER tr_altsystem_aenderung_update_version
    BEFORE UPDATE ON altsystem_aenderung
    FOR EACH ROW EXECUTE PROCEDURE update_row_version();

CREATE SEQUENCE sq_migrationTimestamp_id START WITH 1200 INCREMENT BY 1;
CREATE TABLE Migrationtimestamp (
                                    id DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_migrationTimestamp_id'),
                                    timestamp TIMESTAMP
);

-- Insert default values only if the keys don't exist
INSERT INTO configuration(configuration_key, configuration_value, configuration_hidden )
VALUES ('OLDDBBenutzer', '*****', TRUE) ON CONFLICT (configuration_key) DO NOTHING;

INSERT INTO configuration(configuration_key, configuration_value, configuration_hidden)
VALUES ('OLDDBPassword', '*****', TRUE) ON CONFLICT (configuration_key) DO NOTHING;

INSERT INTO configuration(configuration_key, configuration_value, configuration_hidden)
VALUES ('OLDDBHost', '*****', TRUE) ON CONFLICT (configuration_key) DO NOTHING;

INSERT INTO configuration(configuration_key, configuration_value, configuration_hidden)
VALUES ('OLDDBPort', '*****', TRUE) ON CONFLICT (configuration_key) DO NOTHING;

INSERT INTO configuration(configuration_key, configuration_value, configuration_hidden)
VALUES ('OLDDBName', '*****', TRUE) ON CONFLICT (configuration_key) DO NOTHING

