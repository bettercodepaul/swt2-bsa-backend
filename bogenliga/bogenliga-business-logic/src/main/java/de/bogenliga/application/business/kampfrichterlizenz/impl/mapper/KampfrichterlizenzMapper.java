package de.bogenliga.application.business.kampfrichterlizenz.impl.mapper;

import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.kampfrichterlizenz.impl.entity.KampfrichterlizenzBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;

/**
 * I convert the dsbmitglied DataObjects and BusinessEntities.
 *
 */
public class KampfrichterlizenzMapper implements ValueObjectMapper {

    /**
     * Converts a {@link KampfrichterlizenzBE} to a {@link DsbMitgliedDO}
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
    public static final Function<DsbMitgliedDO, KampfrichterlizenzBE> toKampfrichterlizenz = dsbMitgliedDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(dsbMitgliedDO.getLastModifiedAtUtc());

        KampfrichterlizenzBE kampfrichterlizenzBE = new KampfrichterlizenzBE();
        kampfrichterlizenzBE.setLizenzDsbMitgliedId(dsbMitgliedDO.getId());
        kampfrichterlizenzBE.setLizenztyp("Kampfrichter");
       // kampfrichterlizenzBE.setLizenzId((long)5);
        kampfrichterlizenzBE.setLizenzDisziplinId((long)0);
        kampfrichterlizenzBE.setLizenznummer("123456KL");
        kampfrichterlizenzBE.setLizenzRegionId((long)1);


        kampfrichterlizenzBE.setCreatedAtUtc(createdAtUtcTimestamp);
        kampfrichterlizenzBE.setCreatedByUserId(dsbMitgliedDO.getCreatedByUserId());
        kampfrichterlizenzBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        kampfrichterlizenzBE.setLastModifiedByUserId(dsbMitgliedDO.getLastModifiedByUserId());
        kampfrichterlizenzBE.setVersion(dsbMitgliedDO.getVersion());

        return kampfrichterlizenzBE;
    };


    /**
     * Private constructor
     */
    private KampfrichterlizenzMapper() {
        // empty private constructor
    }
}
