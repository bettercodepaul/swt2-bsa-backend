CREATE OR REPLACE FUNCTION update_row_version()
  returns TRIGGER AS $update_row_version_trigger$
BEGIN
  new.version = old.version + 1;
  return new;
END
$update_row_version_trigger$
LANGUAGE plpgsql;
CREATE TABLE configuration (
  configuration_key   VARCHAR(200) NOT NULL,
  configuration_value TEXT         NOT NULL,
  CONSTRAINT pk_configuration_key PRIMARY KEY (configuration_key)
);
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
CREATE SEQUENCE sq_region_id START WITH 1000 INCREMENT BY 1;


/**
 * Eine Region dient der räumlichen Einteilung des Deutscher Schützenbundes (DSB).
 * Regionen sind hierarisch in einer Baumstruktur eingeordnet.
 * Der DSB bildet den Root-Knoten.
 **/
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
CREATE SEQUENCE sq_verein_id START WITH 1000 INCREMENT BY 1;

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


-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_benutzer_id START WITH 1000 INCREMENT BY 1;

-- Userdaten für die technische Userverwaltung (Authentifizierung, Sessionmanagement, etc)
CREATE TABLE benutzer (
  benutzer_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_benutzer_id'), -- DECIMAL(19,0) = unsigned long
  benutzer_email            VARCHAR(200)    NOT NULL,
  benutzer_salt            VARCHAR(200)    NOT NULL, -- TODO uuid für salt verwenden (in java uuid.generate)
  benutzer_password            VARCHAR(200)    NOT NULL,
  benutzer_using_2fa        BOOLEAN   NOT NULL DEFAULT FALSE,
  benutzer_secret       VARCHAR(200) NULL,
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
  CONSTRAINT uc_benutzer_email UNIQUE (benutzer_email)
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_benutzer_update_version
  BEFORE UPDATE ON benutzer
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_dsb_mitglied_id START WITH 1000 INCREMENT BY 1;

/**
 * Ein Schütze enthält die notwendigen personenbezogenen Daten.
 * Ein Schütze ist immer einem Verein zugewiesen.
 **/
CREATE TABLE dsb_mitglied (
  dsb_mitglied_id              DECIMAL(19, 0) NOT NULL    DEFAULT nextval(
      'sq_dsb_mitglied_id'), -- DECIMAL(19,0) = unsigned long
  dsb_mitglied_vorname         VARCHAR(200)   NOT NULL,
  dsb_mitglied_nachname        VARCHAR(200)   NOT NULL,
  dsb_mitglied_geburtsdatum    DATE           NOT NULL,
  dsb_mitglied_nationalitaet   VARCHAR(5)     NOT NULL, -- TODO Format in User Store gem. ISO festelegen und prüfen
  dsb_mitglied_mitgliedsnummer VARCHAR(200)   NOT NULL,
  dsb_mitglied_verein_id       DECIMAL(19, 0) NOT NULL, --Fremdschluessel zum Verein
  dsb_mitglied_benutzer_id     DECIMAL(19, 0) NULL,


  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc               TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by                   DECIMAL(19, 0) NOT NULL    DEFAULT 0,
  last_modified_at_utc         TIMESTAMP      NULL        DEFAULT NULL,
  last_modified_by             DECIMAL(19, 0) NULL        DEFAULT NULL,
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
  ON DELETE CASCADE, -- das Löschen eines Vereins löscht auch dessen Mitglieder

  CONSTRAINT fk_dsb_mitglied_benutzer_id FOREIGN KEY (dsb_mitglied_benutzer_id) REFERENCES benutzer (benutzer_id)
  ON DELETE SET NULL
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_dsb_mitglied_update_version
  BEFORE UPDATE ON dsb_mitglied
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_klasse_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Klasse dient der Defintion von Gruppen gleichen Alters
 * Relevant ist der Jahrgang und das daraus resultierende Alter des Schützen.
 **/
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
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_klasse_update_version
  BEFORE UPDATE ON klasse
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_disziplin_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Disziplin dient der Defintion von Bogentypen für die die gleichen Anforderung gelten.
 **/
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
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_disziplin_update_version
  BEFORE UPDATE ON disziplin
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_wettkampftyp_id START WITH 1000 INCREMENT BY 1;

/**
 * Defintion von Wettkampftypen - Wettkämpfe mit identischen Regeln
 **/
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
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_wettkampftyp_update_version
  BEFORE UPDATE ON wettkampftyp
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();

-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_liga_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE liga (
  liga_id               DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_liga_id'), -- DECIMAL(19,0) = unsigned long
  liga_region_id        DECIMAL(19,0)   NOT NULL, --Fremdschluessel zur Region
  liga_name             VARCHAR(200)    NOT NULL,
  liga_uebergeordnet    DECIMAL(19,0)   NULL,  -- Verweis auf die uebergeordnete Liga - bei Bundesliga (ganz oben) leer
  liga_verantwortlich   DECIMAL(19,0)   NULL,  -- Verweis auf den Verantwortlichen User für die Liga


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
  CONSTRAINT fk_liga_benutzer FOREIGN KEY (liga_verantwortlich) REFERENCES benutzer (benutzer_id)
);


-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_liga_update_version
  BEFORE UPDATE ON liga
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_lizenz_id START WITH 1000 INCREMENT BY 1;

/**
 * Einen Lizenz kann verschieden Berechtigungen fachlicher Art für den Schützen darstellen
 * wir unterscheiden aktuell zischenn der Kapmfprichter Lizenz
 * --> Berechtigung zur Übernahme der Rolle Kapmfrichter für einen Wetttkamp
 * und der Liga Lizenz --> Berechtigung zur Teilnahme an Liga Wettkämpfen einer Disziplin.
 **/

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

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
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
CREATE SEQUENCE sq_veranstaltung_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Klasse dient der defintion von Veranstaltungen im Sportjahr
 * für die Ligen könnte auf dieser Ebenbe bereits die Diaziplin festgelegt werden
 * allgemein geht dies aber erst auf der Ebene Wettkampf.
 **/
CREATE TABLE veranstaltung (
  veranstaltung_id                  DECIMAL(19, 0) NOT NULL    DEFAULT nextval('sq_veranstaltung_id'),
  veranstaltung_wettkampftyp_id     DECIMAL(19, 0) NOT NULL, --bezug zu den Regeln
  veranstaltung_name                VARCHAR(200)   NOT NULL,
  veranstaltung_sportjahr           DECIMAL(4, 0)  NOT NULL, --Okt 2018 bis Okt 2019 wird als 2019 erfasst
  veranstaltung_meldedeadline       DATE           NOT NULL, --Termin zu dem die Anmeldungen abgeschlossen wird
  /** veranstaltung_kampfrichter_anzahl DECIMAL(2, 0), -- benötigte Kampfrichter im Wettkampf **/
  /** veranstaltung_hoehere             DECIMAL(19, 0), -- Aufstiegsliga oder Weitermeldung in Folgeveranstaltung **/
  veranstaltung_ligaleiter_id       DECIMAL(19, 0) NOT NULL, -- Benutzer-id des Koordinators aller Wettkämpfe des Sportjahres

  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
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

  CONSTRAINT fk_veranstaltung_benutzer FOREIGN KEY (veranstaltung_ligaleiter_id) REFERENCES benutzer (benutzer_id)
  ON DELETE CASCADE -- das Löschen eines wettkampftyps löscht auch die zugehörigen veranstaltungen
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_veranstaltung_update_version
  BEFORE UPDATE ON veranstaltung
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
-- auto increment sequence (sq)
-- primary key range for manually added data [0, 999]
CREATE SEQUENCE sq_wettkampf_id START WITH 1000 INCREMENT BY 1;

/**
 * Eine Klasse dient der Defintion von Wettkämpfen d.h. der konkreten Durchführung einer Veranstaltung
 **/
CREATE TABLE wettkampf (
  wettkampf_id                  DECIMAL(19,0) NOT NULL    DEFAULT nextval('sq_wettkampf_id'), -- DECIMAL(19,0) = unsigned long
  wettkampf_veranstaltung_id    DECIMAL(19,0) NOT NULL, --bezug zum Sportjahr
  wettkampf_datum               DATE NOT NULL, -- Termin der Durchführung
  wettkampf_ort                 VARCHAR(200), -- Anschrift, Name der Sporthalle, mehrere Zeilen
  wettkampf_beginn              VARCHAR(5) NOT NULL, -- Uhrzeit im Format hh:mm
  wettkampf_tag                 DECIMAL(1,0) NOT NULL, -- Liga hat 4 Wettkampftage, sonst 1
  wettkampf_disziplin_id        DECIMAL(19,0) NOT NULL,
  wettkampf_wettkampftyp_id     DECIMAL(19,0) NOT NULL,


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
  CONSTRAINT pk_wettkampf_id PRIMARY KEY (wettkampf_id),


   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_wettkampf_wettkampftyp FOREIGN KEY (wettkampf_wettkampftyp_id ) REFERENCES wettkampftyp (wettkampftyp_id)
    ON DELETE CASCADE, -- das Löschen eines wettkampftyps löscht auch die zugehörigen wettkämpfe

   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_wettkampf_veranstaltung_id FOREIGN KEY (wettkampf_veranstaltung_id ) REFERENCES veranstaltung (veranstaltung_id)
    ON DELETE CASCADE, -- das Löschen eines wettkampftyps löscht auch die zugehörigen wettkämpfe

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
CREATE SEQUENCE sq_mannschaft_id START WITH 1000 INCREMENT BY 1;

/**
 * Einen Mannschaft legt die gemeldeten Schützen für eine Liga oder einen Wettkampf fest
 * aktuell werden die Anzahl der dsb_mitgliedn nicht limitiert
 * Mannschaften bestehen i.d.R. aus Vereinsname und Nummer
 * die Nummer wird durch die Rangfolge der Ligen definiert, in der höchsten Liga ist die Mannschaft 1
 **/

CREATE TABLE mannschaft (
  mannschaft_id               DECIMAL(19, 0) NOT NULL    DEFAULT nextval('sq_mannschaft_id'),
  mannschaft_verein_id        DECIMAL(19, 0) NOT NULL,
  mannschaft_nummer           DECIMAL(2, 0)  NOT NULL,
  mannschaft_benutzer_id      DECIMAL(19, 0) NOT NULL, -- der Ansprechpartner, Meldende der Mannschaft
  mannschaft_veranstaltung_id DECIMAL(19, 0) NOT NULL,

  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
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
  ON DELETE SET NULL -- das Löschen eines Benutzers lentfernt den Verweis
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_mannschaft_update_version
  BEFORE UPDATE ON mannschaft
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
/**
 * Eine Klasse dient der Defintion von Runden im Wettkampf - sie hat nur eine laufende Nummer als ID
 * wir erwarten entweder 2 (WA-Wettkampf) oder 7 Runden (Liga),
 *
 * Der Aspekt Finalrunden ist hier noch nicht abgebildet
 * für Liga wird als Schlüssel zusätzlich das Match definiert max. 7 Matches in einer Liga-Runde
 **/

CREATE TABLE match (
  -- composite key aus mehreren Fremd- und fachlichen Schlüsseln
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
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc       TIMESTAMP      NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by           DECIMAL(19, 0) NOT NULL    DEFAULT 0,
  last_modified_at_utc TIMESTAMP      NULL        DEFAULT NULL,
  last_modified_by     DECIMAL(19, 0) NULL        DEFAULT NULL,
  version              DECIMAL(19, 0) NOT NULL    DEFAULT 0,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_match PRIMARY KEY (match_wettkampf_id, match_nr, match_begegnung, match_scheibennummer, match_mannschaft_id),

   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_match_wettkampf FOREIGN KEY (match_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
  ON DELETE CASCADE, -- das Löschen eines wettkampfs löscht auch die zugehörigen matches

  CONSTRAINT fk_match_mannschaft FOREIGN KEY (match_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
  ON DELETE CASCADE -- das Löschen einer mannschaft löscht auch die zugehörigen matches

);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
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

CREATE TABLE passe (

    passe_mannschaft_id   DECIMAL(19, 0) NULL,     --Fremdschlüsselbezug zum Mannschaft

    -- denormalisiert
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

    -- !!!ACHTUNG: Beispiel für ein Refactoring einer existierenden Tabelle!!!
    -- primary key (pk)
    -- scheme: pk_{column name}
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
EXECUTE PROCEDURE update_row_version()
;
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
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
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
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_kampfrichter_update_version
  BEFORE UPDATE ON kampfrichter
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
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

/**
 * Eine Klasse dient der Dokumentation der LigaTabelle zu Beginn der Liga (manuell) und nach
 * jedem Wettkampftag gem der Match und Satzpunkte
 **/
CREATE TABLE ligatabelle (
  ligatabelle_veranstaltung_id   DECIMAL(19,0) NOT NULL, --bezug zum Sportjahr
  ligatabelle_wettkampf_tag      DECIMAL(1,0) NOT NULL, -- Liga hat 4 Wettkampftage, initiale Ligatabelle für Tag 0
  ligatabelle_mannschaft_id      DECIMAL(19,0) NOT NULL, --bezug zur Mannschaft
  ligatabelle_tabellenplatz          DECIMAL(1,0) NOT NULL, -- resultierender Tabellenplatz nach dem Wettkampf
  ligatabelle_matchpkt               DECIMAL(4,0) NOT NULL, -- Summe aller eigene Matchpunkte
  ligatabelle_matchpkt_gegen         DECIMAL(4,0) NOT NULL, -- Summe aller gegnerischen Matchpunkte
  ligatabelle_satzpkt                DECIMAL(4,0) NOT NULL, -- Summe aller eigenen Satzpunkte
  ligatabelle_satzpkt_gegen          DECIMAL(4,0) NOT NULL, -- Summe aller gegnerischen Satzpunkte


  -- technical columns to track the lifecycle of each row
  -- the "_by" columns references a "benutzer_id" without foreign key constraint
  -- the "_at_utc" columns using the timestamp with the UTC timezone
  -- the version number is automatically incremented by UPDATE queries to detect optimistic concurrency problems
  created_at_utc        TIMESTAMP        NOT NULL    DEFAULT (now() AT TIME ZONE 'utc'),
  created_by            DECIMAL(19,0)    NOT NULL    DEFAULT 0,
  last_modified_at_utc  TIMESTAMP        NULL        DEFAULT NULL,
  last_modified_by      DECIMAL(19,0)    NULL        DEFAULT NULL,
  version               DECIMAL(19,0)    NOT NULL    DEFAULT 0,


  CONSTRAINT uc_ligatabelle UNIQUE (ligatabelle_veranstaltung_id, ligatabelle_wettkampf_tag, ligatabelle_mannschaft_id),


   -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_ligatabelle_veranstaltung FOREIGN KEY (ligatabelle_veranstaltung_id) REFERENCES veranstaltung (veranstaltung_id)
    ON DELETE CASCADE, -- das Löschen eines wettkampftyps löscht auch die zugehörigen wettkämpfe

  CONSTRAINT fk_ligatabelle_mannschaft FOREIGN KEY (ligatabelle_mannschaft_id) REFERENCES mannschaft (mannschaft_id)
    ON DELETE CASCADE -- das Löschen eines wettkampftyps löscht auch die zugehörigen wettkämpfe
);

-- define a trigger of each UPDATE statement on this table to increment the version of the affected row automatically
-- we do not need to implement the "autoincrement" of the version programmatically
-- the procedure is defined in V1__procedure__row_version_update__create.sql
CREATE TRIGGER tr_ligatabelle_update_version
  BEFORE UPDATE ON ligatabelle
  FOR EACH ROW EXECUTE PROCEDURE update_row_version();
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
CREATE SEQUENCE sq_recht_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE recht (
  recht_id             DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_recht_id'), -- DECIMAL(19,0) = unsigned long
  recht_name           VARCHAR(200)  NOT NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_recht_id  PRIMARY KEY (recht_id),

  CONSTRAINT uc_recht_name UNIQUE (recht_name)
);/*
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
CREATE SEQUENCE sq_rolle_id START WITH 1000 INCREMENT BY 1;

CREATE TABLE rolle (
  rolle_id           DECIMAL(19,0)   NOT NULL    DEFAULT nextval('sq_rolle_id'), -- DECIMAL(19,0) = unsigned long
  rolle_name         VARCHAR(200)    NOT NULL,

  -- primary key (pk)
  -- scheme: pk_{column name}
  CONSTRAINT pk_rolle_id  PRIMARY KEY (rolle_id)
);/*
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
CREATE TABLE rolle_recht (
  rolle_recht_rolle_id         DECIMAL(19,0)  NOT NULL,
  rolle_recht_recht_id         DECIMAL(19,0)  NOT NULL,

  CONSTRAINT fk_rolle_recht_rolle FOREIGN KEY (rolle_recht_rolle_id) REFERENCES rolle (rolle_id),
  CONSTRAINT fk_rolle_recht_recht FOREIGN KEY (rolle_recht_recht_id) REFERENCES recht (recht_id)
);/*
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
);/*
 * general conventions:
 * - lower case
 * - use "_"
 * - use table name for column prefixes
 *
 * example:
 * table name = "user"
 * column prefix = "user_"
 */


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
CREATE SEQUENCE mannschaftsmitglied_id START WITH 1000 INCREMENT BY 1;


CREATE TABLE mannschaftsmitglied (
  mannschaftsmitglied_id              DECIMAL(19, 0) NOT NULL    DEFAULT nextval(
      'mannschaftsmitglied_id'), -- DECIMAL(19,0) = unsigned long
  mannschaftsmitglied_mannschaft_id                 DECIMAL(19,0) NOT NULL,
  mannschaftsmitglied_dsb_mitglied_id               DECIMAL(19,0) NOT NULL,
  mannschaftsmitglied_dsb_mitglied_eingesetzt       INTEGER       NOT NULL, -- 0= kein Einsatz, >0 #Einsatze


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
INSERT INTO configuration (configuration_key, configuration_value)
VALUES
  -- Comment
  ('app.bogenliga.frontend.autorefresh.active', 'true'),
  ('app.bogenliga.frontend.autorefresh.interval', '10')
;
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
;INSERT INTO verein (verein_id, verein_name, verein_dsb_identifier, verein_region_id)
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
;INSERT INTO benutzer(
benutzer_id,
benutzer_email,
benutzer_salt,
benutzer_password
)
VALUES
-- password = admin
       (1,
        'admin@bogenliga.de',
        'a9a2ef3c5a023acd2fc79ebd9c638e0ebb62db9c65fa42a6ca43d5d957a4bdf5413c8fc08ed8faf7204ba0fd5805ca638220b84d07c0690aed16ab3a2413142d',
        '0d31dec64a1029ea473fb10628bfa327be135d34f8013e7238f32c019bd0663a7feca101fd86ccee1339e79db86c5494b6e635bb5e21456f40a6722952c53079'),
-- password = moderator
       (2,
        'moderator@bogenliga.de',
        'dbed56d612f8fc8397a79a9e63cc67236ac63027e092adda7b02cbe7c65a4916683a572d71d3cefbcdcf86ee42136b1882ce75b189b1fe3a1457cc72ced3c6ea',
        '3afca75fad3ea4e11e3e1f4274221acb4f0a833e765b21c87098c18c9ebea67eec16f849cffc4f0010ea0f6879d0a8b88c4cfd64abfcd4762cf5c123e87f0a45'),
-- password = user
       (3,
        'user@bogenliga.de',
        '99967378dbe9dcc2f13111a8d031b34918a3e45e311961587d34bdedf897d521e4e9c1808d3f393c351d6f09648feba88436573ba5019ca15d00050bf4d5857e',
        'eac206585d6589abd083febda343f6ce00504791ef915da992dd09f695cecce06ddc92d58feb1967f95b8af2a78f0a78b79ba143870b16e14f62740d95dad594'),
-- password = swt
       (4,
        'Nicholas.Corle@bogenliga.de',
        '7e287c3b7f9f8a7193f007347d7fc784a647536892bd35fc7daee318917aa7544dff21b6454e56786cf8b9263df89811c3bf14309e04025d6b8e369240f61746',
        '4c2b281392d5667a043864ab4231f558eb686c4b28fd10797de60fee89a9e3d808ad31ec0d455f4f89fdc4db84efccdfa4f9d820e5ed5f577aab5e9288bf9196'),
-- password = swt
       (5,
        'Malorie.Artman@bogenliga.de',
        '7e287c3b7f9f8a7193f007347d7fc784a647536892bd35fc7daee318917aa7544dff21b6454e56786cf8b9263df89811c3bf14309e04025d6b8e369240f61746',
        '4c2b281392d5667a043864ab4231f558eb686c4b28fd10797de60fee89a9e3d808ad31ec0d455f4f89fdc4db84efccdfa4f9d820e5ed5f577aab5e9288bf9196'),
-- password = user
       (6,
       'ligadefault',
       '99967378dbe9dcc2f13111a8d031b34918a3e45e311961587d34bdedf897d521e4e9c1808d3f393c351d6f09648feba88436573ba5019ca15d00050bf4d5857e',
        'eac206585d6589abd083febda343f6ce00504791ef915da992dd09f695cecce06ddc92d58feb1967f95b8af2a78f0a78b79ba143870b16e14f62740d95dad594'),
-- password = swt
       (7,
        'TeamLigaleiter@bogenliga.de',
        '7e287c3b7f9f8a7193f007347d7fc784a647536892bd35fc7daee318917aa7544dff21b6454e56786cf8b9263df89811c3bf14309e04025d6b8e369240f61746',
        '4c2b281392d5667a043864ab4231f558eb686c4b28fd10797de60fee89a9e3d808ad31ec0d455f4f89fdc4db84efccdfa4f9d820e5ed5f577aab5e9288bf9196'),
-- password = swt
       (8,
        'TeamSportleiter@bogenliga.de',
        '7e287c3b7f9f8a7193f007347d7fc784a647536892bd35fc7daee318917aa7544dff21b6454e56786cf8b9263df89811c3bf14309e04025d6b8e369240f61746',
        '4c2b281392d5667a043864ab4231f558eb686c4b28fd10797de60fee89a9e3d808ad31ec0d455f4f89fdc4db84efccdfa4f9d820e5ed5f577aab5e9288bf9196'),
-- password = swt
       (9,
        'TeamModerator@bogenliga.de',
        '7e287c3b7f9f8a7193f007347d7fc784a647536892bd35fc7daee318917aa7544dff21b6454e56786cf8b9263df89811c3bf14309e04025d6b8e369240f61746',
        '4c2b281392d5667a043864ab4231f558eb686c4b28fd10797de60fee89a9e3d808ad31ec0d455f4f89fdc4db84efccdfa4f9d820e5ed5f577aab5e9288bf9196')
;
-- DSB Mitglieder ohne Benutzer Account
INSERT INTO dsb_mitglied (dsb_mitglied_id,
                          dsb_mitglied_vorname,
                          dsb_mitglied_nachname,
                          dsb_mitglied_geburtsdatum,
                          dsb_mitglied_nationalitaet,
                          dsb_mitglied_mitgliedsnummer,
                          dsb_mitglied_verein_id)
VALUES (28, 'Nicholas', 'Corle', '1988-01-01', 'D', '89298571', 0),
       (30, 'Malorie', 'Artman', '1978-01-01', 'D', '80179421', 11),
       (31, 'Kassie', 'Hysmith', '1986-01-01', 'D', '87721795', 0),
       (32, 'Phebe', 'Biddle', '1990-01-01', 'D', '92039767', 11),
       (35, 'Stephaine', 'Tovar', '1998-01-01', 'D', '14168219', 5),
       (36, 'Shanae', 'Balding', '1999-01-01', 'D', '20401429', 6),
       (44, 'Shiloh', 'Pegues', '1965-01-01', 'D', '664B0918', 7),
       (45, 'Vicente', 'Strand', '1957-01-01', 'D', '59455978', 5),
       (46, 'Darrell', 'Woodham', '1973-01-01', 'D', '23770976', 6),
       (47, 'Hai', 'Antle', '1964-01-01', 'D', '66075169', 0),
       (48, 'Cammy', 'Scola', '1990-01-01', 'D', '91896623', 11),
       (51, 'Susann', 'Dowdell', '1980-01-01', 'D', '81075269', 7),
       (70, 'Jena', 'Gingras', '1967-01-01', 'D', '69400591', 0),
       (71, 'Jacqui', 'Voltz', '1991-01-01', 'D', '94154216', 11),
       (72, 'Beverley', 'Germany', '1986-01-01', 'D', '26738514', 5),
       (73, 'Rhea', 'Woodward', '1969-01-01', 'D', '71882193', 6),
       (76, 'Tosha', 'Ingles', '1949-01-01', 'D', '510815A3', 5),
       (77, 'Meghann', 'John', '1978-01-01', 'D', '80071582', 6),
       (78, 'Latrina', 'Grooms', '1992-01-01', 'D', '93807031', 7),
       (79, 'Karie', 'Minott', '1978-01-01', 'D', '79781826', 0),
       (80, 'Barbra', 'Thiessen', '1980-01-01', 'D', '81913821', 11),
       (91, 'Jeraldine', 'Remigio', '1969-01-01', 'D', '70340570', 10),
       (103, 'Lissa', 'Veach', '1995-01-01', 'D', '96527009', 11),
       (104, 'Cathryn', 'Ebron', '1962-01-01', 'D', '64230502', 5),
       (109, 'Wilbur', 'Corter', '1980-01-01', 'D', '16321064', 6),
       (110, 'Darci', 'Crist', '1964-01-01', 'D', '16124527', 7),
       (121, 'Vina', 'Fite', '2000-01-01', 'D', '11997585', 10),
       (122, 'Sherilyn', 'Kind', '1964-01-01', 'D', '65853805', 11),
       (123, 'Bailey', 'Michelsen', '1968-01-01', 'D', '69482200', 5),
       (127, 'Cortez', 'Twiggs', '1978-01-01', 'D', '79821028', 12),
       (129, 'Barb', 'Digiovanni', '1953-01-01', 'D', '54533330', 7),
       (132, 'Velda', 'Truax', '1954-01-01', 'D', '55856311', 10),
       (174, 'Nona', 'Akbar', '1963-01-01', 'D', '65017877', 11),
       (176, 'Cindy', 'Parise', '1959-01-01', 'D', '61430786', 10),
       (177, 'Jong', 'Robey', '1999-01-01', 'D', '11503580', 11),
       (178, 'Bonita', 'Preas', '1998-01-01', 'D', '99525317', 5),
       (180, 'Shizuko', 'Berndt', '1991-01-01', 'D', '93635103', 12),
       (182, 'Florence', 'Riggle', '1985-01-01', 'D', '87444779', 7),
       (186, 'Vernita', 'Costa', '1960-01-01', 'D', '62026375', 10),
       (187, 'Lue', 'Caudillo', '1957-01-01', 'D', '58703571', 11),
       (188, 'Sherley', 'Pelc', '1950-01-01', 'D', '52646729', 5),
       (189, 'Ranae', 'Coache', '1985-01-01', 'D', '87390776', 3),
       (190, 'Billy', 'Munro', '1959-01-01', 'D', '60883700', 9),
       (192, 'Collin', 'Gallup', '1987-01-01', 'D', '88667686', 5),
       (193, 'Bernita', 'Rutt', '1967-01-01', 'D', '68789531', 3),
       (216, 'Noella', 'Leung', '1985-01-01', 'D', '86660392', 9),
       (217, 'Jaye', 'Boer', '1969-01-01', 'D', '71012688', 10),
       (218, 'April', 'Holzer', '1995-01-01', 'D', '96867609', 11),
       (219, 'Jaime', 'Mastronardi', '1961-01-01', 'D', '63045871', 5),
       (220, 'Twyla', 'Marcella', '1973-01-01', 'D', '74841017', 12),
       (240, 'Ricarda', 'Terhune', '1963-01-01', 'D', '65207326', 9),
       (244, 'Milton', 'Fassett', '1962-01-01', 'D', '64745105', 10),
       (248, 'Julius', 'Scrivens', '1973-01-01', 'D', '76924684', 11),
       (249, 'Marcos', 'Ringo', '1973-01-01', 'D', '76460106', 3),
       (250, 'Dale', 'Barranco', '1973-01-01', 'D', '17405616', 7),
       (364, 'Hester', 'Wigger', '1975-01-01', 'D', '76460006', 10),
       (383, 'Sharan', 'Ratchford', '1968-01-01', 'D', '71653622', 11),
       (384, 'Shantae', 'Upshur', '1974-01-01', 'D', '76319812', 10),
       (385, 'Connie', 'Keala', '1996-01-01', 'D', '98064586', 1),
       (386, 'Birdie', 'Kulig', '1979-01-01', 'D', '81074095', 5),
       (387, 'Latosha', 'Cheung', '1976-01-01', 'D', '78736627', 6),
       (390, 'Martina', 'Kirkley', '1980-01-01', 'D', '44072584', 7),
       (392, 'Steve', 'Lohman', '1979-01-01', 'D', '80618272', 10),
       (393, 'Cindie', 'Proulx', '1989-01-01', 'D', '91442478', 1),
       (394, 'Florentina', 'Binns', '1992-01-01', 'D', '94649417', 5),
       (396, 'Vicki', 'Timmer', '1962-01-01', 'D', '64027692', 6),
       (406, 'Allegra', 'Bumgarner', '2001-01-01', 'D', '47734647', 7),
       (407, 'Vertie', 'Speier', '1979-01-01', 'D', '77822807', 5),
       (408, 'Barrie', 'Peet', '1980-01-01', 'D', '44172584', 6),
       (440, 'Shirley', 'Bierce', '2002-01-01', 'D', '14655109', 10),
       (441, 'Carletta', 'Hayden', '1984-01-01', 'D', '85999688', 1),
       (442, 'Love', 'Hogan', '2002-01-01', 'D', '35480090', 7),
       (443, 'Cierra', 'Marshell', '1982-01-01', 'D', '12991780', 5),
       (447, 'Ouida', 'Shkreli', '1987-01-01', 'D', '35621209', 6),
       (448, 'Burl', 'Hopkins', '1976-01-01', 'D', '77689293', 7),
       (449, 'Scottie', 'Rickenbacker', '1961-01-01', 'D', '63481366', 10),
       (450, 'Valarie', 'Mchugh', '1955-01-01', 'D', '73G69595', 5),
       (451, 'Elin', 'Leitner', '1967-01-01', 'D', '68660172', 6)
;

-- DSB Mitglieder mit Benutzer Account
UPDATE dsb_mitglied
SET dsb_mitglied_benutzer_id = 4
WHERE dsb_mitglied_id = 28
;

UPDATE dsb_mitglied
SET dsb_mitglied_benutzer_id = 5
WHERE dsb_mitglied_id = 30
;INSERT INTO klasse (klasse_id, klasse_name, klasse_alter_min, klasse_alter_max, klasse_nr)
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

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich)
VALUES
(1,	0, 'Bundesliga', null, 1),
(2,	1, 'Württembergliga Recurve',	1, 1),
(3,	1, 'Landesliga Nord Recurve',	2, 1),
(4,	1, 'Landesliga Süd Recurve',	2, 1),
(5,	3, 'Bezirksoberliga',	3, 1),
(6,	3, 'Bezirksliga A',	5, 1),
(7,	3, 'Bezirksliga B',	6, 1);

INSERT INTO lizenz (
    lizenz_id,
    lizenz_nummer,
    lizenz_region_id,
    lizenz_dsb_mitglied_id,
    lizenz_typ,
    lizenz_disziplin_id)
VALUES

(0, 'WT1234567', 1, 71, 'Liga', 0),
(1, 'WT12340007', 1, 103, 'Liga', 0),
(2, '0012348990', 1, 450, 'Liga', 0),
(3, '12345899', 1, 441, 'Kampfrichter', NULL),
(4, '1234589001', 1, 442, 'Kampfrichter', NULL),
(5, '123458910', 1, 450, 'Kampfrichter', NULL)
;
INSERT INTO veranstaltung (-- Recurve Ligen
                           veranstaltung_id,
                           veranstaltung_wettkampftyp_id,
                           veranstaltung_name,
                           veranstaltung_sportjahr,
                           veranstaltung_ligaleiter_id,
                           veranstaltung_meldedeadline)
VALUES
(0, 1, 'Würtembergliga', 2018, 2, '2017-10-31'),
(1, 1, 'Landesliga Nord', 2018, 2, '2017-10-31'),
(2, 1, 'Landesliga Süd', 2018, 2, '2017-10-31'),
(3, 1, 'Relegation Landesliga Nord', 2018, 2, '2017-10-31'),
(4, 1, 'Relegation Landesliga Süd', 2018, 2, '2017-10-31'),
(5, 1, 'Bezirksoberliga Neckar', 2018, 2, '2017-10-31'),
(6, 1, 'Bezirksliga A Neckar', 2018, 2, '2017-10-31'),
(7, 1, 'Bezirksoberliga Oberschwaben', 2018, 2, '2017-10-31'),
(8, 1, 'Bezirksliga A Oberschwaben', 2018, 2, '2017-10-31'),
(9, 1, 'Bezirksliga B Oberschwaben', 2018, 2, '2017-10-31')
;

INSERT INTO veranstaltung (-- Compound Ligen
                           veranstaltung_id,
                           veranstaltung_wettkampftyp_id,
                           veranstaltung_name,
                           veranstaltung_sportjahr,
                           veranstaltung_ligaleiter_id,
                           veranstaltung_meldedeadline)
VALUES
(20, 1, 'Bawü Compound Finale', 2018, 2, '2017-10-31'),
(21, 1, 'Württembergliga Compound', 2018, 2, '2017-10-31'),
(22, 1, 'Landesliga A Compound', 2018, 2, '2017-10-31'),
(23, 1, 'Landesliga B Compound', 2018, 2, '2017-10-31')
;

INSERT INTO wettkampf ( -- Recurve Ligen
  wettkampf_id,
  wettkampf_veranstaltung_id,
  wettkampf_datum,
  wettkampf_ort,
  wettkampf_beginn,
  wettkampf_tag,
  wettkampf_disziplin_id,
  wettkampf_wettkampftyp_id)
VALUES
(38, 0, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(30, 0, '2017-11-19', 'Sporthalle, 88454 Hochdorf/Riss', '13:30', 1, 0, 0),
(31, 0, '2017-12-09', 'Franz-Baum Bogenhalle, 73642 Welzheim', '13:30', 2, 0, 0),
(32, 0, '2018-01-14', 'Bogenhalle am Schützenhaus, Gurgelbergweg, 88499 Altheim-Waldhausen', '13:30', 3, 0, 1),
(33, 0, '2018-02-03', 'Franz-Baum Bogenhalle, 73642 Welzheim', '09:30', 4, 0, 1),
(34, 1, '2019-11-19', 'Sporthalle, 88454 Hochdorf/Riss', '13:30', 1, 0, 0),
(35, 1, '2019-12-09', 'Franz-Baum Bogenhalle, 73642 Welzheim', '13:30', 2, 0, 0),
(36, 1, '2020-01-14', 'Bogenhalle am Schützenhaus, Gurgelbergweg, 88499 Altheim-Waldhausen', '13:30', 3, 0, 1),
(37, 1, '2020-02-03', 'Franz-Baum Bogenhalle, 73642 Welzheim', '09:30', 4, 0, 1),
(39, 1, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(40, 2, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(41, 3, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(42, 4, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(43, 5, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(44, 6, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(45, 7, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(46, 8, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(47, 9, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(48, 20, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(49, 21, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(50, 22, '1900-01-01', ' - ', ' - ', 0, 0, 0),
(51, 23, '1900-01-01', ' - ', ' - ', 0, 0, 0)
;


/*
 *Willkommen auf der Seite der Württembergliga des WSV
 *
 *Folgende Mannschaften nehmen im Sportjahr 2018 an der Württembergliga Bogen des WSV teil:
 *
 *    SV Brochenzell
 *    SGes Gerstetten
 *    SSV Ehingen
 *    SGes Bempflingen
 *    BSC Stuttgart
 *    BC Magstadt
 *    SV Schwieberdingen
 *    SV Kirchberg / Iller
 *
 *Termine und Austragungsorte:
 *1. Wettkampftag
 *
 *19.11.2017
 *Sporthalle, 88454 Hochdorf/Riss
 *13:30 Uhr Einschießen
 *14:00 Uhr Wettkampfbeginn
 *2. Wettkampftag
 *
 *09.12.2017
 *Franz-Baum Bogenhalle, 73642 Welzheim
 *13:30 Uhr Einschießen
 *14:00 Uhr Wettkampfbeginn
 *3. Wettkampftag
 *
 *14.01.2018
 *Bogenhalle am Schützenhaus, Gurgelbergweg, 88499 Altheim-Waldhausen
 *13:30 Uhr Einschießen
 *14:00 Uhr Wettkampfbeginn
 *4. Wettkampftag
 *
 *03.02.2018
 *Franz-Baum Bogenhalle, 73642 Welzheim
 *09:30 Uhr Einschießen
 *10:00 Uhr Wettkampfbeginn
*/
INSERT INTO mannschaft (mannschaft_id,
                        mannschaft_verein_id,
                        mannschaft_nummer,
                        mannschaft_benutzer_id,
                        mannschaft_veranstaltung_id)
VALUES (101, 0, 1, 4, 0),
       (102, 1, 1, 4, 0),
       (103, 11, 1, 4, 0),
       (104, 3, 1, 4, 0),
       (105, 5, 1, 5, 0),
       (106, 7, 1, 5, 0),
       (107, 9, 1, 5, 0),
       (108, 12, 1, 5, 0)
;

INSERT INTO match (match_nr,
                   match_wettkampf_id,
                   match_begegnung,
                   match_mannschaft_id,
                   match_scheibennummer,
                   match_matchpunkte,
                   match_satzpunkte)
VALUES (1, 30, 1, 101, 1, 2, 6),
       (1, 30, 1, 102, 2, 0, 2),
       (1, 30, 2, 103, 3, 2, 6),
       (1, 30, 2, 104, 4, 0, 4),
       (1, 30, 3, 105, 5, 2, 6),
       (1, 30, 3, 106, 6, 0, 2),
       (1, 30, 4, 107, 7, 2, 7),
       (1, 30, 4, 108, 8, 0, 1),
       (2, 30, 1, 102, 1, 2, 6),
       (2, 30, 1, 107, 2, 0, 4),
       (2, 30, 2, 106, 3, 2, 6),
       (2, 30, 2, 101, 4, 0, 4),
       (2, 30, 3, 105, 5, 2, 6),
       (2, 30, 3, 103, 6, 0, 2),
       (2, 30, 4, 108, 7, 2, 7),
       (2, 30, 4, 104, 8, 0, 3),
       (3, 30, 1, 101, 1, NULL, NULL),
       (3, 30, 1, 103, 2, NULL, NULL),
       (3, 30, 2, 104, 3, NULL, NULL),
       (3, 30, 2, 102, 4, NULL, NULL),
       (3, 30, 3, 108, 5, NULL, NULL),
       (3, 30, 3, 105, 6, NULL, NULL),
       (3, 30, 4, 107, 7, NULL, NULL),
       (3, 30, 4, 106, 8, NULL, NULL),
       (4, 30, 1, 102, 1, NULL, NULL),
       (4, 30, 1, 105, 2, NULL, NULL),
       (4, 30, 2, 106, 3, NULL, NULL),
       (4, 30, 2, 104, 4, NULL, NULL),
       (4, 30, 3, 108, 5, NULL, NULL),
       (4, 30, 3, 101, 6, NULL, NULL),
       (4, 30, 4, 107, 7, NULL, NULL),
       (4, 30, 4, 103, 8, NULL, NULL)
;

INSERT INTO passe (passe_lfdnr,
                   passe_wettkampf_id,
                   passe_match_nr,
                   passe_mannschaft_id,
                   passe_dsb_mitglied_id,
                   passe_ringzahl_pfeil1,
                   passe_ringzahl_pfeil2,
                   passe_ringzahl_pfeil3,
                   passe_ringzahl_pfeil4,
                   passe_ringzahl_pfeil5,
                   passe_ringzahl_pfeil6)
VALUES (1, 30, 1, 101, 28, 9, 9, NULL, NULL, NULL, NULL),
       (1, 30, 1, 101, 31, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 101, 47, 10, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 101, 28, 9, 6, NULL, NULL, NULL, NULL),
       (2, 30, 1, 101, 31, 10, 10, NULL, NULL, NULL, NULL),
       (2, 30, 1, 101, 47, 10, 8, NULL, NULL, NULL, NULL),
       (3, 30, 1, 101, 28, 9, 6, NULL, NULL, NULL, NULL),
       (3, 30, 1, 101, 31, 10, 8, NULL, NULL, NULL, NULL),
       (3, 30, 1, 101, 47, 10, 8, NULL, NULL, NULL, NULL),
       (4, 30, 1, 101, 28, 9, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 101, 31, 10, 10, NULL, NULL, NULL, NULL),
       (4, 30, 1, 101, 47, 10, 8, NULL, NULL, NULL, NULL),
       (1, 30, 1, 102, 441, 8, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 102, 393, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 102, 385, 8, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 102, 441, 10, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 102, 393, 10, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 102, 385, 8, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 102, 441, 10, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 102, 393, 10, 8, NULL, NULL, NULL, NULL),
       (3, 30, 1, 102, 385, 9, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 102, 441, 10, 7, NULL, NULL, NULL, NULL),
       (4, 30, 1, 102, 393, 10, 8, NULL, NULL, NULL, NULL),
       (4, 30, 1, 102, 385, 10, 8, NULL, NULL, NULL, NULL),
       (1, 30, 1, 103, 71, 8, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 103, 80, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 103, 103, 8, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 103, 71, 10, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 103, 80, 10, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 103, 103, 10, 10, NULL, NULL, NULL, NULL),
       (3, 30, 1, 103, 71, 7, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 103, 80, 10, 8, NULL, NULL, NULL, NULL),
       (3, 30, 1, 103, 103, 9, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 103, 71, 10, 10, NULL, NULL, NULL, NULL),
       (4, 30, 1, 103, 80, 10, 8, NULL, NULL, NULL, NULL),
       (4, 30, 1, 103, 103, 10, 10, NULL, NULL, NULL, NULL),
       (5, 30, 1, 103, 71, 10, 7, NULL, NULL, NULL, NULL),
       (5, 30, 1, 103, 80, 10, 8, NULL, NULL, NULL, NULL),
       (5, 30, 1, 103, 103, 10, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 104, 193, 8, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 104, 189, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 104, 249, 8, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 104, 193, 8, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 104, 189, 7, 6, NULL, NULL, NULL, NULL),
       (2, 30, 1, 104, 249, 8, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 104, 193, 10, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 104, 189, 9, 6, NULL, NULL, NULL, NULL),
       (3, 30, 1, 104, 249, 10, 7, NULL, NULL, NULL, NULL),
       (4, 30, 1, 104, 193, 10, 7, NULL, NULL, NULL, NULL),
       (4, 30, 1, 104, 189, 7, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 104, 249, 8, 7, NULL, NULL, NULL, NULL),
       (5, 30, 1, 104, 193, 10, 7, NULL, NULL, NULL, NULL),
       (5, 30, 1, 104, 189, 10, 6, NULL, NULL, NULL, NULL),
       (5, 30, 1, 104, 249, 8, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 105, 45, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 105, 72, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 105, 104, 10, 9, NULL, NULL, NULL, NULL),
       (2, 30, 1, 105, 45, 7, 6, NULL, NULL, NULL, NULL),
       (2, 30, 1, 105, 72, 10, 10, NULL, NULL, NULL, NULL),
       (2, 30, 1, 105, 104, 9, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 105, 45, 10, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 105, 72, 9, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 105, 104, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 105, 45, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 105, 72, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 105, 104, 9, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 106, 44, 10, 8, NULL, NULL, NULL, NULL),
       (1, 30, 1, 106, 78, 10, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 106, 110, 10, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 106, 44, 10, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 106, 78, 10, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 106, 110, 9, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 106, 44, 10, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 106, 78, 9, 6, NULL, NULL, NULL, NULL),
       (3, 30, 1, 106, 110, 10, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 106, 44, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 106, 78, 10, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 106, 110, 9, 7, NULL, NULL, NULL, NULL),
       (1, 30, 1, 107, 190, 10, 8, NULL, NULL, NULL, NULL),
       (1, 30, 1, 107, 216, 10, 10, NULL, NULL, NULL, NULL),
       (1, 30, 1, 107, 240, 10, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 107, 190, 8, 8, NULL, NULL, NULL, NULL),
       (2, 30, 1, 107, 216, 8, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 107, 240, 6, 6, NULL, NULL, NULL, NULL),
       (3, 30, 1, 107, 190, 10, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 107, 216, 9, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 107, 240, 10, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 107, 190, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 107, 216, 10, 9, NULL, NULL, NULL, NULL),
       (4, 30, 1, 107, 240, 10, 9, NULL, NULL, NULL, NULL),
       (1, 30, 1, 108, 180, 10, 8, NULL, NULL, NULL, NULL),
       (1, 30, 1, 108, 220, 10, 6, NULL, NULL, NULL, NULL),
       (1, 30, 1, 108, 127, 10, 7, NULL, NULL, NULL, NULL),
       (2, 30, 1, 108, 180, 10, 9, NULL, NULL, NULL, NULL),
       (2, 30, 1, 108, 220, 7, 6, NULL, NULL, NULL, NULL),
       (2, 30, 1, 108, 127, 6, 0, NULL, NULL, NULL, NULL),
       (3, 30, 1, 108, 180, 10, 9, NULL, NULL, NULL, NULL),
       (3, 30, 1, 108, 220, 9, 7, NULL, NULL, NULL, NULL),
       (3, 30, 1, 108, 127, 10, 6, NULL, NULL, NULL, NULL),
       (4, 30, 1, 108, 180, 10, 8, NULL, NULL, NULL, NULL),
       (4, 30, 1, 108, 220, 10, 8, NULL, NULL, NULL, NULL),
       (4, 30, 1, 108, 127, 10, 9, NULL, NULL, NULL, NULL)

;

/*
*Begegnungen am 1. Wettkampftag - Saison 2017 - 2018
*11	(30, 32, 40,) 71, 80, 103, 122
*
*Wettkampf 30
*
*
*0 28, 31, 47, 70
*1 441, 393, 385
*3 193, 189, 249
*5 45, 72, 104
*7 44, 78, 110
*9 190, 216, 240
*12 180, 220, 127
*
*1. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*101 0 SGes Gerstetten 	    56	53	51	56	0
*102 1 SV Schwieberdingen	50	50	53	53	0	6 : 2	2 : 0
*
*103 11 BSC Stuttgart	50	54	47	58	52
*104 3 SGes Bempflingen	52	43	49	45	48	6 : 4	2 : 0
*
*105 5 SSV Ehingen		59	51	56	54	0
*106 7 BC Magstadt 1	53	53	50	51	0	6 : 2	2 : 0
*
*107 9 SV Brochenzell	56	43	51	57	0
*108 12 SV Kirchberg	51	38	51	55	0	7 : 1	2 : 0
*
*2. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SV Schwieberdingen	54	56	51	54	55
*SV Brochenzell		52	56	56	54	53	6 : 4	2 : 0
*
*BC Magstadt 1	54	57	52	58	54
*SGes Gerstetten	57	54	53	42	51	6 : 4	2 : 0
*
*SSV Ehingen		51	56	58	57	0
*BSC Stuttgart	56	54	57	56	0	6 : 2	2 : 0
*
*SV Kirchberg		49	52	52	55	54
*SGes Bempflingen	54	51	52	50	49	7 : 3	2 : 0
*
*3. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SGes Gerstetten	50	50	51	0	0
*BSC Stuttgart	53	52	52	0	0	0 : 6	0 : 2
*
*SGes Bempflingen	56	55	50	54	51
*SV Schwieberdingen	54	53	51	55	54	4 : 6	0 : 2
*
*SV Kirchberg	51	52	53	0	0
*SSV Ehingen		53	57	56	0	0	0 : 6	0 : 2
*
*SV Brochenzell	55	52	52	52	0
*BC Magstadt 1	53	53	51	51	0	6 : 2	2 : 0
*
*
*4. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SV Schwieberdingen	56	53	56	0	0	SSV Ehingen	54	52	54	0	0	6 : 0	2 : 0
*BC Magstadt 1	57	54	54	0	0	SGes Bempflingen	46	51	46	0	0	6 : 0	2 : 0
*SV Kirchberg	58	49	55	56	0	SGes Gerstetten	52	55	54	50	0	6 : 2	2 : 0
*SV Brochenzell	54	55	54	50	57	BSC Stuttgart	51	54	58	50	51	7 : 3	2 : 0
*5. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SV Schwieberdingen	51	52	43	57	0	BC Magstadt 1	48	49	53	50	0	6 : 2	2 : 0
*SGes Bempflingen	51	47	52	0	0	SV Brochenzell	55	53	55	0	0	0 : 6	0 : 2
*BSC Stuttgart	50	49	58	52	55	SV Kirchberg	48	52	48	53	51	6 : 4	2 : 0
*SSV Ehingen	53	56	57	57	54	SGes Gerstetten	54	57	56	50	52	6 : 4	2 : 0
*6. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SGes Bempflingen	55	54	50	47	51	SGes Gerstetten	52	57	49	55	55	4 : 6	0 : 2
*BSC Stuttgart	51	51	55	58	0	SV Schwieberdingen	50	51	52	54	0	7 : 1	2 : 0
*SV Kirchberg	49	53	50	50	0	BC Magstadt 1	53	51	55	51	0	2 : 6	0 : 2
*SV Brochenzell	52	56	53	0	0	SSV Ehingen	55	58	56	0	0	0 : 6	0 : 2
*7. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SV Schwieberdingen	55	44	55	55	54	SV Kirchberg	53	53	49	55	53	7 : 3	2 : 0
*SGes Gerstetten	53	52	54	52	51	SV Brochenzell	52	58	47	56	45	6 : 4	2 : 0
*SGes Bempflingen	54	47	51	0	0	SSV Ehingen	55	49	55	0	0	0 : 6	0 : 2
*BC Magstadt 1	41	45	54	0	0	BSC Stuttgart	55	48	55	0	0	0 : 6	0 : 2
*Wettkampftag
*Saison
*
*
*
*Begegnungen am 2. Wettkampftag - Saison 2017 - 2018
*1. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SV Schwieberdingen	54	51	53	50	53	SGes Gerstetten	52	56	53	50	48	6 : 4	2 : 0
*BC Magstadt 1	52	53	55	54	0	SV Brochenzell	58	53	55	56	0	2 : 6	0 : 2
*BSC Stuttgart	47	51	44	0	0	SV Kirchberg	0	0	0	0	0	6 : 0	2 : 0
*SSV Ehingen	51	51	56	53	53	SGes Bempflingen	54	59	52	51	44	6 : 4	2 : 0
*2. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SV Schwieberdingen	54	54	51	53	58	BC Magstadt 1	54	53	58	53	52	6 : 4	2 : 0
*SGes Gerstetten	51	57	49	54	52	BSC Stuttgart	54	52	52	54	54	3 : 7	0 : 2
*SGes Bempflingen	49	35	43	0	0	SV Brochenzell	56	57	54	0	0	0 : 6	0 : 2
*SV Kirchberg	0	0	0	0	0	SSV Ehingen	59	57	56	0	0	0 : 6	0 : 2
*3. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SGes Bempflingen	48	53	54	56	47	SV Schwieberdingen	54	54	51	40	52	4 : 6	0 : 2
*BSC Stuttgart	56	55	54	56	0	BC Magstadt 1	48	45	57	53	0	6 : 2	2 : 0
*SSV Ehingen	57	53	58	57	0	SGes Gerstetten	54	54	57	42	0	6 : 2	2 : 0
*SV Brochenzell	54	53	52	0	0	SV Kirchberg	0	0	0	0	0	6 : 0	2 : 0
*4. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SGes Gerstetten	55	57	53	54	55	SV Brochenzell	52	53	55	55	55	5 : 5	1 : 1
*SGes Bempflingen	56	51	53	53	51	BSC Stuttgart	53	52	55	53	53	3 : 7	0 : 2
*SSV Ehingen	57	51	56	53	56	BC Magstadt 1	48	51	51	57	54	7 : 3	2 : 0
*SV Kirchberg	0	0	0	0	0	SV Schwieberdingen	55	54	51	0	0	0 : 6	0 : 2
*5. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SV Schwieberdingen	53	53	57	58	56	BSC Stuttgart	55	53	53	54	41	7 : 3	2 : 0
*BC Magstadt 1	55	53	52	52	55	SGes Bempflingen	50	56	51	54	49	6 : 4	2 : 0
*SV Kirchberg	0	0	0	0	0	SGes Gerstetten	49	49	51	0	0	0 : 6	0 : 2
*SV Brochenzell	52	49	46	54	52	SSV Ehingen	57	47	55	54	55	3 : 7	0 : 2
*6. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SGes Bempflingen	54	47	51	53	0	SGes Gerstetten	49	50	47	41	0	6 : 2	2 : 0
*BC Magstadt 1	57	55	51	0	0	SV Kirchberg	0	0	0	0	0	6 : 0	2 : 0
*SSV Ehingen	56	56	57	56	0	SV Schwieberdingen	56	50	55	55	0	7 : 1	2 : 0
*SV Brochenzell	52	53	52	47	0	BSC Stuttgart	55	51	54	54	0	2 : 6	0 : 2
*7. Match
*	S1	S2	S3	S4	S5		S1 	S2 	S3 	S4 	S5 	Satzpunkte	Matchpunkte
*SGes Gerstetten	49	42	53	59	46	BC Magstadt 1	50	42	55	53	53	3 : 7	0 : 2
*BSC Stuttgart	55	55	55	51	0	SSV Ehingen	57	44	56	52	0	2 : 6	0 : 2
*SV Kirchberg	0	0	0	0	0	SGes Bempflingen	50	40	50	0	0	0 : 6	0 : 2
*SV Brochenzell	51	53	54	0	0	SV Schwieberdingen	52	55	55	0	0	0 : 6	0 : 2
*
*/

INSERT INTO kampfrichter (kampfrichter_benutzer_id, kampfrichter_wettkampf_id, kampfrichter_leitend)
VALUES (4, 32, TRUE),
       (4, 32, FALSE)
;
INSERT INTO ligatabelle ( -- Recurve Ligen
  ligatabelle_veranstaltung_id,
  ligatabelle_wettkampf_tag,
  ligatabelle_mannschaft_id,
  ligatabelle_tabellenplatz,
  ligatabelle_matchpkt,
  ligatabelle_matchpkt_gegen,
  ligatabelle_satzpkt,
  ligatabelle_satzpkt_gegen
  )
VALUES
(0, 0, 101, 1, 0,0,0,0),-- initial
(0, 0, 102, 2, 0,0,0,0),
(0, 0, 103, 3, 0,0,0,0),
(0, 0, 104, 4, 0,0,0,0),
(0, 0, 105, 5, 0,0,0,0),
(0, 0, 106, 6, 0,0,0,0),
(0, 0, 107, 7, 0,0,0,0),
(0, 0, 108, 8, 0,0,0,0),
(0, 1, 105, 1, 12,2,36,14),-- Wettkampftag1
(0, 1, 103, 2, 10,4,36,24),
(0, 1, 102, 3, 10,4,34,26),
(0, 1, 107, 4, 8,6,34,24),
(0, 1, 101, 5, 6,8,28,34),
(0, 1, 106, 6, 6,8,22,30),
(0, 1, 108, 7, 4,10,23,37),
(0, 1, 104, 8, 0,14,15,43)
;


/*
 *Willkommen auf der Seite der Württembergliga des WSV
 *
 *Folgende Mannschaften nehmen im Sportjahr 2018 an der Württembergliga Bogen des WSV teil:
 *
 *    SV Brochenzell
 *    SGes Gerstetten
 *    SSV Ehingen
 *    SGes Bempflingen
 *    BSC Stuttgart
 *    BC Magstadt
 *    SV Schwieberdingen
 *    SV Kirchberg / Iller
 *
 *Termine und Austragungsorte:
 *1. Wettkampftag
 *
 *19.11.2017
 *Sporthalle, 88454 Hochdorf/Riss
 *13:30 Uhr Einschießen
 *14:00 Uhr Wettkampfbeginn
 *2. Wettkampftag
 *
 *09.12.2017
 *Franz-Baum Bogenhalle, 73642 Welzheim
 *13:30 Uhr Einschießen
 *14:00 Uhr Wettkampfbeginn
 *3. Wettkampftag
 *
 *14.01.2018
 *Bogenhalle am Schützenhaus, Gurgelbergweg, 88499 Altheim-Waldhausen
 *13:30 Uhr Einschießen
 *14:00 Uhr Wettkampfbeginn
 *4. Wettkampftag
 *
 *03.02.2018
 *Franz-Baum Bogenhalle, 73642 Welzheim
 *09:30 Uhr Einschießen
 *10:00 Uhr Wettkampfbeginn
*/INSERT INTO recht(
recht_id,
recht_name
)
VALUES (0,'CAN_READ_DEFAULT'),
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
       (17,'CAN_OPERATE_SPOTTING')
;INSERT INTO rolle(
rolle_id,
rolle_name
)
VALUES (101, 'Test-ADMIN'),
       (102, 'Test-MODERATOR'),
       (103, 'Test-USER'),
       (104, 'Test-SPORTLEITER'),
       (105, 'Test-KAMPFRICHTER'),
       (106, 'Test-LIGALEITER')
;INSERT INTO rolle_recht(
  rolle_recht_rolle_id,
  rolle_recht_recht_id
)
VALUES (101, 1), -- admin = all permissions (technical and business)
       (101, 2),
       (101, 3),
       (101, 4),
       (101, 5),
       (101, 6),
       (101, 7),
       (101, 8),
       (101, 9),
       (101, 10),
       (101, 11),
       (102, 1), -- moderator = all business read/ write permissions
       (102, 2),
       (102, 3),
       (102, 4),
       (102, 5),
       (102, 6),
       (102, 9),
       (102, 10),
       (102, 11),
       (103, 1), -- user = all business read permissions
       (103, 3),
       (103, 5),
       (103, 9),
       (104, 1), -- SPORTLEITER = all business read permissions & modify his club  (verein)
       (104, 3),
       (104, 5),
       (104, 9),
       (104, 10),
       (105, 1), -- KAMPFRICHTER = all business read permissions & modify his event  (wettkampf)
       (105, 3),
       (105, 4),
       (105, 5),
       (105, 9),
       (106, 1), -- LIGALEITER = all business read permissions & modify events and all clubs  (verein, wettkampf)
       (106, 3),
       (106, 4),
       (106, 5),
       (106, 9),
       (106, 11)
 ;
INSERT INTO benutzer_login_verlauf (benutzer_login_verlauf_benutzer_id,
                                    benutzer_login_verlauf_timestamp,
                                    benutzer_login_verlauf_login_ergebnis)
VALUES (1, '2018-10-01 11:36:17.000000', 'LOGIN_FAILED'),
       (1, '2018-10-01 11:46:17.000000', 'LOGIN_FAILED'),
       (1, '2018-10-01 11:56:17.000000', 'LOGIN_FAILED'),
       (1, '2018-10-01 11:58:17.000000', 'LOGIN_SUCCESS'),
       (2, '2018-10-01 11:36:17.000000', 'LOGIN_FAILED'),
       (2, '2018-10-01 11:40:17.000000', 'LOGIN_SUCCESS'),
       (3, '2018-10-01 11:40:17.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:41:17.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:17.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:27.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:30.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:31.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:32.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:33.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:34.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:35.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:36.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:37.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:38.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:39.000000', 'LOGIN_FAILED'),
       (3, '2018-10-01 11:42:40.000000', 'LOGIN_FAILED')
;INSERT INTO mannschaftsmitglied (
  mannschaftsmitglied_mannschaft_id,
  mannschaftsmitglied_dsb_mitglied_id,
  mannschaftsmitglied_dsb_mitglied_eingesetzt
  )
VALUES
  (101, 28, 1),
  (101, 31, 2),
  (101, 47, 2),
  (101, 70, 1),

  (102, 441, 1),
  (102, 393, 1),
  (102, 385, 1),

  (103, 30, 0),
  (103, 32, 0),
  (103, 48, 0),
  (103, 71, 2),
  (103, 80, 2),
  (103, 103, 2),
  (103, 122, 1),

  (104, 189, 2),
  (104, 193, 2),
  (104, 249, 2),

  (105, 45, 2),
  (105, 72, 2),
  (105, 104, 2),

  (106, 44, 2),
  (106, 78, 2),
  (106, 110, 2),

  (106, 44, 2),
  (106, 78, 2),
  (106, 110, 2),

  (107, 190, 2),
  (107, 216, 2),
  (107, 240, 2),

  (108, 180, 2),
  (108, 220, 2),
  (108, 127, 2)
;

--add new column veranstaltung_liga_id in TABLE veranstaltung with default value that every entry has a value and column is not null
ALTER TABLE veranstaltung
ADD COLUMN veranstaltung_liga_id DECIMAL(19,0) NOT NULL DEFAULT '2',
ADD CONSTRAINT fk_veranstaltung_liga_id FOREIGN KEY (veranstaltung_liga_id) REFERENCES liga (liga_id)
ON DELETE CASCADE; -- on deleting an entry in liga, also deletes all dependent veranstaltung

--remove the default value from the column
ALTER TABLE veranstaltung
ALTER COLUMN veranstaltung_liga_id
DROP DEFAULT;-- auto increment sequence (sq)
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
        DROP DEFAULT;/**
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
-- zur mannschaft wird noch ein sortierkriterium abgelegt, dass für den Fall des Gleichstands von match und Satzpunkte
-- die Sortierung bestimmt, später auch über einen Dialog zu setzen sein soll.

ALTER TABLE mannschaft
    ADD COLUMN
        -- an auto-incremented unique attribute, for easier internal access
        mannschaft_sortierung DECIMAL(2, 0) NOT NULL DEFAULT 0

;

/**
 * Die Liagtabelle wird durch einen view auf die Tabelle Match ersetzt.
 * die Sortierung der Tabelle wird erst beim Lesen durch Generiern einer weiteren Spalte erzeugt.
 **/


drop TABLE ligatabelle;

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


-- die Verbindung zur Liga ist ebim Anlegen/ Ändern einer Mannschaft keine Pflicht
-- der Ligaleiter oder... können die Mannschaft später der Veranstaltung zuordnen

ALTER TABLE mannschaft
    ALTER COLUMN
        -- drop a require veranstaltung - you may define and set up teams and
      -- bind them to a veranstaltung later (or by a ligaleiter)
        mannschaft_veranstaltung_id DROP NOT NULL

;

-- active attribute makes one user active or inactive
-- change email constraint to be unique to the combination of email and active

ALTER TABLE benutzer
    ADD COLUMN
        benutzer_active       BOOLEAN          NOT NULL    DEFAULT TRUE,
    DROP CONSTRAINT uc_benutzer_email;
    CREATE UNIQUE INDEX uc_benutzer_email_active ON benutzer (benutzer_email) WHERE (benutzer_active = TRUE)

;
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
INSERT INTO rolle_recht(
  rolle_recht_rolle_id,
  rolle_recht_recht_id
)
VALUES (1, 0), -- admin = all permissions (technical and business)
       (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15),
       (1, 16),
       (2, 0), -- LIGALEITER
       (2, 1),
       (2, 7),
       (2, 11),
       (2, 12),
       (3, 0), -- KAMPFRICHTER
       (3, 7),
       (3, 11),
       (4, 0), -- Ausrichter
       (4, 1),
       (4, 7),
       (4, 13),
       (4, 14),
       (4, 15),
       (5, 0), -- SPORTLEITER
       (5, 7),
       (5, 9),
       (5, 10),
       (5, 15),
       (6, 0), -- USER
       (7, 0), -- TECHNISCHER-USER
       (7, 17),
       (8, 0), -- DEFAULT
       (9, 0) --MODERATOR
 ;
INSERT INTO benutzer_rolle(
  benutzer_rolle_benutzer_id,
  benutzer_rolle_rolle_id
)
/*VALUES (1, 1), -- admin
       (2, 2), -- ligaleiter
       (3, 2),
       (4, 2), -- ligaleiter
       (4, 3),
       (5, 4), -- ausrichter
       (1, 4),
       (2, 5),
       (3, 5),
       (5, 5)
;*/
VALUES (1, 1), -- admin
       (2, 2), -- ligaleiter
       (3, 6),
       (4, 2), -- ligaleiter
       (4, 3),
       (5, 4), -- ausrichter
       (1, 4),
       (2, 5),
       (3, 5),
       (5, 5),
       (6, 8), -- default user
       (7, 2), -- ligaleiter (Team)
       (8, 5), -- sportleiter (Team)
       (9, 9) -- moderator (Team)
;

-- user
INSERT INTO rolle_recht (rolle_recht_rolle_id, rolle_recht_recht_id) VALUES (9, 7), (9, 8), (9, 17);
ALTER TABLE tablet_session ADD COLUMN access_token DECIMAL(19,0) DEFAULT 0;
INSERT INTO benutzer_rolle(
  benutzer_rolle_benutzer_id,
  benutzer_rolle_rolle_id
)

VALUES (2, 9)
;
-- inserts a Wettkampftag 0 for each Veranstaltung which has no Wettkampf entry at all
-- and inserts a Wettkampftag 0 for each Wettkampf entry that has no Wettkampftag 0
-- just the wettkampf_veranstaltungs_id will be set by circumstances. The other values are static default values.

INSERT INTO wettkampf(wettkampf_veranstaltung_id,
                      wettkampf_datum,
                      wettkampf_ort,
                      wettkampf_beginn,
                      wettkampf_tag,
                      wettkampf_disziplin_id,
                      wettkampf_wettkampftyp_id)
SELECT DISTINCT v.veranstaltung_id,
                DATE '1900-01-01' AS datum,
                ' - '             AS ort,
                ' - '             AS beginn,
                0                 AS tag,
                0                 AS disziplin,
                0                 AS typ
FROM veranstaltung v
         LEFT JOIN wettkampf w on v.veranstaltung_id = w.wettkampf_veranstaltung_id
WHERE w.wettkampf_id ISNULL
   OR v.veranstaltung_id IN
      (SELECT wettkampf_veranstaltung_id
       from wettkampf
       GROUP BY wettkampf_veranstaltung_id
       HAVING NOT min(wettkampf_tag) = 0
      )
;


ALTER TABLE mannschaftsmitglied ADD COLUMN mannschaftsmitglied_rueckennummer DECIMAL (19,0) not NULL DEFAULT 0;
INSERT INTO recht(
recht_id,
recht_name
)
VALUES
       (18,'CAN_CREATE_STAMMDATEN'),
       (19,'CAN_CREATE_SYSTEMDATEN'),
       (20,'CAN_CREATE_WETTKAMPF')
;
-- /* Annahme: leere Menge, die Rollen werden nicht verwendet */
-- select * from benutzer_rolle
-- where benutzer_rolle_rolle_id in (101,102,103,104,105,106)
-- ;

/* Zuordnung von Rechten löschen */
DELETE from rolle_recht
where rolle_recht_rolle_id in (101, 102, 103, 104, 105, 106)
;

/* Rollen löschen*/
DELETE from rolle
where rolle_id in (101, 102, 103, 104, 105, 106)
;



ALTER TABLE wettkampf ADD COLUMN kampfrichter_id DECIMAL(19,0) DEFAULT 0;
/* Prüfen ob Gero als DSB-Mitglied eingetragen ist - falls nicht, dann ergänzen*/
INSERT INTO dsb_mitglied (dsb_mitglied_vorname,
                          dsb_mitglied_nachname,
                          dsb_mitglied_geburtsdatum,
                          dsb_mitglied_nationalitaet,
                          dsb_mitglied_mitgliedsnummer,
                          dsb_mitglied_verein_id)
VALUES ('Gero', 'Gras', '1990-03-16', 'D', 'WT1009', 0)
ON CONFLICT DO NOTHING;

COMMIT;

/* neue Spalte in Tabelle einfügen mit Defaul-Wert */
ALTER TABLE benutzer
    ADD COLUMN benutzer_dsb_mitglied_id DECIMAL(19,0) DEFAULT 0;

COMMIT;

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

/* aus der bestehender Tabelle den CONTRAIN entfernen */
ALTER TABLE dsb_mitglied
    DROP CONSTRAINT IF EXISTS fk_dsb_mitglied_benutzer_id;

COMMIT;
/*später muss  mal die alte Spalte in der DSB-Mitgliedstabelle bereinigt werden
  aktuell lasse ich sie stehen um den Umbauaufwand klein zu halten. */

/* in die Benutzer-Tabelle den Constrain eintragen */
ALTER TABLE benutzer
    ADD CONSTRAINT fk_benutzer_dsb_mitglied_id
        FOREIGN KEY (benutzer_dsb_mitglied_id)
            REFERENCES dsb_mitglied (dsb_mitglied_id);CREATE SEQUENCE rNrSeq START 1;

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

DROP SEQUENCE rNrSeq;INSERT INTO recht(
recht_id,
recht_name
)
VALUES
       (21,'CAN_READ_DSBMITGLIEDER'),
       (22,'CAN_CREATE_DSBMITGLIEDER'),
       (23,'CAN_MODIFY_DSBMITGLIEDER'),
       (24,'CAN_DELETE_DSBMITGLIEDER'),
       (25, 'CAN_CREATE_MANNSCHAFT'),
       (26, 'CAN_CREATE_VEREIN_DSBMITGLIEDER'),
       (27, 'CAN_MODIFY_VEREIN_DSBMITGLIEDER')
;INSERT INTO rolle_recht(
  rolle_recht_rolle_id,
  rolle_recht_recht_id
)
VALUES (1, 18),
       (1, 17),
       (1, 21),
       (1, 22),
       (1, 23),
       (1, 24),
       (1, 25),
       (2,8),
       (2,4),
       (2,11),
       (2,12),
       (2,21),
       (2,22),
       (2,23),
       (2,25),
       (3,4),
       (3,1),
       (3,7),
       (3,21),
       (4,4),
       (4,21),
       (4,13),
       (4,14),
       (5,4),
       (5,1),
       (5,21),
       (5,26),
       (5,27);

delete from rolle_recht
where rolle_recht_rolle_id = 1 and rolle_recht_recht_id BETWEEN 9 and 14;
INSERT INTO recht(
recht_id,
recht_name
)
VALUES
       (28, 'CAN_DELETE_MANNSCHAFT'),
       (29, 'CAN_MODIFY_MANNSCHAFT')
;alter table Wettkampf
add WettkampfAusrichter DECIMAL (19,0);
alter table Wettkampf
add CONSTRAINT fk_Wettkampf_Ausrichter FOREIGN KEY (WettkampfAusrichter) REFERENCES benutzer(benutzer_id);INSERT INTO rolle_recht(
  rolle_recht_rolle_id,
  rolle_recht_recht_id
)
VALUES (1, 28),
       (1, 29),
       (2, 29);