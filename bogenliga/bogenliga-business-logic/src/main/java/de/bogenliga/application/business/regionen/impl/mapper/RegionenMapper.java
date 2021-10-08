package de.bogenliga.application.business.regionen.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 *I convert the region DataObjects and BusinessEntities
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
public class RegionenMapper implements ValueObjectMapper {

    private RegionenMapper() {
        // empty constructor
    }

    public static final Function<RegionenBE, RegionenDO> toRegionDO = regionenBE -> {
        final Long regionID = regionenBE.getRegionId();
        final String regionName = regionenBE.getRegionName();
        final String regionKuerzel = regionenBE.getRegionKuerzel();
        final String regionTyp = regionenBE.getRegionTyp();
        final Long regionUebergeordnet = regionenBE.getRegionUebergeordnet();

        //Technical parameters
        final Long createdByUserId = regionenBE.getCreatedByUserId();
        final Long lastModifiedByUserId = regionenBE.getLastModifiedByUserId();
        final Long version = regionenBE.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(regionenBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedUtc = DateProvider.convertTimestamp(regionenBE.getLastModifiedAtUtc());

        return new RegionenDO(regionID,regionName,regionKuerzel,regionTyp,regionUebergeordnet, null,
                    createdAtUtc,createdByUserId, lastModifiedUtc, lastModifiedByUserId, version);
    };


    public static final Function<RegionenDO, RegionenBE> toRegionBE = regionenDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(regionenDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(regionenDO.getLastModifiedAtUtc());

        RegionenBE regionenBE = new RegionenBE();
        regionenBE.setRegionId(regionenDO.getId());
        regionenBE.setRegionName(regionenDO.getRegionName());
        regionenBE.setRegionKuerzel(regionenDO.getRegionKuerzel());
        regionenBE.setRegionTyp(regionenDO.getRegionTyp());
        regionenBE.setRegionUebergeordnet(regionenDO.getRegionUebergeordnet());

        regionenBE.setCreatedAtUtc(createdAtUtcTimestamp);
        regionenBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        regionenBE.setLastModifiedByUserId(regionenDO.getLastModifiedByUserId());
        regionenBE.setVersion(regionenDO.getVersion());


        return regionenBE;

    };


}
