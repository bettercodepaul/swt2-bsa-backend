DO
$$
DECLARE
    max_act_val BIGINT;

BEGIN

    -- Alter Mannschaftsmitglied Sequence
    max_act_val := (SELECT mannschaftsmitglied_id FROM mannschaftsmitglied WHERE mannschaftsmitglied_id = (select max(mannschaftsmitglied_id) FROM mannschaftsmitglied));
    PERFORM setval('mannschaftsmitglied_id', max_act_val);

    -- Alter DsbMitglied Sequence
    max_act_val := (SELECT dsb_mitglied_id FROM dsb_mitglied WHERE dsb_mitglied_id = (select max(dsb_mitglied_id) FROM dsb_mitglied));
    PERFORM setval('sq_dsb_mitglied_id', max_act_val);

    -- Alter Mannschaft Sequence
    max_act_val := (SELECT mannschaft_id FROM mannschaft WHERE mannschaft_id = (select max(mannschaft_id) FROM mannschaft));
    PERFORM setval('sq_mannschaft_id', max_act_val);

    -- Alter Verein Sequence
    max_act_val := (SELECT verein_id FROM verein WHERE verein_id = (select max(verein_id) FROM verein));
    PERFORM setval('sq_verein_id', max_act_val);

    -- Alter Wettkampf Sequence
    max_act_val := (SELECT wettkampf_id FROM wettkampf WHERE wettkampf_id = (select max(wettkampf_id) FROM wettkampf));
    PERFORM setval('sq_wettkampf_id', max_act_val);

    -- Alter Veranstaltung Sequence
    max_act_val := (SELECT veranstaltung_id FROM veranstaltung WHERE veranstaltung_id = (select max(veranstaltung_id) FROM veranstaltung));
    PERFORM setval('sq_veranstaltung_id', max_act_val);
END
$$
    LANGUAGE plpgsql;
