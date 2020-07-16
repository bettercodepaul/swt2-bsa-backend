

INSERT INTO benutzer(
benutzer_id,
benutzer_email,
benutzer_salt,
benutzer_password,
benutzer_dsb_mitglied_id
)
VALUES
-- password = user
(2,
  'ligadefault',
  '99967378dbe9dcc2f13111a8d031b34918a3e45e311961587d34bdedf897d521e4e9c1808d3f393c351d6f09648feba88436573ba5019ca15d00050bf4d5857e',
  'eac206585d6589abd083febda343f6ce00504791ef915da992dd09f695cecce06ddc92d58feb1967f95b8af2a78f0a78b79ba143870b16e14f62740d95dad594',
  null)
;


INSERT INTO benutzer_rolle(
  benutzer_rolle_benutzer_id,
  benutzer_rolle_rolle_id
)
VALUES (2, 8) -- default
;


INSERT INTO benutzer_login_verlauf (benutzer_login_verlauf_benutzer_id,
                                    benutzer_login_verlauf_timestamp,
                                    benutzer_login_verlauf_login_ergebnis)
VALUES (1, '2018-10-01 11:36:17.000000', 'LOGIN_FAILED'),
       (1, '2018-10-01 11:58:17.000000', 'LOGIN_SUCCESS')
;

