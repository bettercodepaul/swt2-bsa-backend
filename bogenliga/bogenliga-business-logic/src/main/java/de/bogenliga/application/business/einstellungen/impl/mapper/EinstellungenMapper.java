package de.bogenliga.application.business.einstellungen.impl.mapper;

import java.sql.Time;
import java.util.function.Function;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.business.einstellungen.impl.entity.EinstellungenBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the einstellungen DataObjects and BusinessEntities
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class EinstellungenMapper implements ValueObjectMapper {

    /**
     * Converts a {@link EinstellungenBE} to a {@link EinstellungenDO}
     *
     */
    public static final Function<EinstellungenBE, EinstellungenDO> toEinstellungenDO = be -> {

        final Long id = be.getId();

        final String value = be.getValue();
        final String key = be.getKey();

        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new EinstellungenDO(id, value, key, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);

    };

    /**
     * Converts a {@link EinstellungenDO} to a {@link EinstellungenBE}
     *
     */
    public static final Function<EinstellungenDO, EinstellungenBE> toEinstellungenBE = einstellungenDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(einstellungenDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(einstellungenDO.getLastModifiedAtUtc());

        EinstellungenBE einstellungenBE = new EinstellungenBE();
        einstellungenBE.setId(einstellungenDO.getId());
        einstellungenBE.setValue(einstellungenDO.getValue());
        einstellungenBE.setKey(einstellungenDO.getKey());

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