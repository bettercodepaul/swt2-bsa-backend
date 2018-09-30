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
