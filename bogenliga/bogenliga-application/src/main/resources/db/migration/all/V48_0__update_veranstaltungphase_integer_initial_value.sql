update veranstaltung
set veranstaltung_phase = 2
where veranstaltung_id in (
    select wettkampf_veranstaltung_id
    from wettkampf
    where wettkampf_id in (
        select match_wettkampf_id
        from match
    )
);