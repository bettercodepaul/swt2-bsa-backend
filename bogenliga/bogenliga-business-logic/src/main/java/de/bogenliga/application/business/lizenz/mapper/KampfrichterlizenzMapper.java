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
     * Converts a {@link LizenzBE} to a {@link DsbMitgliedDO}
     *
     */
/*    public static final Function<KampfrichterlizenzBE, DsbMitgliedDO> toDsbMitgliedDO = be -> {

        final Long id = be.getDsbMitgliedId();
        final String vorname = be.getDsbMitgliedVorname();
        final String nachname = be.getDsbMitgliedNachname();
        final Date geburtsdatum = be.getDsbMitgliedGeburtsdatum();
        final String nationalitaet = be.getDsbMitgliedNationalitaet();
        final String mitgliedsnummer = be.getDsbMitgliedMitgliedsnummer();
        final Long vereinsId = be.getDsbMitgliedVereinsId();
        final Long userId = be.getDsbMitgliedUserId();

        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new DsbMitgliedDO(id, vorname, nachname, geburtsdatum, nationalitaet, mitgliedsnummer, vereinsId, userId,
                createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);

    };

    /**
     * Converts a {@link DsbMitgliedDO} to a {@link KampfrichterlizenzBE}
     */
    public static final Function<DsbMitgliedDO, LizenzBE> toKampfrichterlizenz = dsbMitgliedDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getLastModifiedAtUtc());

        LizenzBE lizenzBE = new LizenzBE();
        System.out.println(dsbMitgliedDO.getId());
        lizenzBE.setLizenzDsbMitgliedId(dsbMitgliedDO.getId());
        lizenzBE.setLizenztyp("Kampfrichter");
        lizenzBE.setLizenzId(lizenzBE.getLizenzId());
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
