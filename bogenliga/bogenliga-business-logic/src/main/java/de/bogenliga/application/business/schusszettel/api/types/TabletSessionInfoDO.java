package de.bogenliga.application.business.schusszettel.api.types;

import de.bogenliga.application.business.schusszettel.api.types.inside.TabletSessionSingDO;

/**
 * Enthält die Sammlung aller Sessions an einem WettkampfTag.
 *
 * Attributes:
 * * - WettkampfId
 *      * Per Team:
 *      * - TeamId
 *      * - TeamName
 *      * - Status
 *      * - Token
 *      * - CurrentPasse
 *      * - NächsterGegnerName (can be null)
 *
 * @author Marty Lauterbach
 */
public class TabletSessionInfoDO {
    // attribute wettkampfID
    private long wettkampfId;

    // List of TabletSessionSingDTO
    private TabletSessionSingDO[] tabletSessionSingDOs;

    // Getter/Setter

    public long getWettkampfId() {
        return wettkampfId;
    }

    public void setWettkampfId(long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public TabletSessionSingDO[] getTabletSessionSingDOs() {
        return tabletSessionSingDOs;
    }

    public void setTabletSessionSingDOs(TabletSessionSingDO[] tabletSessionSingDOs) {
        this.tabletSessionSingDOs = tabletSessionSingDOs;
    }
}
