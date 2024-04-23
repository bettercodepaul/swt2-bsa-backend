package de.bogenliga.application.business.dsbmitglied.impl.mapper;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 *
 *
 * I convert the dsbmitglied DataObjects and BusinessEntities.
 *
 */
public class DsbMitgliedMapper implements ValueObjectMapper {

    /**
     * Converts a {@link DsbMitgliedBE} to a {@link DsbMitgliedDO}
     *
     */
    public static final Function<DsbMitgliedBE, DsbMitgliedDO> toDsbMitgliedDO = be -> {

        final Long id = be.getDsbMitgliedId();
        final String vorname = be.getDsbMitgliedVorname();
        final String nachname = be.getDsbMitgliedNachname();
        final Date geburtsdatum = be.getDsbMitgliedGeburtsdatum();
        final String nationalitaet = be.getDsbMitgliedNationalitaet();
        final String mitgliedsnummer = be.getDsbMitgliedMitgliedsnummer();
        final Long vereinsId = be.getDsbMitgliedVereinsId();
        final String vereinName= be.getDsbMitgliedVereinName();
        final Long userId = be.getDsbMitgliedUserId();
        final Boolean kampfrichter = false;


        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new DsbMitgliedDO(id, vorname, nachname, geburtsdatum, nationalitaet, mitgliedsnummer, vereinsId,
                vereinName, userId, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version,
                kampfrichter);
    };

    /**
     * Converts a {@link DsbMitgliedDO} to a {@link DsbMitgliedBE}
     */
    public static final Function<DsbMitgliedDO, DsbMitgliedBE> toDsbMitgliedBE = dsbMitgliedDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getLastModifiedAtUtc());

        DsbMitgliedBE dsbMitgliedBE = new DsbMitgliedBE();
        dsbMitgliedBE.setDsbMitgliedId(dsbMitgliedDO.getId());
        dsbMitgliedBE.setDsbMitgliedVorname(dsbMitgliedDO.getVorname());
        dsbMitgliedBE.setDsbMitgliedNachname(dsbMitgliedDO.getNachname());
        dsbMitgliedBE.setDsbMitgliedGeburtsdatum(dsbMitgliedDO.getGeburtsdatum());
        dsbMitgliedBE.setDsbMitgliedNationalitaet(dsbMitgliedDO.getNationalitaet());
        dsbMitgliedBE.setDsbMitgliedMitgliedsnummer(dsbMitgliedDO.getMitgliedsnummer());
        dsbMitgliedBE.setDsbMitgliedVereinsId(dsbMitgliedDO.getVereinsId());
        dsbMitgliedBE.setDsbMitgliedVereinName(dsbMitgliedDO.getVereinName());
        dsbMitgliedBE.setDsbMitgliedUserId(dsbMitgliedDO.getUserId());

        dsbMitgliedBE.setCreatedAtUtc(createdAtUtcTimestamp);
        dsbMitgliedBE.setCreatedByUserId(dsbMitgliedDO.getCreatedByUserId());
        dsbMitgliedBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        dsbMitgliedBE.setLastModifiedByUserId(dsbMitgliedDO.getLastModifiedByUserId());
        dsbMitgliedBE.setVersion(dsbMitgliedDO.getVersion());

        return dsbMitgliedBE;
    };


    /**
     * Private constructor
     */
    private DsbMitgliedMapper() {
        // empty private constructor
    }
}
