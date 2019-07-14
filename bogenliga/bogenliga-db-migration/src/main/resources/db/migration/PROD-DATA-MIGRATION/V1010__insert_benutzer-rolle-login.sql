-- wir brauchen einen User um ihn für alle ehemaligen Wettkämpfe als Referenzperson anzulegen
-- er ist dann alles: Ligaleiter, Kampfrichter, Sportleiter, Admin...
-- und er heisst Gero ;-))

--ggf. müssen wir hier salt und passwort nochmal korrigieren..

SET search_path = 'prod'
;

INSERT INTO benutzer(
benutzer_id,
benutzer_email,
benutzer_salt,
benutzer_password
)
VALUES
-- password = admin
       (1,
        'Gero.Gras@bogenliga.de',
        'a9a2ef3c5a023acd2fc79ebd9c638e0ebb62db9c65fa42a6ca43d5d957a4bdf5413c8fc08ed8faf7204ba0fd5805ca638220b84d07c0690aed16ab3a2413142d',
        '0d31dec64a1029ea473fb10628bfa327be135d34f8013e7238f32c019bd0663a7feca101fd86ccee1339e79db86c5494b6e635bb5e21456f40a6722952c53079')
;

INSERT INTO benutzer_rolle(
  benutzer_rolle_benutzer_id,
  benutzer_rolle_rolle_id
)
VALUES (1, 1) -- admin
;


INSERT INTO benutzer_login_verlauf (benutzer_login_verlauf_benutzer_id,
                                    benutzer_login_verlauf_timestamp,
                                    benutzer_login_verlauf_login_ergebnis)
VALUES (1, '2018-10-01 11:36:17.000000', 'LOGIN_FAILED'),
       (1, '2018-10-01 11:58:17.000000', 'LOGIN_SUCCESS')
;

