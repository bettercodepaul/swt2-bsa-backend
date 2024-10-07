package de.bogenliga.application.business.dsbmannschaft.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;

import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * @author Philip Dengler
 */


public class DsbMannschaftMapper implements ValueObjectMapper {


    /**
     * Converts a {@link DsbMannschaftBE} to a {@link DsbMannschaftDO}
     *
     */
    public static final Function<DsbMannschaftBE, DsbMannschaftDO> toDsbMannschaftDO = be -> {

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

        return new DsbMannschaftDO(id, name, vereinId, nummer, benutzerId, veranstaltungId, sortierung,
                createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };
    public static final Function<DsbMannschaftBE, DsbMannschaftDO> toDsbMannschaftVerUWettDO = be -> {

        final String veranstaltungName = be.getVeranstaltungName();
        final String wettkampfTag = be.getWettkampfTag();
        final String wettkampfOrtsname = be.getWettkampfOrtsname();
        final String vereinName = be.getVereinName();
        final Long mannschaftNummer = be.getNummer();

        return new DsbMannschaftDO(veranstaltungName, wettkampfTag, wettkampfOrtsname, vereinName,mannschaftNummer);
    };



    /**
     * Converts a {@link DsbMannschaftDO} to a {@link DsbMannschaftBE}
     */

    public static final Function<DsbMannschaftDO, DsbMannschaftBE> toDsbMannschaftBE = dsbMannschaftDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMannschaftDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMannschaftDO.getLastModifiedAtUtc());

        DsbMannschaftBE dsbMannschaftBE = new DsbMannschaftBE();
        dsbMannschaftBE.setId(dsbMannschaftDO.getId());
        dsbMannschaftBE.setVereinId(dsbMannschaftDO.getVereinId());
        dsbMannschaftBE.setNummer(dsbMannschaftDO.getNummer());
        dsbMannschaftBE.setBenutzerId(dsbMannschaftDO.getBenutzerId());
        dsbMannschaftBE.setVeranstaltungId(dsbMannschaftDO.getVeranstaltungId());
        dsbMannschaftBE.setSortierung(dsbMannschaftDO.getSortierung());


        dsbMannschaftBE.setCreatedAtUtc(createdAtUtcTimestamp);
        dsbMannschaftBE.setCreatedByUserId(dsbMannschaftDO.getCreatedByUserId());
        dsbMannschaftBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        dsbMannschaftBE.setLastModifiedByUserId(dsbMannschaftDO.getLastModifiedByUserId());
        dsbMannschaftBE.setVersion(dsbMannschaftDO.getVersion());

        return dsbMannschaftBE;
    };


    /**
     * Private constructor
     */
    private DsbMannschaftMapper() {
        // empty private constructor
    }

}
