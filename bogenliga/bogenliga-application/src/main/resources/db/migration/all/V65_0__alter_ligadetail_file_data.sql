ALTER TABLE liga
ADD COLUMN liga_file_base64 varchar(10485760) DEFAULT '',
ADD COLUMN liga_file_name varchar (200) DEFAULT '',
ADD COLUMN liga_file_type varchar (202) DEFAULT '';