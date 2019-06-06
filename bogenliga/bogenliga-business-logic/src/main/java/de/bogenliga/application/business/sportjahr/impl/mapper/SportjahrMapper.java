package de.bogenliga.application.business.sportjahr.impl.mapper;


import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.business.sportjahr.impl.entity.SportjahrBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the wettkampftyp DataObjects and BusinessEntities.
 */
public class SportjahrMapper implements ValueObjectMapper {

    /**
     * Converts a {@link SportjahrBE} to a {@link SportjahrDO}
     */
    public static final Function<SportjahrBE, SportjahrDO> toSportjahrDO = be -> {

        final Long id = be.getSportjahrId();
        final String ligaName = be.getSportjahrName();
        final String regionName = be.getLigaName;


        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new SportjahrDO(id, name, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId,
                version);
    };

    /**
     * Converts a {@link SportjahrDO} to a {@link SportjahrBE}
     */
    public static final Function<SportjahrDO, SportjahrBE> toWettkampftypBE = wettkampftypDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(wettkampftypDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(
                wettkampftypDO.getLastModifiedAtUtc());

        SportjahrBE wettkampftypBe = new SportjahrBE();
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
    private SportjahrMapper() {
        // empty private constructor
    }
}
