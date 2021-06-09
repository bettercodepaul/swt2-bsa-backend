package de.bogenliga.application.business.mannschaft.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;

import de.bogenliga.application.business.mannschaft.api.types.MannschaftDO;
import de.bogenliga.application.business.mannschaft.impl.entity.MannschaftBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * @author Philip Dengler
 */


public class MannschaftMapper implements ValueObjectMapper {


    /**
     * Converts a {@link MannschaftBE} to a {@link MannschaftDO}
     *
     */
    public static final Function<MannschaftBE, MannschaftDO> toDsbMannschaftDO = be -> {

        final Long id = be.getId();
        final Long vereinId = be.getVereinId();
        final Long nummer = be.getNummer();
        final Long benutzerId = be.getBenutzerId();
        final Long veranstaltungId = be.getVeranstaltungId();
        final Long sortierung = be.getSortierung();
        final String name = null; //empty


        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new MannschaftDO(id, name, vereinId, nummer, benutzerId, veranstaltungId, sortierung,
                createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };



    /**
     * Converts a {@link MannschaftDO} to a {@link MannschaftBE}
     */

    public static final Function<MannschaftDO, MannschaftBE> toDsbMannschaftBE = dsbMannschaftDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMannschaftDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMannschaftDO.getLastModifiedAtUtc());

        MannschaftBE mannschaftBE = new MannschaftBE();
        mannschaftBE.setId(dsbMannschaftDO.getId());
        mannschaftBE.setVereinId(dsbMannschaftDO.getVereinId());
        mannschaftBE.setNummer(dsbMannschaftDO.getNummer());
        mannschaftBE.setBenutzerId(dsbMannschaftDO.getBenutzerId());
        mannschaftBE.setVeranstaltungId(dsbMannschaftDO.getVeranstaltungId());
        mannschaftBE.setSortierung(dsbMannschaftDO.getSortierung());


        mannschaftBE.setCreatedAtUtc(createdAtUtcTimestamp);
        mannschaftBE.setCreatedByUserId(dsbMannschaftDO.getCreatedByUserId());
        mannschaftBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        mannschaftBE.setLastModifiedByUserId(dsbMannschaftDO.getLastModifiedByUserId());
        mannschaftBE.setVersion(dsbMannschaftDO.getVersion());

        return mannschaftBE;
    };


    /**
     * Private constructor
     */
    private MannschaftMapper() {
        // empty private constructor
    }

}
