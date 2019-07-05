package de.bogenliga.application.business.disziplin.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.disziplin.impl.entity.DisziplinBE;
import de.bogenliga.application.common.time.DateProvider;

/**
 *
 * @author Marcel Neumann
 * @author Robin Mueller
 */
public class DisziplinMapper {

    private DisziplinMapper(){}

    public static final Function<DisziplinBE, DisziplinDO> toDisziplinDO = disziplinBE -> {
        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(disziplinBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedUtc = DateProvider.convertTimestamp(disziplinBE.getLastModifiedAtUtc());
        return new DisziplinDO(disziplinBE.getId(), disziplinBE.getName(), createdAtUtc,
                disziplinBE.getCreatedByUserId(),
                lastModifiedUtc,
                disziplinBE.getLastModifiedByUserId(),
                disziplinBE.getVersion());
    };

    public static final Function<DisziplinDO, DisziplinBE> toDisziplinBE = disziplinDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(disziplinDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(disziplinDO.getLastModifiedAtUtc());

        DisziplinBE disziplinBE = new DisziplinBE();
        disziplinBE.setId(disziplinDO.getDisziplinID());
        disziplinBE.setName(disziplinDO.getDisziplinName());

        disziplinBE.setCreatedAtUtc(createdAtUtcTimestamp);
        disziplinBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        disziplinBE.setCreatedByUserId(disziplinDO.getCreatedByUserId());
        disziplinBE.setLastModifiedByUserId(disziplinDO.getLastModifiedByUserId());
        disziplinBE.setVersion(disziplinDO.getVersion());

        return disziplinBE;
    };
}