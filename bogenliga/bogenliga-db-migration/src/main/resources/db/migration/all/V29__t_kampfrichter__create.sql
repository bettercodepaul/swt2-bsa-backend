/*
 * general conventions:
 * - lower case
 * - use "_"
 * - use table name for column prefixes
 *
 * example:
 * table name = "user"
 * column prefix = "user_"
 */

CREATE TABLE kampfrichter (
  kampfrichter_dsb_mitglied_id  DECIMAL(19,0) NOT NULL,
  kampfrichter_wettkampf_id     DECIMAL(19,0) NOT NULL,
  kampfrichter_leitend          BOOLEAN NOT NULL,

  -- foreign key (fk)
  -- schema: fk_{current table name}_{foreign key origin table name}
  CONSTRAINT fk_kampfrichter_dsb_mitglied FOREIGN KEY (kampfrichter_dsb_mitglied_id) REFERENCES dsb_mitglied (dsb_mitglied_id),
  CONSTRAINT fk_kampfrichter_wettkampf FOREIGN KEY (kampfrichter_wettkampf_id) REFERENCES wettkampf (wettkampf_id)
);