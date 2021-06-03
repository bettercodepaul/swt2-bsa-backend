-- add new leagues to test data table

INSERT INTO liga (
                    liga_id,
                    liga_region_id,
                    liga_name,
                    liga_uebergeordnet,
                    liga_verantwortlich,
                    created_at_utc,
                    created_by,
                    last_modified_at_utc,
                    last_modified_by,
                    version)
VALUES
(8, null, 'Regionalliga Südwest', 1, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(9, null, 'Relegation Landesliga Nord', 3, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(10, null, 'Relegation Landesliga Süd', 4, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(11, null, 'Bezirksoberliga Neckar', 10, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(12, null, 'Bezirksoberliga Oberschwaben', 10, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(13, null, 'Bezirksliga A Neckar', 11, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(14, null, 'Bezirksliga A Oberschwaben', 12, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(15, null, 'Bezirksliga B Oberschwaben', 14, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(16, null, 'Württembergliga Compound', 1, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(17, null, 'Landesliga A Compound', 16, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(18, null, 'Landesliga B Compound', 17, 1, null, 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(19, null, 'Württembergliga Compound Finale', 1, 1, null, 0, null, null, 0);
