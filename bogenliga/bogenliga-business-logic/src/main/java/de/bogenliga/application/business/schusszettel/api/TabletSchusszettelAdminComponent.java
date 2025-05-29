package de.bogenliga.application.business.schusszettel.api;

import de.bogenliga.application.business.schusszettel.api.types.TabletSessionInfoDO;

public interface TabletSchusszettelAdminComponent {

    // ---- ADMIN FUNCTION IMPLS, either hooked into existing code or as getters ----

    void initializeForWettkampf(long wettkampfId);

    void deleteForWettkampf(long wettkampfId);

    boolean existsForWettkampf(long wettkampfId);

    /**
     * Re-tokenizes the schusszettel for a given wettkampf and team.
     */
    void reTokenize(long wettkampfId, long teamId);

    /**
     * Generates a TabletSessionInfoDO for a given wettkampfId.
     * - WettkampfId
     * Per Team:
     * - TeamId
     * - TeamName
     * - Status
     * - Token
     */
    TabletSessionInfoDO generateSchusszettelSessions(long wettkampfId);
}
