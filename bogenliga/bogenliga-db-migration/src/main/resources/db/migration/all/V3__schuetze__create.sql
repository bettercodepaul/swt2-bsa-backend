CREATE TABLE region (
    region_ID INT,
    region_name   VARCHAR(200) NOT NULL,
    region_kuerzel   VARCHAR(10) NOT NULL,
    region_uebergeordnet ID INT, /*Verweis auf die uebergeordnete Region - bei DSB (ganz oben) Bezug zu sich selbst oder leer*/
    CONSTRAINT pk_region_ID PRIMARY KEY (region_ID)
);


CREATE TABLE verein (
    verein_ID INT,
    verein_name   VARCHAR(200) NOT NULL,
    DSB_ID   VARCHAR(200) NOT NULL, /*Identifikator beim DBS*/
    region_ID, /*Fremdschluessel zur Regionszuordnung auf Ebene "Kreis"*/
    CONSTRAINT pk_verein_ID PRIMARY KEY (verein_ID)
);


CREATE TABLE schuetze (
  schuetze_ID   INT,
  schuetze_vorname   VARCHAR(200) NOT NULL,
  schuetze_nachname   VARCHAR(200) NOT NULL,
  schuetze_jahrgang   INT NOT NULL,
  schuetze_nationalitaet VARCHAR(5) NOT NULL,
  schuetze_mitgliedsnummer VARCHAR(200) NOT NULL,
  schuetze_email VARCHAR(200) NOT NULL,
  verein_ID,/*Fremdschluessel zum Verein*/
  CONSTRAINT pk_schuetze_ID PRIMARY KEY (schuetze_ID)
);
