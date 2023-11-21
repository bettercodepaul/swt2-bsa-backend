package de.bogenliga.application.business.datatransfer.bl_model;


public class BL_MannschaftDBO {
    private int id;
    private int liga_id;
    private String mannr;
    private int saison_id;

    public BL_MannschaftDBO(int id, int liga_id, String mannr, int saison_id){
        this.id = id;
        this.liga_id = id;
        this.mannr = mannr;
        this.saison_id = saison_id;
    }


    public int getId() {
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


    public void setId(int id) {
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
