package de.bogenliga.application.services.v1.sync.model;


// Class für Offline Funktionalität der Ergebnisserfassung
//WICHTIG: im Offline Modus (Client) darf die PasseID kein Pflichtfeld sein
// diese Daten werden im Client neu generiert und müssen dort
// über die anderen IDs eindeutig gelesen/geschrieben werden
// die IDs werden bei Snyc zum Backend dann initial vergeben.

public class LigapasseSyncDTO {
    private static final long serialVersionUID = -3582493648923927523L;

    private Long ligapasseWettkampfId;
    private Long ligapasseMatchId;
    private Long ligapassePasseId;
    private Integer ligapassePasseLfndr;
    private Long ligapassePasseMannschaftId;
    private Long ligapasseDsbMitgliedId;
    private String ligapasseDsbMigliedName;
    private Integer ligapasseMannschaftsmitgliedRueckennummer;
    private Integer ligapassePasseRingzahlPfeil1;
    private Integer ligapassePasseRingzahlPfeil2;

    public LigapasseSyncDTO(){

    }

    public LigapasseSyncDTO(Long ligapasseWettkampfId,
                            Long ligapasseMatchId,
                            Long ligapassePasseId,
                            Integer ligapassePasseLfndr,
                            Long ligapassePasseMannschaftId,
                            Long ligapasseDsbMitgliedId,
                            String ligapasseDsbMigliedName,
                            Integer ligapasseMannschaftsmitgliedRueckennummer,
                            Integer ligapassePasseRingzahlPfeil1,
                            Integer ligapassePasseRingzahlPfeil2) {
        this.ligapasseWettkampfId = ligapasseWettkampfId;
        this.ligapasseMatchId = ligapasseMatchId;
        this.ligapassePasseId = ligapassePasseId;
        this.ligapassePasseLfndr = ligapassePasseLfndr;
        this.ligapassePasseMannschaftId = ligapassePasseMannschaftId;
        this.ligapasseDsbMitgliedId = ligapasseDsbMitgliedId;
        this.ligapasseDsbMigliedName = ligapasseDsbMigliedName;
        this.ligapasseMannschaftsmitgliedRueckennummer = ligapasseMannschaftsmitgliedRueckennummer;
        this.ligapassePasseRingzahlPfeil1 = ligapassePasseRingzahlPfeil1;
        this.ligapassePasseRingzahlPfeil2 = ligapassePasseRingzahlPfeil2;
    }
}

