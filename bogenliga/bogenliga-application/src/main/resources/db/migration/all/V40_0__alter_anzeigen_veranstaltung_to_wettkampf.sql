ALTER table anzeigen
    DROP column veranstaltungs_id,
    add column wettkampf_id NUMERIC(19),
    add CONSTRAINT fk_wettkampf_id FOREIGN KEY (wettkampf_id) REFERENCES wettkampf (wettkampf_id);