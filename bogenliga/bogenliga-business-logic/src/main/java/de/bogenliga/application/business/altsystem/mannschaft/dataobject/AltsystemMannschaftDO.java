package de.bogenliga.application.business.altsystem.mannschaft.dataobject;


import de.bogenliga.application.common.altsystem.AltsystemDO;

public class AltsystemMannschaftDO extends AltsystemDO {

    private int liga_id = 0;
    private String mannr = null;
    private String name = null;
    private int saison_id = 0;

    public int getLiga_id() {
        return liga_id;
    }

    public String getMannr() {
        return mannr;
    }

    public int getSaison_id() {
        return saison_id;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public void setLiga_id(int liga_id) {
        this.liga_id = liga_id;
    }

    public void setMannr(String mannr) {
        this.mannr = mannr;
    }

    public void setSaison_id(int saison_id) {
        this.saison_id = saison_id;
    }


}
