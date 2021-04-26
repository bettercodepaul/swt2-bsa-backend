package de.bogenliga.application.business.lizenz.impl.mapper;

import java.sql.Timestamp;
import java.util.function.Function;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert Lizenz DataObjects into BusinessEntities and vice versa.
 *
 * @author Manuel Baisch
 */
public class LizenzMapper implements ValueObjectMapper {


    /**
     * Private constructor
     */
    private LizenzMapper() {
        // empty private constructor
    }

    public static final Function<LizenzBE, LizenzDO> toLizenzDO = be -> {

        final Long lizenzId = be.getLizenzId();
        final String lizenznummer = be.getLizenznummer();
        final Long lizenzRegionId = be.getLizenzRegionId();
        final Long lizenzDsbMitgliedId = be.getLizenzDsbMitgliedId();
        final String lizenztyp = be.getLizenztyp();
        final Long lizenzDisziplinId = be.getLizenzDisziplinId();

        return new LizenzDO(lizenzId, lizenznummer, lizenzRegionId, lizenzDsbMitgliedId, lizenztyp,lizenzDisziplinId);
    };

    public static final Function<LizenzDO, LizenzBE> toLizenzBE = lizenzDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(lizenzDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(lizenzDO.getLastModifiedAtUtc());

        LizenzBE lizenzBE = new LizenzBE();
        lizenzBE.setLizenzId(lizenzDO.getLizenzId());
        lizenzBE.setLizenznummer(lizenzDO.getLizenznummer());
        lizenzBE.setLizenzRegionId(lizenzDO.getLizenzRegionId());
        lizenzBE.setLizenzDsbMitgliedId(lizenzDO.getLizenzDsbMitgliedId());
        lizenzBE.setLizenztyp(lizenzDO.getLizenztyp());
        lizenzBE.setLizenzDisziplinId(lizenzDO.getLizenzDisziplinId());

        lizenzBE.setCreatedAtUtc(createdAtUtcTimestamp);
        lizenzBE.setCreatedByUserId(lizenzBE.getCreatedByUserId());
        lizenzBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        lizenzBE.setLastModifiedByUserId(lizenzBE.getLastModifiedByUserId());

        return lizenzBE;
    };
}
