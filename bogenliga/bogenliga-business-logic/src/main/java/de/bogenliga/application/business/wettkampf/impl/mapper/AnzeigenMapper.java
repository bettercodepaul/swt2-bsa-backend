package de.bogenliga.application.business.wettkampf.impl.mapper;

import de.bogenliga.application.business.wettkampf.api.types.AnzeigenDO;
import de.bogenliga.application.business.wettkampf.impl.entity.AnzeigenBE;
import de.bogenliga.application.common.time.DateProvider;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;

public class AnzeigenMapper {

    private AnzeigenMapper(){}

    public static final Function<AnzeigenBE, AnzeigenDO> toAnzeigenDO = anzeigenBE -> {
        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(anzeigenBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedUtc = DateProvider.convertTimestamp(anzeigenBE.getLastModifiedAtUtc());

        final Long id = anzeigenBE.getId();
        final String physischeBildschirmId = anzeigenBE.getPhysischeBildschirmId();
        final String tableTyp = anzeigenBE.getTableTyp();
        final Long veranstaltungsId = anzeigenBE.getVeranstaltungsId();

        return new AnzeigenDO(id, physischeBildschirmId, tableTyp, veranstaltungsId, createdAtUtc,
                anzeigenBE.getCreatedByUserId(),
                lastModifiedUtc,
                anzeigenBE.getLastModifiedByUserId(),
                anzeigenBE.getVersion());
    };

    public static final Function<AnzeigenDO, AnzeigenBE> toAnzeigenBE = anzeigenDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(anzeigenDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(anzeigenDO.getLastModifiedAtUtc());

        AnzeigenBE anzeigenBE = new AnzeigenBE();
        anzeigenBE.setId(anzeigenDO.getId());
        anzeigenBE.setPhysischeBildschirmId(anzeigenDO.getPhysischeBildschirmId());
        anzeigenBE.setTableTyp(anzeigenDO.getTableTyp());
        anzeigenBE.setVeranstaltungsId(anzeigenDO.getVeranstaltungsId());

        anzeigenBE.setCreatedAtUtc(createdAtUtcTimestamp);
        anzeigenBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        anzeigenBE.setCreatedByUserId(anzeigenDO.getCreatedByUserId());
        anzeigenBE.setLastModifiedByUserId(anzeigenDO.getLastModifiedByUserId());
        anzeigenBE.setVersion(anzeigenDO.getVersion());

        return anzeigenBE;
    };

}

