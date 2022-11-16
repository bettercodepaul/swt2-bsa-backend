INSERT INTO configuration (configuration_key, configuration_value)
VALUES('aktives-Sportjahr','2022')
ON CONFLICT (configuration_key) DO
    NOTHING;