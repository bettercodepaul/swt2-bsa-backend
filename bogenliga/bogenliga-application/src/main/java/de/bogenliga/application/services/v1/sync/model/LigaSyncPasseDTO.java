package de.bogenliga.application.services.v1.sync.model;


// Class für Offline Funktionalität der Ergebnisserfassung
//WICHTIG: im Offline Modus (Client) darf die PasseID kein Pflichtfeld sein
// diese Daten werden im Client neu generiert und müssen dort
// über die anderen IDs eindeutig gelesen/geschrieben werden
// die IDs werden bei Snyc zum Backend dann initial vergeben.

public class LigaSyncPasseDTO {
    private static final long serialVersionUID = -3582493648923927523L;

    private Long id;
    private Long version;
    private Long matchId;
    private Long mannschaftId;
    private Long wettkampfId;
    private Integer lfdNr;
    private Long dsbMitgliedId;
    private String dsbMigliedName;
    private Integer rueckennummer;
    private Integer[] ringzahl;

    public LigaSyncPasseDTO(){

    }

    public LigaSyncPasseDTO(Long id, Long version, Long matchId, Long mannschaftId,
                            Long wettkampfId, Integer lfdNr, Long dsbMitgliedId,
                            String dsbMigliedName, Integer rueckennummer,
                            Integer[] ringzahl) {
        this.id = id;
        this.version = version;
        this.matchId = matchId;
        this.mannschaftId = mannschaftId;
        this.wettkampfId = wettkampfId;
        this.lfdNr = lfdNr;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMigliedName = dsbMigliedName;
        this.rueckennummer = rueckennummer;
        this.ringzahl = ringzahl;
    }
}

