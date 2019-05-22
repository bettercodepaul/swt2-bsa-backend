package de.bogenliga.application.business.wettkampftyp.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampfTypBE;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.time.DateProvider;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class WettkampfTypMapper {
    public static final Function<WettkampfTypBE, WettkampfTypDO> toWettkampfTypDO = wettkampfTypBE -> {
        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(wettkampfTypBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedUtc = DateProvider.convertTimestamp(wettkampfTypBE.getLastModifiedAtUtc());

        return new WettkampfTypDO(wettkampfTypBE.getId(),
                wettkampfTypBE.getName(),
                createdAtUtc,
                wettkampfTypBE.getCreatedByUserId(),
                lastModifiedUtc,
                wettkampfTypBE.getLastModifiedByUserId(),
                wettkampfTypBE.getVersion());
    };


    public static final Function<WettkampfTypDO, WettkampfTypBE> toWettkampfTypBE = toWettkampfTypDO -> {
        if (toWettkampfTypDO == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    "Entity could not be found with the given IDs");
        }
        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(toWettkampfTypDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(toWettkampfTypDO.getLastModifiedAtUtc());

        WettkampfTypBE wettkampfTypBE = new WettkampfTypBE();
        wettkampfTypBE.setId(toWettkampfTypDO.getId());
        wettkampfTypBE.setName(toWettkampfTypDO.getName());
        wettkampfTypBE.setCreatedAtUtc(createdAtUtcTimestamp);
        wettkampfTypBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        wettkampfTypBE.setLastModifiedByUserId(toWettkampfTypDO.getLastModifiedByUserId());
        wettkampfTypBE.setCreatedByUserId(toWettkampfTypDO.getCreatedByUserId());
        wettkampfTypBE.setVersion(toWettkampfTypDO.getVersion());

        return wettkampfTypBE;
    };
}
