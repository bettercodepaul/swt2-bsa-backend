package de.bogenliga.application.business.mannschaftsmitglied.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 *
 * @author Philip Dengler
 */
public class MannschaftsmitgliedMapper implements ValueObjectMapper {



    public static final Function<MannschaftsmitgliedBE, MannschaftsmitgliedDO> toMannschaftsmitgliedDO = be -> {

        final Long mannschaftId = be.getMannschaftId();
        final Long mitgliedId = be.getDsbMitgliedId();
        final boolean mitgliedEingesetzt = be.isDsbMitgliedEingesetzt();
        final String mitgliedVorname = be.getDsbMitgliedVorname();
        final String mitgliedNachname = be.getDsbMitgliedNachname();

        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new MannschaftsmitgliedDO(mannschaftId, mitgliedId, mitgliedEingesetzt,mitgliedVorname,mitgliedNachname,
                createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };



    public static final Function<MannschaftsmitgliedDO, MannschaftsmitgliedBE> toMannschaftsmitgliedBE = mannschaftsmitgliedDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(mannschaftsmitgliedDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(
                mannschaftsmitgliedDO.getLastModifiedAtUtc());

        MannschaftsmitgliedBE mannschaftsmitgliedBE = new MannschaftsmitgliedBE();

        mannschaftsmitgliedBE.setMannschaftId(mannschaftsmitgliedDO.getMannschaftId());
        mannschaftsmitgliedBE.setDsbMitgliedId(mannschaftsmitgliedDO.getDsbMitgliedId());
        mannschaftsmitgliedBE.setDsbMitgliedEingesetzt(mannschaftsmitgliedDO.isDsbMitgliedEingesetzt());
        mannschaftsmitgliedBE.setDsbMitgliedVorname(mannschaftsmitgliedDO.getDsbMitgliedVorname());
        mannschaftsmitgliedBE.setDsbMitgliedNachname(mannschaftsmitgliedDO.getDsbMitgliedNachname());

        mannschaftsmitgliedBE.setCreatedAtUtc(createdAtUtcTimestamp);
        mannschaftsmitgliedBE.setCreatedByUserId(mannschaftsmitgliedDO.getCreatedByUserId());
        mannschaftsmitgliedBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        mannschaftsmitgliedBE.setLastModifiedByUserId(mannschaftsmitgliedDO.getLastModifiedByUserId());
        mannschaftsmitgliedBE.setVersion(mannschaftsmitgliedDO.getVersion());

        return mannschaftsmitgliedBE;
    };

        /**
         * Private constructor
         */
    private MannschaftsmitgliedMapper() {
            // empty
        // private constructor
        }




}
