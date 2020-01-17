SET search_path = 'prod'
;


DELETE from configuration;

INSERT INTO configuration (configuration_key, configuration_value)
VALUES
  -- Comment
  ('app.bogenliga.frontend.autorefresh.active', 'true'),
  ('app.bogenliga.frontend.autorefresh.interval', '10')
;
