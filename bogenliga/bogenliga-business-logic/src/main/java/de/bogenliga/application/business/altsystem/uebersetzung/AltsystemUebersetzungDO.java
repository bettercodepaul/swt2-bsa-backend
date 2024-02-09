package de.bogenliga.application.business.altsystem.uebersetzung;

/**
 * Datenobjekt für Datensätze in der Übersetzungstabelle
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemUebersetzungDO {

    private Long uebersetzungId;
    private String kategorie;
    private Long altsystemId;
    private Long bogenligaId;
    private String wert;


    public Long getUebersetzungId() {
        return uebersetzungId;
    }


    public void setUebersetzungId(Long uebersetzungId) {
        this.uebersetzungId = uebersetzungId;
    }


    public String getKategorie() {
        return kategorie;
    }


    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }


    public Long getAltsystemId() {
        return altsystemId;
    }


    public void setAltsystemId(Long altsystemId) {
        this.altsystemId = altsystemId;
    }


    public Long getBogenligaId() {
        return bogenligaId;
    }


    public void setBogenligaId(Long bogenligaId) {
        this.bogenligaId = bogenligaId;
    }


    public String getWert() {
        return wert;
    }


    public void setWert(String wert) {
        this.wert = wert;
    }


}
