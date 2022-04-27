package de.bogenliga.application.business.wettkampf.impl.mapper;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.function.Function;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the wettkampf DataObjects and BusinessEntities.
 *
 */
public class WettkampfMapper implements ValueObjectMapper {

    /**
     * Converts a {@link WettkampfBE} to a {@link WettkampfDO}
     *
     */
    public static final Function<WettkampfBE, WettkampfDO> toWettkampfDO = be -> {

        final Long id = be.getId();
        final Long veranstaltungsId = be.getVeranstaltungsId();
        final Date datum = be.getDatum();
        final String wettkampfStrasse = be.getWettkampfStrasse();
        final String wettkampfPlz = be.getWettkampfPlz();
        final String wettkampfOrtsname = be.getWettkampfOrtsname();
        final String wettkampfOrstinfo = be.getWettkampfOrtsinfo();
        final String wettkampfBeginn = be.getWettkampfBeginn();
        final Long wettkampfTag = be.getWettkampfTag();
        final Long wettkampfDisziplinId = be.getWettkampfDisziplinId();
        final Long wettkampfTypId = be.getWettkampfTypId();
        final Long wettkampfAusrichter = be.getWettkampfAusrichter();

        // technical parameter
        Long version = be.getVersion();

        return new WettkampfDO(
                id,
                veranstaltungsId,
                datum,
                wettkampfStrasse,
                wettkampfPlz,
                wettkampfOrtsname,
                wettkampfOrstinfo ,
                wettkampfBeginn,
                wettkampfTag,
                wettkampfDisziplinId,
                wettkampfTypId,
                version,
                wettkampfAusrichter);
    };

    /**
     * Converts a {@link WettkampfDO} to a {@link WettkampfBE}
     */
    public static final Function<WettkampfDO, WettkampfBE> toWettkampfBE = wettkampfDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(wettkampfDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(wettkampfDO.getLastModifiedAtUtc());

        WettkampfBE wettkampfBe = new WettkampfBE();
        wettkampfBe.setId(wettkampfDO.getId());
        wettkampfBe.setVeranstaltungsId(wettkampfDO.getWettkampfVeranstaltungsId());
        wettkampfBe.setDatum(wettkampfDO.getWettkampfDatum());
        wettkampfBe.setWettkampfStrasse(wettkampfDO.getWettkampfStrasse());
        wettkampfBe.setWettkampfPlz(wettkampfDO.getWettkampfPlz());
        wettkampfBe.setWettkampfOrtsname(wettkampfDO.getWettkampfOrtsname());
        wettkampfBe.setWettkampfOrtsinfo(wettkampfDO.getWettkampfOrtsinfo());
        wettkampfBe.setWettkampfBeginn(wettkampfDO.getWettkampfBeginn());
        wettkampfBe.setWettkampfTag(wettkampfDO.getWettkampfTag());
        wettkampfBe.setWettkampfDisziplinId(wettkampfDO.getWettkampfDisziplinId());
        wettkampfBe.setWettkampfTypId(wettkampfDO.getWettkampfTypId());


        wettkampfBe.setCreatedAtUtc(createdAtUtcTimestamp);
        wettkampfBe.setCreatedByUserId(wettkampfDO.getCreatedByUserId());
        wettkampfBe.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        wettkampfBe.setLastModifiedByUserId(wettkampfDO.getLastModifiedByUserId());
        wettkampfBe.setVersion(wettkampfDO.getVersion());
        wettkampfBe.setWettkampfAusrichter(wettkampfDO.getWettkampfAusrichter());

        return wettkampfBe;
    };


    /**
     * Private constructor
     */
    private WettkampfMapper() {
        // empty private constructor
    }
}
