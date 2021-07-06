package de.bogenliga.application.business.kampfrichter.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterExtendedBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the kampfrichter DataObjects and BusinessEntities.
 */
public class KampfrichterMapper implements ValueObjectMapper {

    /**
     * Converts a {@link KampfrichterBE} to a {@link KampfrichterDO}
     */
    public static final Function<KampfrichterBE, KampfrichterDO> toKampfrichterDO = be -> {

        final Long userId = be.getKampfrichterUserId();
        final Long wettkampfId = be.getKampfrichterWettkampfId();
        final boolean leitend = be.isKampfrichterLeitend();

        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new KampfrichterDO(userId, wettkampfId, leitend,
                createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };

    /**
     * Converts a {@link KampfrichterDO} to a {@link KampfrichterBE}
     */
    public static final Function<KampfrichterDO, KampfrichterBE> toKampfrichterBE = kampfrichterDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(kampfrichterDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(
                kampfrichterDO.getLastModifiedAtUtc());

        KampfrichterBE kampfrichterBE = new KampfrichterBE();
        kampfrichterBE.setKampfrichterUserId(kampfrichterDO.getUserId());


        kampfrichterBE.setKampfrichterWettkampfId(kampfrichterDO.getWettkampfId());

        kampfrichterBE.setKampfrichterLeitend(kampfrichterDO.isLeitend());

        kampfrichterBE.setCreatedAtUtc(createdAtUtcTimestamp);
        kampfrichterBE.setCreatedByUserId(kampfrichterDO.getCreatedByUserId());
        kampfrichterBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        kampfrichterBE.setLastModifiedByUserId(kampfrichterDO.getLastModifiedByUserId());
        kampfrichterBE.setVersion(kampfrichterDO.getVersion());

        return kampfrichterBE;
    };

    public static final Function<KampfrichterExtendedBE, KampfrichterDO> toKampfrichterDOExtended = be -> {

        final Long userId = be.getKampfrichterExtendedUserID();
        final String kampfrichterVorname= be.getKampfrichterExtendedVorname();
        final String kampfrichterNachname= be.getKampfrichterExtendedNachname();
        final String email = be.getKampfrichterExtendedEmail();
        final Long wettkampfId = be.getKampfrichterExtendedWettkampfID();
        boolean leitend=false;
        if(be.getKampfrichterExtendedLeitend()!=null){
            leitend = be.getKampfrichterExtendedLeitend();
        }


        return new KampfrichterDO(userId,kampfrichterVorname, kampfrichterNachname,email,wettkampfId, leitend);
    };
    public static final Function<KampfrichterDO, KampfrichterExtendedBE> toKampfrichterExtendedBE = kampfrichterDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(kampfrichterDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(
                kampfrichterDO.getLastModifiedAtUtc());

        KampfrichterExtendedBE kampfrichterExtendedBE= new KampfrichterExtendedBE();
        kampfrichterExtendedBE.setKampfrichterExtendedUserID(kampfrichterDO.getUserId());
        kampfrichterExtendedBE.setKampfrichterExtendedVorname(kampfrichterDO.getVorname());
        kampfrichterExtendedBE.setKampfrichterExtendedNachname(kampfrichterDO.getNachname());
        kampfrichterExtendedBE.setKampfrichterExtendedEmail(kampfrichterDO.getKampfrichterEmail());
        kampfrichterExtendedBE.setKampfrichterExtendedWettkampfID(kampfrichterDO.getWettkampfId());

        kampfrichterExtendedBE.setKampfrichterExtendedLeitend(kampfrichterDO.isLeitend());

        kampfrichterExtendedBE.setCreatedAtUtc(createdAtUtcTimestamp);
        kampfrichterExtendedBE.setCreatedByUserId(kampfrichterDO.getCreatedByUserId());
        kampfrichterExtendedBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        kampfrichterExtendedBE.setLastModifiedByUserId(kampfrichterDO.getLastModifiedByUserId());
        kampfrichterExtendedBE.setVersion(kampfrichterDO.getVersion());

        return kampfrichterExtendedBE;
    };




    /**
     * Private constructor
     */
    private KampfrichterMapper() {
        // empty private constructor
    }
}
