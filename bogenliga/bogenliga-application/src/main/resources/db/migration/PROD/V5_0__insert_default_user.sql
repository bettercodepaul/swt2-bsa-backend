INSERT INTO benutzer
    (benutzer_id,
     benutzer_email,
     benutzer_salt,
     benutzer_password,
     benutzer_using_2fa,
     benutzer_secret,
     created_at_utc,
     created_by,
     last_modified_at_utc,
     last_modified_by,
     version,
     benutzer_active,
     benutzer_dsb_mitglied_id
     )
VALUES (6,
        'ligadefault',
        '99967378dbe9dcc2f13111a8d031b34918a3e45e311961587d34bdedf897d521e4e9c1808d3f393c351d6f09648feba88436573ba5019ca15d00050bf4d5857e',
        'eac206585d6589abd083febda343f6ce00504791ef915da992dd09f695cecce06ddc92d58feb1967f95b8af2a78f0a78b79ba143870b16e14f62740d95dad594',
        false,
        null,
        '2024-03-23 10:48:56.692210',
        0,
        null,
        null,
        1,
        true,
        0
        );

INSERT INTO benutzer_rolle (
        benutzer_rolle_benutzer_id,
        benutzer_rolle_rolle_id,
        created_at_utc,
        created_by,
        last_modified_at_utc,
        last_modified_by,
        version
        )
VALUES (6,
        8,
        '2024-03-23 10:48:57.525494',
        0,
        null,
        null,
        0);

