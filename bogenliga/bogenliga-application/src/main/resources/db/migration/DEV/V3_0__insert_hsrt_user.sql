INSERT INTO benutzer(
benutzer_id,
benutzer_email,
benutzer_salt,
benutzer_password,
benutzer_dsb_mitglied_id
)
VALUES
-- password = mki4HSRT
       (10,
        'HSRT-Test@bogenliga.de',
        'c44e0172f2515ebf0234bdd0b4d315d87ce82d3a0f49d2e1a90340793b5b2a6e842e88d2cdba8e8c10f8eb6ab6bbdf6513faa37974a3bbe63d5492a79af2b149',
        'df56be8bb98411714f09d62742e27349d9c93cd5c92f7e40ebdb8925e0d6aa368427e32187ee20efea9f2bb71714efa30a95e511eb7821daaa10b5097128eee7',
       '0'),
-- password = mki4HSRT
       (11,
        'HSRT-Test2@bogenliga.de',
        '8f80b5d6214975bd53a5563caf41a1e1bff02b1d2ae24f3eef329e2a436aeba97e6745cc911878878347bf98f6c132cd5d23bc67001d392f71fcb065bf9da003',
        '9485c2baf5590248734252fc0e79de9b4bdbbc2f796a8fd49840b3cdfd7a0f2ac83c3184e030a9e8c0c054df0469d3475dadfdc8d2f539d96fb7321308893726',
       '0'),
-- password = mki4HSRT
       (12,
        'HSRT-Test3@bogenliga.de',
        '0e39057bc2c9a6c0ff2fb24d0bfde0aa4dfe07b1c9775a247452b06702dc85c399db5d93f3599d272f3e9896af08a0814468b11e852400eca27475f0a4ba7b92',
        'd98698f0feba34fea43ceaa7bf347566f84b1d2ffcfa01203d10b296dcf3a5d7f834dc25632d7ec7b068bbd498966023ef4b97926678d6a11c12285cf9d8e8b4',
       '0')
;
INSERT INTO benutzer_rolle(
  benutzer_rolle_benutzer_id,
  benutzer_rolle_rolle_id
)
VALUES (10, 2),
       (11, 4), 
       (12, 5)
;


