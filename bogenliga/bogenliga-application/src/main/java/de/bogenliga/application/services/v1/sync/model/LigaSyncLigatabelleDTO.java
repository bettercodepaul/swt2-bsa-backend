package de.bogenliga.application.services.v1.sync.model;

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
    private Integer matchCount;


    public LigaSyncLigatabelleDTO(Long veranstaltungId, String veranstaltungName,
                                  Long wettkampfId, Integer wettkampfTag,
                                  Long mannschaftId, String mannschaftName,
                                  Integer matchpkt, Integer matchpktGegen,
                                  Integer satzpkt, Integer satzpktGegen,
                                  Integer satzpktDifferenz, Integer sortierung,
                                  Integer tabellenplatz, Integer matchCount) {
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
        this.matchCount = matchCount;
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

    public Integer getMatchCount() {return matchCount;}

    public void setMatchCount(Integer matchCount) {this.matchCount = matchCount;}


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigaSyncLigatabelleDTO)) {
            return false;
        }
        LigaSyncLigatabelleDTO that = (LigaSyncLigatabelleDTO) o;

        Boolean condition1 = this.getVeranstaltungName().equals(that.getVeranstaltungName());
        Boolean condition2 = this.getVeranstaltungId().equals(that.getVeranstaltungId());
        Boolean condition3 = this.getWettkampfId().equals(that.getWettkampfId());
        Boolean condition4 = this.getWettkampfTag().equals(that.getWettkampfTag());
        Boolean condition5 = this.getMannschaftId().equals(that.getMannschaftId());
        Boolean condition6 = this.getMannschaftName().equals(that.getMannschaftName());
        Boolean condition7 = this.getMatchpkt().equals(that.getMatchpkt());
        Boolean condition8 = this.getMatchpktGegen().equals(that.getMatchpktGegen());
        Boolean condition9 = this.getSatzpkt().equals(that.getSatzpkt());
        Boolean condition10 = this.getSatzpktGegen().equals(that.getSatzpktGegen());
        Boolean condition11 = this.getSatzpktDifferenz().equals(that.getSatzpktDifferenz());
        Boolean condition12 = this.getSortierung().equals(that.getSortierung());
        Boolean condition13 = this.getMatchCount().equals(that.getMatchCount());

        return condition1 && condition2 && condition3
                && condition4 && condition5
                && condition6 && condition7
                && condition8 &&condition9
                && condition10 && condition11 && condition12
                && condition13;
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
        result = 31 * result + (this.matchCount != null ? this.matchCount.hashCode() : 0);
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
                        ",                             matchCount=" + this.matchCount +
                        '}';
    }
}
