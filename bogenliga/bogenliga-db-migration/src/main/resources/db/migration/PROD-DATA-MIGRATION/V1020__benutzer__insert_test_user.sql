SET search_path = 'prod'
;

INSERT INTO benutzer(benutzer_id,
                     benutzer_email,
                     benutzer_salt,
                     benutzer_password)
VALUES
  -- password = admin
  (2,
   'admin@bogenliga.de',
   'a9a2ef3c5a023acd2fc79ebd9c638e0ebb62db9c65fa42a6ca43d5d957a4bdf5413c8fc08ed8faf7204ba0fd5805ca638220b84d07c0690aed16ab3a2413142d',
   '0d31dec64a1029ea473fb10628bfa327be135d34f8013e7238f32c019bd0663a7feca101fd86ccee1339e79db86c5494b6e635bb5e21456f40a6722952c53079'),
  -- password = moderator
  (3,
   'moderator@bogenliga.de',
   'dbed56d612f8fc8397a79a9e63cc67236ac63027e092adda7b02cbe7c65a4916683a572d71d3cefbcdcf86ee42136b1882ce75b189b1fe3a1457cc72ced3c6ea',
   '3afca75fad3ea4e11e3e1f4274221acb4f0a833e765b21c87098c18c9ebea67eec16f849cffc4f0010ea0f6879d0a8b88c4cfd64abfcd4762cf5c123e87f0a45'),
  -- password = user
  (4,
   'user@bogenliga.de',
   '99967378dbe9dcc2f13111a8d031b34918a3e45e311961587d34bdedf897d521e4e9c1808d3f393c351d6f09648feba88436573ba5019ca15d00050bf4d5857e',
   'eac206585d6589abd083febda343f6ce00504791ef915da992dd09f695cecce06ddc92d58feb1967f95b8af2a78f0a78b79ba143870b16e14f62740d95dad594')
;



INSERT INTO benutzer_rolle(benutzer_rolle_benutzer_id,
                           benutzer_rolle_rolle_id)
VALUES (2, 1),
       (3, 1),
       (4, 1) -- admin
;
