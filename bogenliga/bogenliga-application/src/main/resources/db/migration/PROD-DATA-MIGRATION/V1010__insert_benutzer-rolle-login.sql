-- wir brauchen einen User um ihn für alle ehemaligen Wettkämpfe als Referenzperson anzulegen
-- er ist dann alles: Ligaleiter, Kampfrichter, Sportleiter, Admin...
-- und er heisst Gero ;-))

--ggf. müssen wir hier salt und passwort nochmal korrigieren..


DELETE from benutzer_rolle;
DELETE from benutzer_login_verlauf;
DELETE from benutzer;

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
 '0d31dec64a1029ea473fb10628bfa327be135d34f8013e7238f32c019bd0663a7feca101fd86ccee1339e79db86c5494b6e635bb5e21456f40a6722952c53079'),
-- password = user
(2,
  'ligadefault',
  '99967378dbe9dcc2f13111a8d031b34918a3e45e311961587d34bdedf897d521e4e9c1808d3f393c351d6f09648feba88436573ba5019ca15d00050bf4d5857e',
  'eac206585d6589abd083febda343f6ce00504791ef915da992dd09f695cecce06ddc92d58feb1967f95b8af2a78f0a78b79ba143870b16e14f62740d95dad594')
;


INSERT INTO benutzer_rolle(
  benutzer_rolle_benutzer_id,
  benutzer_rolle_rolle_id
)
VALUES (1, 1), -- admin
      (2, 8) -- default
;


INSERT INTO benutzer_login_verlauf (benutzer_login_verlauf_benutzer_id,
                                    benutzer_login_verlauf_timestamp,
                                    benutzer_login_verlauf_login_ergebnis)
VALUES (1, '2018-10-01 11:36:17.000000', 'LOGIN_FAILED'),
       (1, '2018-10-01 11:58:17.000000', 'LOGIN_SUCCESS')
;

