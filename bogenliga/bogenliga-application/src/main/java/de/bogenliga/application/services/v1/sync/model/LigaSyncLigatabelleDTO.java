package de.bogenliga.application.services.v1.sync.model;

import java.util.Objects;
import de.bogenliga.application.common.service.types.DataTransferObject;

public class LigaSyncLigatabelleDTO implements DataTransferObject {
    private static final long serialVersionUID = 432721597574336871L;

    //Kritisch Hinterfragen... wir können nicht alle Funktionen offline beritstellen
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

    public Long getVeranstaltungId() {
        return veranstaltungId;
    }

    public void setVeranstaltungId(Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public String getVeranstaltungName() {
        return veranstaltungName;
    }

    public void setVeranstaltungName(String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
    }

    public Long getWettkampfId() {
        return wettkampfId;
    }

    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public Integer getWettkampfTag() {
        return wettkampfTag;
    }

    public void setWettkampfTag(Integer wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }

    public Long getMannschaftId() {
        return mannschaftId;
    }

    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }

    public String getMannschaftName() {
        return mannschaftName;
    }

    public void setMannschaftName(String mannschaftName) {
        this.mannschaftName = mannschaftName;
    }

    public Integer getMatchpkt() {
        return matchpkt;
    }

    public void setMatchpkt(Integer matchpkt) {
        this.matchpkt = matchpkt;
    }

    public Integer getMatchpktGegen() {
        return matchpktGegen;
    }

    public void setMatchpktGegen(Integer matchpktGegen) {
        this.matchpktGegen = matchpktGegen;
    }

    public Integer getSatzpkt() {
        return satzpkt;
    }

    public void setSatzpkt(Integer satzpkt) {
        this.satzpkt = satzpkt;
    }

    public Integer getSatzpktGegen() {
        return satzpktGegen;
    }

    public void setSatzpktGegen(Integer satzpktGegen) {
        this.satzpktGegen = satzpktGegen;
    }

    public Integer getSatzpktDifferenz() {
        return satzpktDifferenz;
    }

    public void setSatzpktDifferenz(Integer satzpktDifferenz) {
        this.satzpktDifferenz = satzpktDifferenz;
    }

    public Integer getSortierung() {
        return sortierung;
    }

    public void setSortierung(Integer sortierung) {
        this.sortierung = sortierung;
    }

    public Integer getTabellenplatz() {
        return tabellenplatz;
    }

    public void setTabellenplatz(Integer tabellenplatz) {
        this.tabellenplatz = tabellenplatz;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigaSyncLigatabelleDTO)) {
            return false;
        }
        LigaSyncLigatabelleDTO that = (LigaSyncLigatabelleDTO) o;
        return Objects.equals(getVeranstaltungId(), that.getVeranstaltungId()) && Objects.equals(
                getVeranstaltungName(), that.getVeranstaltungName()) && Objects.equals(getWettkampfId(),
                that.getWettkampfId()) && Objects.equals(getWettkampfTag(),
                that.getWettkampfTag()) && Objects.equals(getMannschaftId(),
                that.getMannschaftId()) && Objects.equals(getMannschaftName(),
                that.getMannschaftName()) && Objects.equals(getMatchpkt(),
                that.getMatchpkt()) && Objects.equals(getMatchpktGegen(),
                that.getMatchpktGegen()) && Objects.equals(getSatzpkt(),
                that.getSatzpkt()) && Objects.equals(getSatzpktGegen(),
                that.getSatzpktGegen()) && Objects.equals(getSatzpktDifferenz(),
                that.getSatzpktDifferenz()) && Objects.equals(getSortierung(),
                that.getSortierung()) && Objects.equals(getTabellenplatz(), that.getTabellenplatz());
    }


    @Override
    public int hashCode() {
        int result = this.veranstaltungId != null ? this.veranstaltungId.hashCode() : 0;
        result = 31 * result + (this.veranstaltungName != null ? this.veranstaltungName.hashCode() : 0);
        result = 31 * result + (this.wettkampfId != null ? this.wettkampfId.hashCode() : 0);
        result = 31 * result + (this.wettkampfTag != null ? this.wettkampfTag.hashCode() : 0);
        result = 31 * result + (this.mannschaftId != null ? this.mannschaftId.hashCode() : 0);
        result = 31 * result + (this.mannschaftName != null ? this.mannschaftName.hashCode() : 0);
        result = 31 * result + (this.matchpkt != null ? this.matchpkt.hashCode() : 0);
        result = 31 * result + (this.matchpktGegen != null ? this.matchpktGegen.hashCode() : 0);
        result = 31 * result + (this.satzpkt != null ? this.satzpkt.hashCode() : 0);
        result = 31 * result + (this.satzpktGegen != null ? this.satzpktGegen.hashCode() : 0);
        result = 31 * result + (this.satzpktDifferenz != null ? this.satzpktDifferenz.hashCode() : 0);
        result = 31 * result + (this.sortierung != null ? this.sortierung.hashCode() : 0);
        result = 31 * result + (this.tabellenplatz != null ? this.tabellenplatz.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LigaSyncLigatabelleDTO{" +
                        "                            veranstaltungId=" + this.veranstaltungId +
                        ",                             veranstaltungName='" + this.veranstaltungName + '\'' +
                        ",                             wettkampfId=" + this.wettkampfId +
                        ",                             wettkampfTag=" + this.wettkampfTag +
                        ",                             mannschaftId=" + this.mannschaftId +
                        ",                             mannschaftName='" + this.mannschaftName + '\'' +
                        ",                             matchpkt=" + this.matchpkt +
                        ",                             matchpktGegen=" + this.matchpktGegen +
                        ",                             satzpkt=" + this.satzpkt +
                        ",                             satzpktGegen=" + this.satzpktGegen +
                        ",                             satzpktDifferenz=" + this.satzpktDifferenz +
                        ",                             sortierung=" + this.sortierung +
                        ",                             tabellenplatz=" + this.tabellenplatz +
                        '}';
    }
}
