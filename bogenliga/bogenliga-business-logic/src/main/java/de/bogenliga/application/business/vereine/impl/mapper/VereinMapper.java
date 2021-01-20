package de.bogenliga.application.business.vereine.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert Verein DataObjects into BusinessEntities and vice versa.
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class VereinMapper implements ValueObjectMapper {

    private VereinMapper() {
        // empty constructor
    }

    public static final Function<VereinBE, VereinDO> toVereinDO = be -> {

        final Long id = be.getVereinId();
        final String name = be.getVereinName();
        final String dsbIdentifier = be.getVereinDsbIdentifier();
        final Long regionId = be.getVereinRegionId();
        final String website = be.getVereinWebsite();
        final String description = be.getVereinDescription();
        final String icon = be.getVereinIcon();

        // technical params
        final Long createdByUserId = be.getCreatedByUserId();
        final Long lastModifiedByUserId = be.getLastModifiedByUserId();
        final Long version = be.getVersion();

        final OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        final OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new VereinDO(id, name, dsbIdentifier, regionId, "",
                            website, description, icon, createdAtUtc, createdByUserId,
                            lastModifiedAtUtc, lastModifiedByUserId, version);
    };

    public static final Function<VereinDO, VereinBE> toVereinBE = vereinDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(vereinDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(vereinDO.getLastModifiedAtUtc());

        VereinBE vereinBE = new VereinBE();
        vereinBE.setVereinId(vereinDO.getId());
        vereinBE.setVereinName(vereinDO.getName());
        vereinBE.setVereinDsbIdentifier(vereinDO.getDsbIdentifier());
        vereinBE.setVereinRegionId(vereinDO.getRegionId());
        vereinBE.setVereinWebsite(vereinDO.getWebsite());
        vereinBE.setVereinDescription(vereinDO.getDescription());
        vereinBE.setVereinIcon(vereinDO.getIcon());

        vereinBE.setCreatedAtUtc(createdAtUtcTimestamp);
        vereinBE.setCreatedByUserId(vereinBE.getCreatedByUserId());
        vereinBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        vereinBE.setLastModifiedByUserId(vereinBE.getLastModifiedByUserId());

        return vereinBE;
    };

}
