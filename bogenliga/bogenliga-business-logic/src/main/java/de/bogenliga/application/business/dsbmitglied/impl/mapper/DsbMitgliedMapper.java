package de.bogenliga.application.business.dsbmitglied.impl.mapper;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedWithoutVereinsnameBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the dsbmitglied DataObjects and BusinessEntities.
 */
public class DsbMitgliedMapper implements ValueObjectMapper {

    private static DsbMitgliedDO convertToDsbMitgliedDO(
            Long id, String vorname, String nachname, Date geburtsdatum, String nationalitaet,
            String mitgliedsnummer, Long vereinsId, String vereinName, Long userId, Date beitrittsdatum,
            Long createdByUserId, Long lastModifiedByUserId, Long version, OffsetDateTime createdAtUtc,
            OffsetDateTime lastModifiedAtUtc, Boolean kampfrichter) {

        return new DsbMitgliedDO(id, vorname, nachname, geburtsdatum, nationalitaet, mitgliedsnummer,
                vereinsId, vereinName, userId, createdAtUtc, createdByUserId, lastModifiedAtUtc,
                lastModifiedByUserId, version, kampfrichter, beitrittsdatum);
    }

    /**
     * Converts a {@link DsbMitgliedBE} to a {@link DsbMitgliedDO}
     */
    public static final Function<DsbMitgliedBE, DsbMitgliedDO> toDsbMitgliedDO = be -> {
        return convertToDsbMitgliedDO(
                be.getDsbMitgliedId(), be.getDsbMitgliedVorname(), be.getDsbMitgliedNachname(),
                be.getDsbMitgliedGeburtsdatum(), be.getDsbMitgliedNationalitaet(), be.getDsbMitgliedMitgliedsnummer(),
                be.getDsbMitgliedVereinsId(), be.getDsbMitgliedVereinName(), be.getDsbMitgliedUserId(),
                be.getDsbMitgliedBeitrittsdatum(), be.getCreatedByUserId(), be.getLastModifiedByUserId(),
                be.getVersion(), DateProvider.convertTimestamp(be.getCreatedAtUtc()),
                DateProvider.convertTimestamp(be.getLastModifiedAtUtc()), false);
    };

    /**
     * Converts a {@link DsbMitgliedWithoutVereinsnameBE} to a {@link DsbMitgliedDO}
     */
    public static final Function<DsbMitgliedWithoutVereinsnameBE, DsbMitgliedDO> toDsbMitgliedDOWithoutVereinsname = be -> {
        return convertToDsbMitgliedDO(
                be.getDsbMitgliedId(), be.getDsbMitgliedVorname(), be.getDsbMitgliedNachname(),
                be.getDsbMitgliedGeburtsdatum(), be.getDsbMitgliedNationalitaet(), be.getDsbMitgliedMitgliedsnummer(),
                be.getDsbMitgliedVereinsId(), null, be.getDsbMitgliedUserId(), be.getDsbMitgliedBeitrittsdatum(),
                be.getCreatedByUserId(), be.getLastModifiedByUserId(), be.getVersion(),
                DateProvider.convertTimestamp(be.getCreatedAtUtc()), DateProvider.convertTimestamp(be.getLastModifiedAtUtc()),
                false);
    };

    private static <T extends DsbMitgliedWithoutVereinsnameBE> T convertToDsbMitgliedBECommon(
            DsbMitgliedDO dsbMitgliedDO, T dsbMitgliedBE) {
        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getLastModifiedAtUtc());

        dsbMitgliedBE.setDsbMitgliedId(dsbMitgliedDO.getId());
        dsbMitgliedBE.setDsbMitgliedVorname(dsbMitgliedDO.getVorname());
        dsbMitgliedBE.setDsbMitgliedNachname(dsbMitgliedDO.getNachname());
        dsbMitgliedBE.setDsbMitgliedGeburtsdatum(dsbMitgliedDO.getGeburtsdatum());
        dsbMitgliedBE.setDsbMitgliedNationalitaet(dsbMitgliedDO.getNationalitaet());
        dsbMitgliedBE.setDsbMitgliedMitgliedsnummer(dsbMitgliedDO.getMitgliedsnummer());
        dsbMitgliedBE.setDsbMitgliedVereinsId(dsbMitgliedDO.getVereinsId());
        dsbMitgliedBE.setDsbMitgliedUserId(dsbMitgliedDO.getUserId());
        dsbMitgliedBE.setDsbMitgliedBeitrittsdatum(dsbMitgliedDO.getBeitrittsdatum());

        dsbMitgliedBE.setCreatedAtUtc(createdAtUtcTimestamp);
        dsbMitgliedBE.setCreatedByUserId(dsbMitgliedDO.getCreatedByUserId());
        dsbMitgliedBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        dsbMitgliedBE.setLastModifiedByUserId(dsbMitgliedDO.getLastModifiedByUserId());
        dsbMitgliedBE.setVersion(dsbMitgliedDO.getVersion());

        return dsbMitgliedBE;
    }

    /**
     * Converts a {@link DsbMitgliedDO} to a {@link DsbMitgliedBE}
     */
    public static final Function<DsbMitgliedDO, DsbMitgliedBE> toDsbMitgliedBE = dsbMitgliedDO -> {
        DsbMitgliedBE dsbMitgliedBE = new DsbMitgliedBE();
        dsbMitgliedBE = convertToDsbMitgliedBECommon(dsbMitgliedDO, dsbMitgliedBE);
        dsbMitgliedBE.setDsbMitgliedVereinName(dsbMitgliedDO.getVereinName());
        return dsbMitgliedBE;
    };

    /**
     * Converts a {@link DsbMitgliedDO} to a {@link DsbMitgliedWithoutVereinsnameBE}
     */
    public static final Function<DsbMitgliedDO, DsbMitgliedWithoutVereinsnameBE> toDsbMitgliedWithoutVereinsnameBE = dsbMitgliedDO -> {
        DsbMitgliedWithoutVereinsnameBE dsbMitgliedWithoutVereinsnameBE = new DsbMitgliedWithoutVereinsnameBE();
        return convertToDsbMitgliedBECommon(dsbMitgliedDO, dsbMitgliedWithoutVereinsnameBE);
    };

    /**
     * Private constructor
     */
    private DsbMitgliedMapper() {
        // empty private constructor
    }
}
