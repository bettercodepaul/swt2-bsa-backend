package de.bogenliga.application.business.schusszettel.api;

import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSessionInfoDO;

public interface TabletSchusszettelComponent {

    // ---- TABLET SESSION API CALLS IMPLs ----

    TabletSchusszettelDO getStatus(long wettkampfid, long teamid, String token);

    void submitSatz(long wettkampfid, long teamid, String token, SatzEingabeDO doObj);

    void submitSchuetzen(long wettkampfid, long teamid, String token, SchuetzenMeldungDO doObj);

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
