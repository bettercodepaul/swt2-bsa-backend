package de.bogenliga.application.business.altsystem.schuetze.dataobject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemSchuetzeDO {

    private int id;
    private int mannschaft_id;
    private int ruecknr;
    private String name;

    public AltsystemSchuetzeDO() {

    }

    public int getId() {
        return id;
    }


    public int getMannschaft_id() {
        return mannschaft_id;
    }


    public int getRuecknr() {
        return ruecknr;
    }


    public String getName() {
        return name;
    }
}
