CREATE OR REPLACE FUNCTION update_row_version()
  returns TRIGGER AS $update_row_version_trigger$
BEGIN
  new.version = old.version + 1;
  return new;
END
$update_row_version_trigger$
LANGUAGE plpgsql;
