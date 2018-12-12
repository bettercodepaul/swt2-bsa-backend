package de.bogenliga.application.business.lizenz.mapper;

import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.lizenz.entity.LizenzBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

import java.sql.Timestamp;
import java.util.function.Function;

/**
 * I convert the dsbmitglied DataObjects and BusinessEntities.
 *
 */
public class KampfrichterlizenzMapper implements ValueObjectMapper {


    /**
     * Converts a {@link DsbMitgliedDO} to a {@link LizenzBE}
     */
    public static final Function<DsbMitgliedDO, LizenzBE> toKampfrichterlizenz = dsbMitgliedDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getLastModifiedAtUtc());

        LizenzBE lizenzBE = new LizenzBE();
        lizenzBE.setLizenzDsbMitgliedId(dsbMitgliedDO.getId());
        lizenzBE.setLizenztyp("Kampfrichter");
        lizenzBE.setLizenzId(null);
        lizenzBE.setLizenzDisziplinId((long)0);
        lizenzBE.setLizenznummer("123456KL");
        lizenzBE.setLizenzRegionId((long)1);

        lizenzBE.setCreatedAtUtc(createdAtUtcTimestamp);
        lizenzBE.setCreatedByUserId(dsbMitgliedDO.getCreatedByUserId());
        lizenzBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        lizenzBE.setLastModifiedByUserId(dsbMitgliedDO.getLastModifiedByUserId());
        lizenzBE.setVersion(dsbMitgliedDO.getVersion());

        return lizenzBE;
    };


    /**
     * Private constructor
     */
    private KampfrichterlizenzMapper() {
        // empty private constructor
    }
}
