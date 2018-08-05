CREATE TABLE t_configuration (
  configuration_key VARCHAR(200) NOT NULL,
  configuration_value VARCHAR(200) NOT NULL,
  CONSTRAINT pk_configuration_key PRIMARY KEY (configuration_key)
);
