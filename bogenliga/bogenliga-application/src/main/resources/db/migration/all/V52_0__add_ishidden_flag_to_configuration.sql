-- Add is Hidden Flag column to configuration table
alter table configuration
add configuration_hidden boolean default false;
-- default smtp configuration keys to be hidden
update configuration
set configuration_hidden = true
where configuration_key in ('SMTPBenutzer', 'SMTPPort', 'SMTPHost', 'SMTPEmail', 'SMTPPasswort');

