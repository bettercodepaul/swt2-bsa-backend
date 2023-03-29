package de.bogenliga.application.services.v1.sync.model;


// Class für Offline Funktionalität der Ergebnisserfassung
//WICHTIG: im Offline Modus (Client) darf die PasseID kein Pflichtfeld sein
// diese Daten werden im Client neu generiert und müssen dort
// über die anderen IDs eindeutig gelesen/geschrieben werden
// die IDs werden bei Snyc zum Backend dann initial vergeben.


import java.util.Arrays;
import java.util.Objects;
import de.bogenliga.application.common.service.types.DataTransferObject;

public class LigaSyncPasseDTO implements DataTransferObject {
    private static final long serialVersionUID = 8167439169583799814L;

    private Long id;
    private Long version;
    private Long matchId;
    private Long mannschaftId;
    private Long wettkampfId;
    private Long lfdNr;
    private Long dsbMitgliedId;
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

    // Konstruktor mit allen Attributen
    public LigaSyncPasseDTO(Long id, Long version, Long matchId, Long mannschaftId,
                            Long wettkampfId, Long lfdNr, Long dsbMitgliedId,
                            Integer rueckennummer,
                            Integer[] ringzahl) {
        this.id = id;
        this.version = version;
        this.matchId = matchId;
        this.mannschaftId = mannschaftId;
        this.wettkampfId = wettkampfId;
        this.lfdNr = lfdNr;
        this.dsbMitgliedId = dsbMitgliedId;
        this.rueckennummer = rueckennummer;
        this.ringzahl = ringzahl;
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


    @Override
    public String toString() {
        return "LigaSyncPasseDTO{" +
                "id=" + id +
                ", version=" + version +
                ", matchId=" + matchId +
                ", mannschaftId=" + mannschaftId +
                ", wettkampfId=" + wettkampfId +
                ", lfdNr=" + lfdNr +
                ", dsbMitgliedId=" + dsbMitgliedId +
                ", ringzahl=" + Arrays.toString(ringzahl) +
                ", rueckennummer=" + rueckennummer +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigaSyncPasseDTO)) {
            return false;
        }
        LigaSyncPasseDTO that = (LigaSyncPasseDTO) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVersion(),
                that.getVersion()) && Objects.equals(getMatchId(), that.getMatchId()) && Objects.equals(
                getMannschaftId(), that.getMannschaftId()) && Objects.equals(getWettkampfId(),
                that.getWettkampfId()) && Objects.equals(getLfdNr(), that.getLfdNr()) && Objects.equals(
                getDsbMitgliedId(), that.getDsbMitgliedId()) && Arrays.equals(getRingzahl(),
                that.getRingzahl()) && Objects.equals(getRueckennummer(), that.getRueckennummer());
    }


    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getVersion(), getMatchId(), getMannschaftId(), getWettkampfId(), getLfdNr(),
                getDsbMitgliedId(), getRueckennummer());
        result = 31 * result + Arrays.hashCode(getRingzahl());
        return result;
    }
}
