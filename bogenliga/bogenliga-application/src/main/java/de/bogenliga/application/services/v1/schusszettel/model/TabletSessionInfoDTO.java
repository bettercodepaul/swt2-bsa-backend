package de.bogenliga.application.services.v1.schusszettel.model;

import de.bogenliga.application.services.v1.schusszettel.model.inside.TabletSessionSingDTO;

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
public class TabletSessionInfoDTO {

    // attribute wettkampfID
    private long wettkampfId;

    // List of TabletSessionSingDTO
    private TabletSessionSingDTO[] tabletSessionSingDTOs;

    // Getter/Setter

    public long getWettkampfId() {
        return wettkampfId;
    }

    public void setWettkampfId(long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public TabletSessionSingDTO[] getTabletSessionSingDTOs() {
        return tabletSessionSingDTOs;
    }

    public void setTabletSessionSingDTOs(TabletSessionSingDTO[] tabletSessionSingDTOs) {
        this.tabletSessionSingDTOs = tabletSessionSingDTOs;
    }
}
