CREATE TABLE IF NOT EXISTS einstellungen (
    einstellungen_id int NOT NULL PRIMARY KEY,
    einstellungen_key varchar(255) NOT NULL UNIQUE,
    einstellungen_value varchar(255) NOT NULL)




