package de.bogenliga.application.business.schusszettel.api;

import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.business.schusszettel.api.types.TabletSchusszettelDO;

public interface TabletSchusszettelComponent {

    TabletSchusszettelDO getStatus(long wettkampfid, long teamid, String token);

    void submitSatz(long wettkampfid, long teamid, String token, SatzEingabeDO doObj);

    void submitSchuetzen(long wettkampfid, long teamid, String token, SchuetzenMeldungDO doObj);

    void initializeForWettkampf(long wettkampfId);

    void deleteForWettkampf(long wettkampfId);

    boolean existsForWettkampf(long wettkampfId);
}
