package de.bogenliga.application.business.tabletsession.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.tabletsession.api.types.TabletSessionDO;
import de.bogenliga.application.business.tabletsession.impl.entity.TabletSessionBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * @author Kay Scheerer
 */
public class TabletSessionMapper implements ValueObjectMapper {

    /**
     * Converts a {@link TabletSessionDO} to a {@link TabletSessionBE}
     */
    public static final Function<TabletSessionBE, TabletSessionDO> toTabletSessionDO = be -> {

        final Long id = be.getWettkampfId();
        final Long scheibennr = be.getScheibennummer();
        final Long satzNr = be.getSatznummer();
        final Long matchId = be.getMatchId();
        final Boolean active = be.isActive();
        final Long accessToken = be.getAccessToken();

        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new TabletSessionDO(id, scheibennr, satzNr, matchId, active, accessToken, createdAtUtc, createdByUserId, lastModifiedAtUtc,
                lastModifiedByUserId, version);
    };

    /**
     * Converts a {@link TabletSessionDO} to a {@link TabletSessionBE}
     */
    public static final Function<TabletSessionDO, TabletSessionBE> toTabletSessionBE = vo -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getLastModifiedAtUtc());

        TabletSessionBE tabBE = new TabletSessionBE();
        tabBE.setWettkampfId(vo.getWettkampfId());
        tabBE.setScheibennummer(vo.getScheibennummer());
        tabBE.setSatznummer(vo.getSatznummer());
        tabBE.setMatchId(vo.getMatchId());
        tabBE.setActive(vo.isActive());
        tabBE.setAccessToken(vo.getAccessToken());

        tabBE.setCreatedAtUtc(createdAtUtcTimestamp);
        tabBE.setCreatedByUserId(vo.getCreatedByUserId());
        tabBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        tabBE.setLastModifiedByUserId(vo.getLastModifiedByUserId());
        tabBE.setVersion(vo.getVersion());

        return tabBE;
    };


    // private so it can not be instanciated
    private TabletSessionMapper() {
    }
}
