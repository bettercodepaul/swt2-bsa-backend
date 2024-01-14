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

    public AltsystemSchuetzeDO(int id, int mannschaft_id, int ruecknr, String name){
        this.id = id;
        this.mannschaft_id = mannschaft_id;
        this.ruecknr = ruecknr;
        this.name = name;
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


    public void setId(int id) {
        this.id = id;
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
