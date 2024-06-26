/*
Add Role Can create my liga for ligaleiter to add lowest liga
*/

INSERT INTO recht(
    recht_id,
    recht_name
)
VALUES(
       35,'CAN_CREATE_MY_LIGA'
      );

INSERT INTO rolle_recht(
    rolle_recht_rolle_id,
    rolle_recht_recht_id
)
VALUES (
        2,35
       )

