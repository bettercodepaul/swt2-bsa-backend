package de.bogenliga.application.business.altsystem.ergebnisse.dataobject;

import de.bogenliga.application.common.altsystem.AltsystemDO;

/**
 * Bean to store the data of the entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemErgebnisseDO extends AltsystemDO {

    private Long schuetze_id =null;
    private int match = 0;
    private int ergebniss = 0;


    public Long getSchuetze_Id() { return schuetze_id; }

    public void setSchuetze_Id( Long schuetze_id) {
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