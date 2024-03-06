package de.bogenliga.application.business.altsystem.ergebnisse.dataobject;

import de.bogenliga.application.common.altsystem.AltsystemDO;

/**
 * Bean to store the data of the entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemErgebnisseDO extends AltsystemDO {

    private int schuetze_id;
    private int match;
    private int ergebniss;


    public int getSchuetze_Id() { return schuetze_id; }

    public void setSchuetze_Id( int schuetze_id) {
        this.schuetze_id = schuetze_id;
    }
    public int getMatch() {
        return match;
    }
    public void setMatch(int match) {
        this.match = match;
    }

    public int getErgebnis() {
        return ergebniss;
    }

    public void setErgebnis(int ergebniss) {
        this.ergebniss = (ergebniss);
    }
}