
-- Löschen der abhängigen Fremdschlüssel-Constraints in der Tabelle "acl"
ALTER TABLE IF EXISTS acl DROP CONSTRAINT IF EXISTS fk_acl_users_id;

-- Löschen der Tabelle "banner-stat" und der Sequenz
DROP TABLE IF EXISTS banner_stat CASCADE;
DROP SEQUENCE IF EXISTS sq_banner_stat;

-- Löschen der Tabelle "ergebniss" und der Sequenz
DROP TABLE IF EXISTS ergebniss;
-- Löschen der Tabelle "schuetze" und der Sequenz
DROP TABLE IF EXISTS schuetze CASCADE;
DROP SEQUENCE IF EXISTS sq_schuetze CASCADE;

-- Löschen der abhängigen Fremdschlüssel-Constraints
ALTER TABLE match DROP CONSTRAINT IF EXISTS fk_match_mannschaft;
ALTER TABLE passe DROP CONSTRAINT IF EXISTS fk_passe_mannschaft;
ALTER TABLE mannschaftsmitglied DROP CONSTRAINT IF EXISTS fk_mannschaftsmitglied_mannschaft;

-- Löschen der Tabelle "mannschaft" und der Sequenz
DROP TABLE IF EXISTS mannschaft CASCADE;
DROP SEQUENCE IF EXISTS sq_mannschaft;

-- Löschen der Tabelle "sites" und der Sequenz
DROP TABLE IF EXISTS sites;
DROP SEQUENCE IF EXISTS sq_sites_id;

-- Löschen der Tabelle "wettkampfdaten" und der Sequenz
DROP TABLE IF EXISTS wettkampfdaten;
DROP SEQUENCE IF EXISTS sq_wettkampfdaten;

-- Löschen der Tabelle "acl" und der Sequenz
DROP TABLE IF EXISTS acl;
DROP SEQUENCE IF EXISTS sq_acl;

-- Löschen der Tabelle "banner" und der Sequenz
DROP TABLE IF EXISTS banner;
DROP SEQUENCE IF EXISTS sq_banner;

-- Löschen der Tabelle "banner" und der Sequenz
DROP TABLE IF EXISTS banner;
DROP SEQUENCE IF EXISTS sq_banner;

-- Löschen der Tabelle "liga" und der Sequenz
DROP TABLE IF EXISTS liga CASCADE;
DROP SEQUENCE IF EXISTS sq_liga;

-- Löschen der Tabelle "users" und der Sequenz
DROP TABLE IF EXISTS users CASCADE;
DROP SEQUENCE IF EXISTS sq_users;

-- Löschen der Tabelle "saison" und der Sequenz
DROP TABLE IF EXISTS saison CASCADE;
DROP SEQUENCE IF EXISTS sq_saison;

-- Löschen der Sequenz sq_schuetze
DROP SEQUENCE IF EXISTS sq_schuetze;

-- Löschen der Sequenz sq_sites_id
DROP SEQUENCE IF EXISTS sq_sites_id;

