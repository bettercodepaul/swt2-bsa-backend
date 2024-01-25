package de.bogenliga.application.business.altsystem.ergebnisse.dataobject;

import de.bogenliga.application.common.altsystem.AltsystemDO;

/**
 * Bean to store the data of the entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemErgebnisseDO extends AltsystemDO {

    private long schuetzeID;
    private int mannschaftsID;
    private String name;
    private int ergebnis;


    public long getSchuetzeID() {
        return schuetzeID;
    }

    public void setSchuetzeID(long schuetzeID) {
        this.schuetzeID = schuetzeID;
    }

    public int getMannschaftsID() {
        return mannschaftsID;
    }

    public void setMannschaftsID(int mannschaftsID) {
        this.mannschaftsID = mannschaftsID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getErgebnis() {
        return ergebnis;
    }

    public void setErgebnis(int ergebnis) {
        this.ergebnis = ergebnis;
    }
}