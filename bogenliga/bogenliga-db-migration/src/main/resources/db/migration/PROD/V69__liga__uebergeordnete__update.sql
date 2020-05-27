UPDATE liga
SET liga_uebergeordnet = liga_id
WHERE liga_uebergeordnet IS NULL;