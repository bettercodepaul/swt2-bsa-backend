package de.bogenliga.application.business.altsystem.ergebnisse.dataobject;

import de.bogenliga.application.common.altsystem.AltsystemDO;

/**
 * Bean to store the data of the entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemErgebnisseDO extends AltsystemDO {

    private long schuetzeID;
    private long match;
    private long ergebnis;


    public long getMatch() {
        return match;
    }
    public void setMatch(long match) {
        this.match = match;
    }
    public long getSchuetzeID() {
        return schuetzeID;
    }

    public void setSchuetzeID(long schuetzeID) {
        this.schuetzeID = schuetzeID;
    }

    public long getErgebnis() {
        return ergebnis;
    }

    public void setErgebnis(int ergebnis) {
        this.ergebnis = ergebnis;
    }
}