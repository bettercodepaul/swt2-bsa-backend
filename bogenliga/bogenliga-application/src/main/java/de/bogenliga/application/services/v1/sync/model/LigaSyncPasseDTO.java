package de.bogenliga.application.services.v1.sync.model;


// Class für Offline Funktionalität der Ergebnisserfassung
//WICHTIG: im Offline Modus (Client) darf die PasseID kein Pflichtfeld sein
// diese Daten werden im Client neu generiert und müssen dort
// über die anderen IDs eindeutig gelesen/geschrieben werden
// die IDs werden bei Snyc zum Backend dann initial vergeben.


import de.bogenliga.application.common.service.types.DataTransferObject;

public class LigaSyncPasseDTO implements DataTransferObject {
    private static final long serialVersionUID = -3582493648923927523L;

    private Long id;
    private Long version;
    private Long matchId;
    private Long mannschaftId;
    private Long wettkampfId;
    private Long lfdNr;
    private Long dsbMitgliedId;
    private String dsbMitgliedName;
    private Integer[] ringzahl;
    private Integer rueckennummer;

    public LigaSyncPasseDTO(){

    }

    public LigaSyncPasseDTO(Long id, Long version, Long matchId, Long mannschaftId,
                            Long wettkampfId, Long lfdNr, Long dsbMitgliedId,
                            Integer[] ringzahl) {
        this.id = id;
        this.version = version;
        this.matchId = matchId;
        this.mannschaftId = mannschaftId;
        this.wettkampfId = wettkampfId;
        this.lfdNr = lfdNr;
        this.dsbMitgliedId = dsbMitgliedId;
        this.ringzahl = ringzahl;
    }

    //Original Constructor
    public LigaSyncPasseDTO(Long id, Long version, Long matchId, Long mannschaftId,
                            Long wettkampfId, Long lfdNr, Long dsbMitgliedId,
                            String dsbMitgliedName, Integer rueckennummer,
                            Integer[] ringzahl) {
        this.id = id;
        this.version = version;
        this.matchId = matchId;
        this.mannschaftId = mannschaftId;
        this.wettkampfId = wettkampfId;
        this.lfdNr = lfdNr;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedName = dsbMitgliedName;
        this.rueckennummer = rueckennummer;
        this.ringzahl = ringzahl;
    }

    public String getDsbMitgliedName() {
        return dsbMitgliedName;
    }
    public void setDsbMitgliedName(String dsbMitgliedName) {
        this.dsbMitgliedName = dsbMitgliedName;
    }
    public Integer getRueckennummer() {
        return rueckennummer;
    }
    public void setRueckennummer(Integer rueckennummer) {
        this.rueckennummer = rueckennummer;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getVersion() {
        return version;
    }
    public void setVersion(Long version) {
        this.version = version;
    }
    public Long getMatchId() {
        return matchId;
    }
    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }
    public Long getMannschaftId() {
        return mannschaftId;
    }
    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }
    public Long getWettkampfId() {
        return wettkampfId;
    }
    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }
    public Long getLfdNr() {
        return lfdNr;
    }
    public void setLfdNr(Long lfdNr) {
        this.lfdNr = lfdNr;
    }
    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }
    public void setDsbMitgliedId(Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }
    public Integer[] getRingzahl() {
        return ringzahl;
    }
    public void setRingzahl(Integer[] ringzahl) {
        this.ringzahl = ringzahl;
    }
}

