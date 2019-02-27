package de.bogenliga.application.business.wettkampf.impl.mapper;


import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfOverviewDO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfOverviewBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;

/**
 * I convert the wettkampf DataObjects and BusinessEntities.
 *
 */
public class WettkampfOverviewMapper implements ValueObjectMapper {

    /**
     * Converts a {@link WettkampfBE} to a {@link WettkampfDO}
     *
     */
    public static final Function<WettkampfOverviewBE, WettkampfOverviewDO> toWettkampfOverviewDO = be -> {

        final Long id = be.getId();
        final String datum = be.getDatum();
        final String wettkampfOrt = be.getWettkampfOrt();
        final String wettkampfBeginn = be.getWettkampfBeginn();
        final Long wettkampfTag = be.getWettkampfTag();
        final Long wettkampfDisziplinId = be.getDisziplinID();
        final String disziplinName = be.getDisziplinName();


        return new WettkampfOverviewDO(id, datum, wettkampfOrt, wettkampfBeginn, wettkampfTag, wettkampfDisziplinId, disziplinName);
    };

    /**
     * Private constructor
     */
    private WettkampfOverviewMapper() {
        // empty private constructor
    }
}
