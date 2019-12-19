-- Tabelle erfüllt folgende Aufgaben: Ligaleiter generiert authentificationCode für den Wettkampftag
-- Spotter meldet sich am Tablet mit dem authentificationCode an
-- AuthentificationCode berechtigt schreiben in die Datenbank

CREATE TABLE AccessTabletToken (
authentificationCode varchar(50), -- automatisch generiert nach dem auf "+" Gedrückt wird in der Tabletanmeldung
aktiv bool, 
);

--erstellt am 19.12.2019, @AnGsmn