BEGIN;

-- Insert default values only if the keys don't exist
INSERT INTO configuration(configuration_key, configuration_value)
VALUES ('OLDDBBenutzer', 'default')
ON CONFLICT (configuration_key) DO NOTHING;

INSERT INTO configuration(configuration_key, configuration_value)
VALUES ('OLDDBPassword', 'default')
ON CONFLICT (configuration_key) DO NOTHING;

INSERT INTO configuration(configuration_key, configuration_value)
VALUES ('OLDDBHost', 'default')
ON CONFLICT (configuration_key) DO NOTHING;

INSERT INTO configuration(configuration_key, configuration_value)
VALUES ('OLDDBPort', 'default')
ON CONFLICT (configuration_key) DO NOTHING;

INSERT INTO configuration(configuration_key, configuration_value)
VALUES ('OLDDBName', 'default')
ON CONFLICT (configuration_key) DO NOTHING;

-- Update values only if the keys exist
UPDATE configuration SET configuration_value = '*****' WHERE configuration_key = 'OLDDBBenutzer';
UPDATE configuration SET configuration_value = '*****' WHERE configuration_key = 'OLDDBPassword';
UPDATE configuration SET configuration_value = '*****' WHERE configuration_key = 'OLDDBHost';
UPDATE configuration SET configuration_value = '*****' WHERE configuration_key = 'OLDDBPort';
UPDATE configuration SET configuration_value = '*****' WHERE configuration_key = 'OLDDBName';

COMMIT;
