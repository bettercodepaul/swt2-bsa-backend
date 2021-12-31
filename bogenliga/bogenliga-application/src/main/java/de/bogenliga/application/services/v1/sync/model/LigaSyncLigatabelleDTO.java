package de.bogenliga.application.services.v1.sync.model;

public class LigaSyncLigatabelleDTO {
    //Kritisch Hinterfragen... wir können nicht alle Funktionen offlien beritstellen
    // Annahme: für die Steuerung im Wettkampf benötigen die Teams aktuelle Rückmeldung über ihre
    // derzeitige Tabellenposition - aber nur über diese.
    // wir lesen Ligatabelle intial und berechnen die aktuelle Ligatabelle im Client auf Basis der
    // Daten von Ligamatch in Summe mit Ligatabelle.
    private Long veranstaltungId;
    private String veranstaltungName;
    private Long wettkampfId;
    private Integer wettkampfTag;
    private Long mannschaftId;
    private String mannschaftName;
    private Integer matchpkt;
    private Integer matchpktGegen;
    private Integer satzpkt;
    private Integer satzpktGegen;
    private Integer satzpktDifferenz;
    private Integer sortierung;
    private Integer tabellenplatz;

    public LigaSyncLigatabelleDTO(){

    }

    public LigaSyncLigatabelleDTO(Long veranstaltungId, String veranstaltungName,
                                  Long wettkampfId, Integer wettkampfTag,
                                  Long mannschaftId, String mannschaftName) {
        this.veranstaltungId = veranstaltungId;
        this.veranstaltungName = veranstaltungName;
        this.wettkampfId = wettkampfId;
        this.wettkampfTag = wettkampfTag;
        this.mannschaftId = mannschaftId;
        this.mannschaftName = mannschaftName;
        this.matchpkt =0;
        this.matchpktGegen = 0;
        this.satzpkt =0;
        this.satzpktGegen=0;
        this.satzpktDifferenz=0;
        this.sortierung=0;
        this.tabellenplatz=0;
    }

    public LigaSyncLigatabelleDTO(Long veranstaltungId, String veranstaltungName,
                                  Long wettkampfId, Integer wettkampfTag,
                                  Long mannschaftId, String mannschaftName,
                                  Integer matchpkt, Integer matchpktGegen,
                                  Integer satzpkt, Integer satzpktGegen,
                                  Integer satzpktDifferenz, Integer sortierung,
                                  Integer tabellenplatz) {
        this.veranstaltungId = veranstaltungId;
        this.veranstaltungName = veranstaltungName;
        this.wettkampfId = wettkampfId;
        this.wettkampfTag = wettkampfTag;
        this.mannschaftId = mannschaftId;
        this.mannschaftName = mannschaftName;
        this.matchpkt = matchpkt;
        this.matchpktGegen = matchpktGegen;
        this.satzpkt = satzpkt;
        this.satzpktGegen = satzpktGegen;
        this.satzpktDifferenz = satzpktDifferenz;
        this.sortierung = sortierung;
        this.tabellenplatz = tabellenplatz;
    }
}
