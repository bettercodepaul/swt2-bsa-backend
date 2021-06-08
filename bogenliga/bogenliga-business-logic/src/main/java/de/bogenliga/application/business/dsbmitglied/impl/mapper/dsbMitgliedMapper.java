package de.bogenliga.application.business.dsbmitglied.impl.mapper;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.MitgliedBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 *
 *
 * I convert the dsbmitglied DataObjects and BusinessEntities.
 *
 */
public class dsbMitgliedMapper implements ValueObjectMapper {

    /**
     * Converts a {@link MitgliedBE} to a {@link DsbMitgliedDO}
     *
     */
    public static final Function<MitgliedBE, DsbMitgliedDO> toDsbMitgliedDO = be -> {

        final Long id = be.getDsbMitgliedId();
        final String vorname = be.getDsbMitgliedVorname();
        final String nachname = be.getDsbMitgliedNachname();
        final Date geburtsdatum = be.getDsbMitgliedGeburtsdatum();
        final String nationalitaet = be.getDsbMitgliedNationalitaet();
        final String mitgliedsnummer = be.getDsbMitgliedMitgliedsnummer();
        final Long vereinsId = be.getDsbMitgliedVereinsId();
        final Long userId = be.getDsbMitgliedUserId();
        final Boolean kampfrichter=false;


        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new DsbMitgliedDO(id, vorname, nachname, geburtsdatum, nationalitaet, mitgliedsnummer, vereinsId, userId,
                createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version, kampfrichter);
    };

    /**
     * Converts a {@link DsbMitgliedDO} to a {@link MitgliedBE}
     */
    public static final Function<DsbMitgliedDO, MitgliedBE> toDsbMitgliedBE = dsbMitgliedDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getLastModifiedAtUtc());

        MitgliedBE mitgliedBE = new MitgliedBE();
        mitgliedBE.setDsbMitgliedId(dsbMitgliedDO.getId());
        mitgliedBE.setDsbMitgliedVorname(dsbMitgliedDO.getVorname());
        mitgliedBE.setDsbMitgliedNachname(dsbMitgliedDO.getNachname());
        mitgliedBE.setDsbMitgliedGeburtsdatum(dsbMitgliedDO.getGeburtsdatum());
        mitgliedBE.setDsbMitgliedNationalitaet(dsbMitgliedDO.getNationalitaet());
        mitgliedBE.setDsbMitgliedMitgliedsnummer(dsbMitgliedDO.getMitgliedsnummer());
        mitgliedBE.setDsbMitgliedVereinsId(dsbMitgliedDO.getVereinsId());
        mitgliedBE.setDsbMitgliedUserId(dsbMitgliedDO.getUserId());

        mitgliedBE.setCreatedAtUtc(createdAtUtcTimestamp);
        mitgliedBE.setCreatedByUserId(dsbMitgliedDO.getCreatedByUserId());
        mitgliedBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        mitgliedBE.setLastModifiedByUserId(dsbMitgliedDO.getLastModifiedByUserId());
        mitgliedBE.setVersion(dsbMitgliedDO.getVersion());

        return mitgliedBE;
    };


    /**
     * Private constructor
     */
    private dsbMitgliedMapper() {
        // empty private constructor
    }
}
