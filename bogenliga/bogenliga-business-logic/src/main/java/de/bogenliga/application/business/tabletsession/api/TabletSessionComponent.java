package de.bogenliga.application.business.tabletsession.api;

import java.util.List;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.tabletsession.api.types.TabletSessionDO;

/**
 * @author Kay Scheerer
 */

public interface TabletSessionComponent {
    List<TabletSessionDO> findAll();

    List<TabletSessionDO> findByWettkampfId(Long wettkampfid);

    List<MatchDO> getRelatedMatches(TabletSessionDO tabletSessionDO, MatchComponent matchComponent);

    List<TabletSessionDO> createInitialForWettkampf(Long wettkampfId, final MatchComponent matchComponent, Long currentUserId);

    TabletSessionDO addInitialData (Long wettkampfId, int scheibennummer, MatchComponent matchComponent);

    TabletSessionDO findByIdScheibennummer(Long wettkampfid, Long scheibenNr);

    TabletSessionDO create(TabletSessionDO sessionDO, Long currentUserId);

    TabletSessionDO update(TabletSessionDO tabletSessionDO, final Long currentUserId);

    void delete(TabletSessionDO tabletSessionDO, final Long currentUserId);
}
