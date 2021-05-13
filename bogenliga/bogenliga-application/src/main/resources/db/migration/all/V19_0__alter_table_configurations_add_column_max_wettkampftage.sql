INSERT INTO configuration (configuration_key, configuration_value)
VALUES('MaxWettkampfTage','4')
ON CONFLICT (configuration_key) DO
    NOTHING;