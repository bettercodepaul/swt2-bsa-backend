UPDATE configuration
SET configuration_regex = '^(true|false)$'
WHERE configuration_key = 'app.bogenliga.frontend.autorefresh.active';

UPDATE configuration
SET configuration_regex = '^[1-9]$|^[1-9][0-9]$|^(100)$'
WHERE configuration_key = 'app.bogenliga.frontend.autorefresh.interval';

UPDATE configuration
SET configuration_regex = '[1-4]'
WHERE configuration_key = 'MaxWettkampfTage';

UPDATE configuration
SET configuration_regex = '^2[01][0-9]{2}'
WHERE configuration_key = 'aktives-Sportjahr';