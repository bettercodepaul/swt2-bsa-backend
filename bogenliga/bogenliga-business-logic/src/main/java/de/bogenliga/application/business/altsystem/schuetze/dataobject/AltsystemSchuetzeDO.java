package de.bogenliga.application.business.altsystem.schuetze.dataobject;

import de.bogenliga.application.common.altsystem.AltsystemDO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemSchuetzeDO extends AltsystemDO {

    private int mannschaft_id = 0;
    private int ruecknr = 0;
    private String name = null;

    public int getMannschaft_id() {
        return mannschaft_id;
    }


    public int getRuecknr() {
        return ruecknr;
    }


    public String getName() {
        return name;
    }


    public void setMannschaft_id(int mannschaft_id) {
        this.mannschaft_id = mannschaft_id;
    }


    public void setRuecknr(int ruecknr) {
        this.ruecknr = ruecknr;
    }


    public void setName(String name) {
        this.name = name;
    }
}
