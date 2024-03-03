package de.bogenliga.application.business.altsystem.ergebnisse.dataobject;

import de.bogenliga.application.common.altsystem.AltsystemDO;

/**
 * Bean to store the data of the entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemErgebnisseDO extends AltsystemDO {

    private Long schuetzeID;
    private Long match;
    private Long ergebnis;


    public Long getMatch() {
        return match;
    }
    public void setMatch(long match) {
        this.match = match;
    }
    public Long getSchuetzeID() {
        return schuetzeID;
    }

    public void setSchuetzeID(Long schuetzeID) {
        this.schuetzeID = schuetzeID;
    }

    public Long getErgebnis() {
        return ergebnis;
    }

    public void setErgebnis(int ergebnis) {
        this.ergebnis = Long.valueOf(ergebnis);
    }
}