ALTER TABLE dsb_mitglied
    ADD COLUMN IF NOT EXISTS dsb_mitglied_beitrittsdatum date NOT NULL DEFAULT current_date;
