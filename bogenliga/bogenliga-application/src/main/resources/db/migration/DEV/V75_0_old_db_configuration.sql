BEGIN;

INSERT INTO configuration(configuration_key, configuration_value) VALUES ('OLDDBBenutzer', 'default');
INSERT INTO configuration(configuration_key, configuration_value) VALUES ('OLDDBPassword', 'default');
INSERT INTO configuration(configuration_key, configuration_value) VALUES ('OLDDBHost', 'default');
INSERT INTO configuration(configuration_key, configuration_value) VALUES ('OLDDBPort', 'default');
INSERT INTO configuration(configuration_key, configuration_value) VALUES ('OLDDBName', 'default');


UPDATE configuration SET configuration_value = '*****' WHERE configuration_key = 'OLDDBBenutzer';
UPDATE configuration SET configuration_value = '*****' WHERE configuration_key = 'OLDDBPassword';
UPDATE configuration SET configuration_value = '*****' WHERE configuration_key = 'OLDDBHost';
UPDATE configuration SET configuration_value = '*****' WHERE configuration_key = 'OLDDBPort';
UPDATE configuration SET configuration_value = '*****' WHERE configuration_key = 'OLDDBName';

COMMIT;