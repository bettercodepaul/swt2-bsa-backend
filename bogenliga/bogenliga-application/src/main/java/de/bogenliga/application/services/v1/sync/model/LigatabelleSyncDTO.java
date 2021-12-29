package de.bogenliga.application.services.v1.sync.model;

public class LigatabelleSyncDTO {
    //Kritisch Hinterfragen... wir können nicht alle Funktionen offlien beritstellen
    // Annahme: für die Steuerung im Wettkampf benötigen die Teams aktuelle Rückmeldung über ihre
    // derzeitige Tabellenposition - aber nur über diese.
    // wir lesen Ligatabelle intial und berechnen die aktuelle Ligatabelle im Client auf Basis der
    // Daten von Ligamatch in Summe mit Ligatabelle.
    private Long ligatabelleVeranstaltungId;
    private String ligatabelleVeranstaltungName;
    private Long ligatabelleWettkampfId;
    private Integer ligatabelleWettkampfTag;
    private Long ligatabelleMannschaftId;
    private Integer ligatabelleMannschaftNummer;
    private Long ligatabelleVereinId;
    private String ligatabelleVereinName;
    private Integer ligatabelleMatchpkt;
    private Integer ligatabelleMatchpktGegen;
    private Integer ligatabelleSatzpkt;
    private Integer ligatabelleSatzpktGegen;
    private Integer ligatabelleSatzpktDifferenz;
    private Integer ligatabelleSortierung;

    public LigatabelleSyncDTO(){

    }

    public LigatabelleSyncDTO(Long ligatabelleVeranstaltungId, String ligatabelleVeranstaltungName,
                              Long ligatabelleWettkampfId, Integer ligatabelleWettkampfTag,
                              Long ligatabelleMannschaftId, Integer ligatabelleMannschaftNummer,
                              Long ligatabelleVereinId, String ligatabelleVereinName,
                              Integer ligatabelleMatchpkt, Integer ligatabelleMatchpktGegen,
                              Integer ligatabelleSatzpkt, Integer ligatabelleSatzpktGegen,
                              Integer ligatabelleSatzpktDifferenz, Integer ligatabelleSortierung) {
        this.ligatabelleVeranstaltungId = ligatabelleVeranstaltungId;
        this.ligatabelleVeranstaltungName = ligatabelleVeranstaltungName;
        this.ligatabelleWettkampfId = ligatabelleWettkampfId;
        this.ligatabelleWettkampfTag = ligatabelleWettkampfTag;
        this.ligatabelleMannschaftId = ligatabelleMannschaftId;
        this.ligatabelleMannschaftNummer = ligatabelleMannschaftNummer;
        this.ligatabelleVereinId = ligatabelleVereinId;
        this.ligatabelleVereinName = ligatabelleVereinName;
        this.ligatabelleMatchpkt = ligatabelleMatchpkt;
        this.ligatabelleMatchpktGegen = ligatabelleMatchpktGegen;
        this.ligatabelleSatzpkt = ligatabelleSatzpkt;
        this.ligatabelleSatzpktGegen = ligatabelleSatzpktGegen;
        this.ligatabelleSatzpktDifferenz = ligatabelleSatzpktDifferenz;
        this.ligatabelleSortierung = ligatabelleSortierung;
    }

    //constructor ohne match- und satzdaten
    public LigatabelleSyncDTO(Long ligatabelleVeranstaltungId, String ligatabelleVeranstaltungName,
                              Long ligatabelleWettkampfId, Integer ligatabelleWettkampfTag,
                              Long ligatabelleMannschaftId, Integer ligatabelleMannschaftNummer,
                              Long ligatabelleVereinId, String ligatabelleVereinName) {
        this.ligatabelleVeranstaltungId = ligatabelleVeranstaltungId;
        this.ligatabelleVeranstaltungName = ligatabelleVeranstaltungName;
        this.ligatabelleWettkampfId = ligatabelleWettkampfId;
        this.ligatabelleWettkampfTag = ligatabelleWettkampfTag;
        this.ligatabelleMannschaftId = ligatabelleMannschaftId;
        this.ligatabelleMannschaftNummer = ligatabelleMannschaftNummer;
        this.ligatabelleVereinId = ligatabelleVereinId;
        this.ligatabelleVereinName = ligatabelleVereinName;
    }
}
