package de.bogenliga.application.business.altsystem.mannschaft.dataobject;


import de.bogenliga.application.common.altsystem.AltsystemDO;

public class AltsystemMannschaftDO extends AltsystemDO {

    private long id;
    private int liga_id;
    private String mannr;
    private String name;
    private int saison_id;





    public AltsystemMannschaftDO(int id, int liga_id, String mannr, int saison_id){
        this.id = id;
        this.liga_id = id;
        this.mannr = mannr;
        this.name = name;
        this.saison_id = saison_id;
    }


    public long getId() {
        return id;
    }


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

    public void setId(long id) {
        this.id = id;
    }


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
