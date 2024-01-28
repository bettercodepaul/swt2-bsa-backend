BEGIN;

INSERT INTO configuration(configuration_key, configuration_value) VALUES ('OLDDBBenutzer', 'default');
INSERT INTO configuration(configuration_key, configuration_value) VALUES ('OLDDBPassword', 'default');
INSERT INTO configuration(configuration_key, configuration_value) VALUES ('OLDDBHost', 'default');
INSERT INTO configuration(configuration_key, configuration_value) VALUES ('OLDDBPort', 'default');
INSERT INTO configuration(configuration_key, configuration_value) VALUES ('OLDDBName', 'default');

COMMIT;
