/**
 * Tabelle zum Abbilden einer n:m-Beziehung
 *
 * Nur ein Kampfrichter kann eine leitende Rolle bei einem Wettkampf einnehmen.
 */
CREATE TABLE kampfrichter (
  kampfrichter_dsb_mitglied_id  DECIMAL(19,0) NOT NULL,
  kampfrichter_wettkampf_id     DECIMAL(19,0) NOT NULL,
  kampfrichter_leitend          BOOLEAN       NOT NULL  DEFAULT FALSE,

  CONSTRAINT pk_kampfrichter_wettkampf_leitend PRIMARY KEY (kampfrichter_wettkampf_id, kampfrichter_dsb_mitglied_id, kampfrichter_leitend),

    -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_kampfrichter_dsb_mitglied FOREIGN KEY (kampfrichter_dsb_mitglied_id) REFERENCES dsb_mitglied (dsb_mitglied_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_kampfrichter_wettkampf FOREIGN KEY (kampfrichter_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
    ON DELETE CASCADE
);