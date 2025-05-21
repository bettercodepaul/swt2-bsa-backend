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
}
