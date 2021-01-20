-- Since kampfrichter have their own table we no longer need a kampfrichter_id in wettkampf.
-- It can be deleted without worries because the kampfrichter_id hasn't been used so far anyway.

ALTER TABLE wettkampf
    DROP COLUMN kampfrichter_id
;