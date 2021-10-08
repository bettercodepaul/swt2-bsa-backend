package de.bogenliga.application.business.wettkampftyp.impl.mapper;


import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampftypBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the wettkampftyp DataObjects and BusinessEntities.
 *
 */
public class WettkampftypMapper implements ValueObjectMapper {

    /**
     * Converts a {@link WettkampftypBE} to a {@link WettkampfTypDO}
     *
     */
    public static final Function<WettkampftypBE, WettkampfTypDO> toWettkampfTypDO = be -> {

        final Long id = be.getwettkampftypID();
        final String name = be.getwettkampftypname();


        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new WettkampfTypDO(id, name, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };

    /**
     * Converts a {@link WettkampfTypDO} to a {@link WettkampftypBE}
     */
    public static final Function<WettkampfTypDO, WettkampftypBE> toWettkampftypBE = wettkampftypDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(wettkampftypDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(wettkampftypDO.getLastModifiedAtUtc());

        WettkampftypBE wettkampftypBe = new WettkampftypBE();
        wettkampftypBe.setwettkampftypID(wettkampftypDO.getId());
        wettkampftypBe.setwettkampftypname(wettkampftypDO.getName());



        wettkampftypBe.setCreatedAtUtc(createdAtUtcTimestamp);
        wettkampftypBe.setCreatedByUserId(wettkampftypDO.getCreatedByUserId());
        wettkampftypBe.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        wettkampftypBe.setLastModifiedByUserId(wettkampftypDO.getLastModifiedByUserId());
        wettkampftypBe.setVersion(wettkampftypDO.getVersion());

        return wettkampftypBe;
    };


    /**
     * Private constructor
     */
    private WettkampftypMapper() {
        // empty private constructor
    }
}