package de.bogenliga.application.business.dsbmitglied.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the dsbmitglied DataObjects and BusinessEntities.
 *
 */
public class DsbMitgliedMapper implements ValueObjectMapper {

    /**
     * Converts a {@link DsbMitgliedBE} to a {@link DsbMitgliedDO}
     *
     */
    public static final Function<DsbMitgliedBE, DsbMitgliedDO> toDsbMitgliedDO = be -> {

        final long id = be.getDsbMitgliedId();
        final String vorname = be.getDsbMitgliedVorname();
        final String nachname = be.getDsbMitgliedNachname();
        final String geburtsdatum = be.getDsbMitgliedGeburtsdatum();
        final String nationalitaet = be.getDsbMitgliedNationalitaet();
        final String mitgliedsnummer = be.getDsbMitgliedMitgliedsnummer();
        final long vereinsId = be.getDsbMitgliedVereinsId();
        final long userId = be.getDsbMitgliedUserId();

        // technical parameter
        long createdByUserId = be.getCreatedByUserId();
        long lastModifiedByUserId = be.getLastModifiedByUserId();
        long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new DsbMitgliedDO(id, vorname, nachname, geburtsdatum, nationalitaet, mitgliedsnummer, vereinsId, userId,
                createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };

    /**
     * Converts a {@link DsbMitgliedDO} to a {@link DsbMitgliedBE}
     */
    public static final Function<DsbMitgliedDO, DsbMitgliedBE> toDsbMitgliedBE = vo -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getLastModifiedAtUtc());

        DsbMitgliedBE dsbMitgliedBE = new DsbMitgliedBE();
        dsbMitgliedBE.setDsbMitgliedId(vo.getId());
        dsbMitgliedBE.setDsbMitgliedVorname(vo.getVorname());
        dsbMitgliedBE.setDsbMitgliedNachname(vo.getNachname());
        dsbMitgliedBE.setDsbMitgliedGeburtsdatum(vo.getGeburtsdatum());
        dsbMitgliedBE.setDsbMitgliedNationalitaet(vo.getNationalitaet());
        dsbMitgliedBE.setDsbMitgliedMitgliedsnummer(vo.getMitgliedsnummer());
        dsbMitgliedBE.setDsbMitgliedVereinsId(vo.getVereinsId());
        dsbMitgliedBE.setDsbMitgliedUserId(vo.getUserId());

        dsbMitgliedBE.setCreatedAtUtc(createdAtUtcTimestamp);
        dsbMitgliedBE.setCreatedByUserId(vo.getCreatedByUserId());
        dsbMitgliedBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        dsbMitgliedBE.setLastModifiedByUserId(vo.getLastModifiedByUserId());
        dsbMitgliedBE.setVersion(vo.getVersion());

        return dsbMitgliedBE;
    };


    /**
     * Private constructor
     */
    private DsbMitgliedMapper() {
        // empty private constructor
    }
}
