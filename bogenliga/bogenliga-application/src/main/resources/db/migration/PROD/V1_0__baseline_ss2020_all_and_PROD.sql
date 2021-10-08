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

DELETE from configuration;

INSERT INTO configuration (configuration_key, configuration_value)
VALUES
  -- Comment
  ('app.bogenliga.frontend.autorefresh.active', 'true'),
  ('app.bogenliga.frontend.autorefresh.interval', '10')
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

DELETE from rolle_recht;
DELETE from recht;

INSERT INTO recht(
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
       (8, 0) -- DEFAULT
 ;
ALTER TABLE tablet_session ADD COLUMN access_token DECIMAL(19,0) DEFAULT 0;
INSERT INTO rolle_recht(
  rolle_recht_rolle_id,
  rolle_recht_recht_id
)
VALUES  (9, 7),
        (9, 8),
        (9, 17)
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
INSERT INTO rolle_recht(
  rolle_recht_rolle_id,
  rolle_recht_recht_id
)
VALUES (1, 18),
       (1, 19),
       (1, 20);
/* Annahme: leere Menge, die Rollen werden nicht verwendet */
select * from benutzer_rolle
where benutzer_rolle_rolle_id in (101,102,103,104,105,106)
;

/* Zuordnung von Rechten löschen */
DELETE from rolle_recht
where rolle_recht_rolle_id in (101, 102, 103, 104, 105, 106)
;

/* Rollen löschen*/
DELETE from rolle
where rolle_id in (101, 102, 103, 104, 105, 106)
;
ALTER TABLE wettkampf ADD COLUMN kampfrichter_id DECIMAL(19,0) DEFAULT 0;

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
;

INSERT INTO rolle_recht(
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
;

alter table Wettkampf
add WettkampfAusrichter DECIMAL (19,0);

alter table Wettkampf
add CONSTRAINT fk_Wettkampf_Ausrichter FOREIGN KEY (WettkampfAusrichter) REFERENCES benutzer(benutzer_id);INSERT INTO rolle_recht(
  rolle_recht_rolle_id,
  rolle_recht_recht_id
)
VALUES (1, 28),
       (1, 29),
       (2, 29);