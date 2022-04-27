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
(8, 1, 'Regionalliga Südwest', 1, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(9, 1, 'Relegation Landesliga Nord', 3, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(10, 1, 'Relegation Landesliga Süd', 4, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(11, 3, 'Bezirksoberliga Neckar', 10, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(12, 3, 'Bezirksoberliga Oberschwaben', 10, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(13, 3, 'Bezirksliga A Neckar', 11, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(14, 3, 'Bezirksliga A Oberschwaben', 12, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(15, 3, 'Bezirksliga B Oberschwaben', 14, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(16, 1, 'Württembergliga Compound', 1, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(17, 1, 'Landesliga A Compound', 16, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(18, 1, 'Landesliga B Compound', 17, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);

INSERT INTO liga (liga_id, liga_region_id, liga_name, liga_uebergeordnet, liga_verantwortlich, created_at_utc, created_by, last_modified_at_utc, last_modified_by,version)
VALUES
(19, 1, 'Württembergliga Compound Finale', 1, 1, '2021-01-28 20:49:06.337792', 0, null, null, 0);
