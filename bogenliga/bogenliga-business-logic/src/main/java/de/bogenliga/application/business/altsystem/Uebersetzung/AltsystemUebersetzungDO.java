package de.bogenliga.application.business.altsystem.Uebersetzung;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemUebersetzungDO {



    int uebersetzung_id;
    String kategorie;
    int altsystem_id;
    int bogenliga_id;
    String value;


    public int getUebersetzung_id() {
        return uebersetzung_id;
    }


    public void setUebersetzung_id(int uebersetzung_id) {
        this.uebersetzung_id = uebersetzung_id;
    }


    public String getKategorie() {
        return kategorie;
    }


    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }


    public int getAltsystem_id() {
        return altsystem_id;
    }


    public void setAltsystem_id(int altsystem_id) {
        this.altsystem_id = altsystem_id;
    }


    public int getBogenliga_id() {
        return bogenliga_id;
    }


    public void setBogenliga_id(int bogenliga_id) {
        this.bogenliga_id = bogenliga_id;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }




}
