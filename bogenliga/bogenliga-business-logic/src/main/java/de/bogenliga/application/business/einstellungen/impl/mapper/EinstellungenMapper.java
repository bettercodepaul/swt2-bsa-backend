package de.bogenliga.application.business.einstellungen.impl.mapper;

import java.util.function.Function;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.business.einstellungen.impl.entity.EinstellungenBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the einstellungen DataObjects and BusinessEntities I convert the {@link EinstellungenBE} and the {@link
 * EinstellungenDO}
 *
 * @author Fabio Care, fabio_silas.care@student.reutlingen-university.de
 */
public class EinstellungenMapper implements ValueObjectMapper {

    /**
     * Converts a {@link EinstellungenBE} to a {@link EinstellungenDO}
     */
    public static final Function<EinstellungenBE, EinstellungenDO> toDO = be -> {

        final Long id = be.getEinstellungenId();
        final String value = be.getEinstellungenValue();
        final String key = be.getEinstellungenKey();

        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new EinstellungenDO(id, key, value, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId,
                version);

    };

    /**
     * Converts a {@link EinstellungenDO} to a {@link EinstellungenBE}
     */
    public static final Function<EinstellungenDO, EinstellungenBE> toBE = einstellungenDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(einstellungenDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(
                einstellungenDO.getLastModifiedAtUtc());

        EinstellungenBE einstellungenBE = new EinstellungenBE();
        einstellungenBE.setEinstellungenId(einstellungenDO.getId());
        einstellungenBE.setEinstellungenValue(einstellungenDO.getValue());
        einstellungenBE.setEinstellungenKey(einstellungenDO.getKey());

        einstellungenBE.setCreatedAtUtc(createdAtUtcTimestamp);
        einstellungenBE.setCreatedByUserId(einstellungenDO.getCreatedByUserId());
        einstellungenBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        einstellungenBE.setLastModifiedByUserId(einstellungenDO.getLastModifiedByUserId());
        einstellungenBE.setVersion(einstellungenDO.getVersion());

        return einstellungenBE;
    };


    /**
     * Private constructor
     */
    private EinstellungenMapper() {
        //empty private constructor
    }
}