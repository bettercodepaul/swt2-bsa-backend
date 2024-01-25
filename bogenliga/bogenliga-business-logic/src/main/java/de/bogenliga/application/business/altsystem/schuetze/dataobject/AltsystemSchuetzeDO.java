package de.bogenliga.application.business.altsystem.schuetze.dataobject;

import de.bogenliga.application.common.altsystem.AltsystemDO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemSchuetzeDO extends AltsystemDO {

    private int mannschaft_id;
    private int ruecknr;
    private String name;

    public long getMannschaft_id() {
        return mannschaft_id;
    }


    public int getRuecknr() {
        return ruecknr;
    }


    public String getName() {
        return name;
    }
}
