DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM benutzer WHERE benutzer_id = 10) THEN
        INSERT INTO benutzer (
            benutzer_id,
            benutzer_email,
            benutzer_salt,
            benutzer_password,
            benutzer_dsb_mitglied_id
        )
        VALUES (
            10,
            'HSRT-Test@bogenliga.de',
            'c44e0172f2515ebf0234bdd0b4d315d87ce82d3a0f49d2e1a90340793b5b2a6e842e88d2cdba8e8c10f8eb6ab6bbdf6513faa37974a3bbe63d5492a79af2b149',
            'df56be8bb98411714f09d62742e27349d9c93cd5c92f7e40ebdb8925e0d6aa368427e32187ee20efea9f2bb71714efa30a95e511eb7821daaa10b5097128eee7',
            '1000'
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM benutzer WHERE benutzer_id = 11) THEN
        INSERT INTO benutzer (
            benutzer_id,
            benutzer_email,
            benutzer_salt,
            benutzer_password,
            benutzer_dsb_mitglied_id
        )
        VALUES (
            11,
            'HSRT-Test2@bogenliga.de',
            '8f80b5d6214975bd53a5563caf41a1e1bff02b1d2ae24f3eef329e2a436aeba97e6745cc911878878347bf98f6c132cd5d23bc67001d392f71fcb065bf9da003',
            '9485c2baf5590248734252fc0e79de9b4bdbbc2f796a8fd49840b3cdfd7a0f2ac83c3184e030a9e8c0c054df0469d3475dadfdc8d2f539d96fb7321308893726',
            '1000'
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM benutzer WHERE benutzer_id = 12) THEN
        INSERT INTO benutzer (
            benutzer_id,
            benutzer_email,
            benutzer_salt,
            benutzer_password,
            benutzer_dsb_mitglied_id
        )
        VALUES (
            12,
            'HSRT-Test3@bogenliga.de',
            '0e39057bc2c9a6c0ff2fb24d0bfde0aa4dfe07b1c9775a247452b06702dc85c399db5d93f3599d272f3e9896af08a0814468b11e852400eca27475f0a4ba7b92',
            'd98698f0feba34fea43ceaa7bf347566f84b1d2ffcfa01203d10b296dcf3a5d7f834dc25632d7ec7b068bbd498966023ef4b97926678d6a11c12285cf9d8e8b4',
            '1000'
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM benutzer_rolle WHERE benutzer_rolle_benutzer_id = 10 AND benutzer_rolle_rolle_id = 2) THEN
        INSERT INTO benutzer_rolle (
            benutzer_rolle_benutzer_id,
            benutzer_rolle_rolle_id
        )
        VALUES (10, 2);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM benutzer_rolle WHERE benutzer_rolle_benutzer_id = 11 AND benutzer_rolle_rolle_id = 4) THEN
        INSERT INTO benutzer_rolle (
            benutzer_rolle_benutzer_id,
            benutzer_rolle_rolle_id
        )
        VALUES (11, 4);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM benutzer_rolle WHERE benutzer_rolle_benutzer_id = 12 AND benutzer_rolle_rolle_id = 5) THEN
        INSERT INTO benutzer_rolle (
            benutzer_rolle_benutzer_id,
            benutzer_rolle_rolle_id
        )
        VALUES (12, 5);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM benutzer WHERE benutzer_id = 13) THEN
        INSERT INTO benutzer (
            benutzer_id,
            benutzer_email,
            benutzer_salt,
            benutzer_password,
            benutzer_dsb_mitglied_id
        )
        VALUES (
            13,
            'HSRT-Test4@bogenliga.de',
            'aec8c4d7c59b90adb69bf59f9a0cf8cb737c9f133327a3622b3139c86cab1e01ca67b2e429141475fd806d81503a4382f188da909a687431f9242e7f8850fc67',
            'd840f148043b95dc5bf077abdf4d0a67a7d24e057c7285d669383a3fbf5dd810aa423695c3e139d241b14f24ba16b8ffe9c3099e26857d2638487a2d56d55877',
            '1000'
        );
    END IF;

    IF NOT EXISTS (SELECT 1 FROM benutzer_rolle WHERE benutzer_rolle_benutzer_id = 13 AND benutzer_rolle_rolle_id = 2) THEN
        INSERT INTO benutzer_rolle (
            benutzer_rolle_benutzer_id,
            benutzer_rolle_rolle_id
        )
        VALUES (13, 2);
    END IF;
END $$;


