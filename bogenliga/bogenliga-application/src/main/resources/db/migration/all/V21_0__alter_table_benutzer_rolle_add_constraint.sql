ALTER TABLE benutzer_rolle
	ADD CONSTRAINT uniquectm_ben_rol UNIQUE (benutzer_rolle_benutzer_id, benutzer_rolle_rolle_id);