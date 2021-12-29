package de.bogenliga.application.services.v1.sync.model;

public class LigamatchSyncDTO {
    private Long ligamatchMatchWettkampfId;
    private Long ligamatchMatchId;
    private Integer ligamatchMatchNr;
    private Integer liagmatchMatchScheibennummer;
    private Long ligamatchMatchMannschaftId;
    private String ligamatchMannschaftName;
    private String ligamatchNameGegner;
    private Integer ligamatchScheibennumerGegner;
    private Long ligamatchMatchIdGegner;
    private Long ligamatchNaechsteMatchId;
    private Long ligamatchNaechsteNaechsteMatchNrMatchId;
    private Integer ligamatchMatchStrafpunkteSatz1;
    private Integer ligamatchMatchStrafpunkteSatz2;
    private Integer ligamatchMatchStrafpunkteSatz3;
    private Integer ligamatchMatchStrafpunkteSatz4;
    private Integer ligamatchMatchStrafpunkteSatz5;

    public LigamatchSyncDTO(){

    }

    // Constrctor mit Fehlerpunkte = 0
    public LigamatchSyncDTO(Long ligamatchMatchWettkampfId, Long ligamatchMatchId,
                            Integer ligamatchMatchNr, Integer liagmatchMatchScheibennummer,
                            Long ligamatchMatchMannschaftId, String ligamatchMannschaftName,
                            String ligamatchNameGegner, Integer ligamatchScheibennumerGegner,
                            Long ligamatchMatchIdGegner, Long ligamatchNaechsteMatchId,
                            Long ligamatchNaechsteNaechsteMatchNrMatchId) {
        this.ligamatchMatchWettkampfId = ligamatchMatchWettkampfId;
        this.ligamatchMatchId = ligamatchMatchId;
        this.ligamatchMatchNr = ligamatchMatchNr;
        this.liagmatchMatchScheibennummer = liagmatchMatchScheibennummer;
        this.ligamatchMatchMannschaftId = ligamatchMatchMannschaftId;
        this.ligamatchMannschaftName = ligamatchMannschaftName;
        this.ligamatchNameGegner = ligamatchNameGegner;
        this.ligamatchScheibennumerGegner = ligamatchScheibennumerGegner;
        this.ligamatchMatchIdGegner = ligamatchMatchIdGegner;
        this.ligamatchNaechsteMatchId = ligamatchNaechsteMatchId;
        this.ligamatchNaechsteNaechsteMatchNrMatchId = ligamatchNaechsteNaechsteMatchNrMatchId;
        this.ligamatchMatchStrafpunkteSatz1 = 0;
        this.ligamatchMatchStrafpunkteSatz2 = 0;
        this.ligamatchMatchStrafpunkteSatz3 = 0;
        this.ligamatchMatchStrafpunkteSatz4 = 0;
        this.ligamatchMatchStrafpunkteSatz5 = 0;
    }

    public LigamatchSyncDTO(Long ligamatchMatchWettkampfId, Long ligamatchMatchId,
                            Integer ligamatchMatchNr, Integer liagmatchMatchScheibennummer,
                            Long ligamatchMatchMannschaftId, String ligamatchMannschaftName,
                            String ligamatchNameGegner, Integer ligamatchScheibennumerGegner,
                            Long ligamatchMatchIdGegner, Long ligamatchNaechsteMatchId,
                            Long ligamatchNaechsteNaechsteMatchNrMatchId,
                            Integer ligamatchMatchStrafpunkteSatz1, Integer ligamatchMatchStrafpunkteSatz2,
                            Integer ligamatchMatchStrafpunkteSatz3, Integer ligamatchMatchStrafpunkteSatz4,
                            Integer ligamatchMatchStrafpunkteSatz5) {
        this.ligamatchMatchWettkampfId = ligamatchMatchWettkampfId;
        this.ligamatchMatchId = ligamatchMatchId;
        this.ligamatchMatchNr = ligamatchMatchNr;
        this.liagmatchMatchScheibennummer = liagmatchMatchScheibennummer;
        this.ligamatchMatchMannschaftId = ligamatchMatchMannschaftId;
        this.ligamatchMannschaftName = ligamatchMannschaftName;
        this.ligamatchNameGegner = ligamatchNameGegner;
        this.ligamatchScheibennumerGegner = ligamatchScheibennumerGegner;
        this.ligamatchMatchIdGegner = ligamatchMatchIdGegner;
        this.ligamatchNaechsteMatchId = ligamatchNaechsteMatchId;
        this.ligamatchNaechsteNaechsteMatchNrMatchId = ligamatchNaechsteNaechsteMatchNrMatchId;
        this.ligamatchMatchStrafpunkteSatz1 = ligamatchMatchStrafpunkteSatz1;
        this.ligamatchMatchStrafpunkteSatz2 = ligamatchMatchStrafpunkteSatz2;
        this.ligamatchMatchStrafpunkteSatz3 = ligamatchMatchStrafpunkteSatz3;
        this.ligamatchMatchStrafpunkteSatz4 = ligamatchMatchStrafpunkteSatz4;
        this.ligamatchMatchStrafpunkteSatz5 = ligamatchMatchStrafpunkteSatz5;
    }
}
