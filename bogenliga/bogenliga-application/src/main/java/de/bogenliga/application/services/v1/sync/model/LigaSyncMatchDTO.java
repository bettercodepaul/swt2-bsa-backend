package de.bogenliga.application.services.v1.sync.model;

import java.util.Objects;
import de.bogenliga.application.common.service.types.DataTransferObject;

public class LigaSyncMatchDTO implements DataTransferObject {
    private static final long serialVersionUID = 1276764785943730903L;

    private Long id; //match-Id
    private Long version;
    private Long wettkampfId;
    private Integer matchNr;
    private Integer matchScheibennummer;
    private Long matchpunkte;
    private Long satzpunkte;
    private Long mannschaftId;
    private String mannschaftName;
    private String nameGegner;
    private Integer scheibennummerGegner;
    private Long matchIdGegner;
    private Long naechsteMatchId;



    private Long naechsteNaechsteMatchNrMatchId;
    private Integer strafpunkteSatz1;
    private Integer strafpunkteSatz2;
    private Integer strafpunkteSatz3;
    private Integer strafpunkteSatz4;
    private Integer strafpunkteSatz5;

    public LigaSyncMatchDTO(){

    }

    public LigaSyncMatchDTO(Long id, Long version, Long wettkampfId,
                            Integer matchNr, Integer matchScheibennummer,
                            Long matchpunkte, Long satzpunkte,
                            Long mannschaftId, String mannschaftName,
                            String nameGegner, Integer scheibennummerGegner,
                            Long matchIdGegner, Long naechsteMatchId,
                            Long naechsteNaechsteMatchNrMatchId) {
        this.setId(id);
        this.setVersion(version);
        this.setWettkampfId(wettkampfId);
        this.setMatchNr(matchNr);
        this.setMatchScheibennummer(matchScheibennummer);
        this.setMatchpunkte(matchpunkte);
        this.setSatzpunkte(satzpunkte);
        this.setMannschaftId(mannschaftId);
        this.setMannschaftName(mannschaftName);
        this.setNameGegner(nameGegner);
        this.setScheibennummerGegner(scheibennummerGegner);
        this.setMatchIdGegner(matchIdGegner);
        this.setNaechsteMatchId(naechsteMatchId);
        this.setNaechsteNaechsteMatchNrMatchId(naechsteNaechsteMatchNrMatchId);
    }

    // Konstruktor mit allen Attributen
    public LigaSyncMatchDTO(Long id, Long version, Long wettkampfId,
                            Integer matchNr, Integer matchScheibennummer,
                            Long matchpunkte, Long satzpunkte,
                            Long mannschaftId, String mannschaftName,
                            String nameGegner, Integer scheibennummerGegner,
                            Long matchIdGegner, Long naechsteMatchId,
                            Long naechsteNaechsteMatchNrMatchId, Integer strafpunkteSatz1,
                            Integer strafpunkteSatz2, Integer strafpunkteSatz3,
                            Integer strafpunkteSatz4, Integer strafpunkteSatz5) {
        this(id, version, wettkampfId, matchNr, matchScheibennummer, matchpunkte, satzpunkte, mannschaftId, mannschaftName,
                nameGegner, scheibennummerGegner, matchIdGegner, naechsteMatchId, naechsteNaechsteMatchNrMatchId);
        this.setStrafpunkteSatz1(strafpunkteSatz1);
        this.setStrafpunkteSatz2(strafpunkteSatz2);
        this.setStrafpunkteSatz3(strafpunkteSatz3);
        this.setStrafpunkteSatz4(strafpunkteSatz4);
        this.setStrafpunkteSatz5(strafpunkteSatz5);
    }

    public void setMannschaftName(String mannschaftName) {
        this.mannschaftName = mannschaftName;
    }

    public void setNameGegner(String nameGegner){
        this.nameGegner = nameGegner;
    }

    public void setScheibennummerGegner(Integer scheibennummerGegner){
        this.scheibennummerGegner = scheibennummerGegner;
    }

    public void setMatchIdGegner(Long matchIdGegner){
        this.matchIdGegner = matchIdGegner;
    }

    public void setNaechsteMatchId(Long naechsteMatchId){
        this.naechsteMatchId = naechsteMatchId;

    }

    public void setNaechsteNaechsteMatchNrMatchId(Long naechsteNaechsteMatchNrMatchId){
        this.naechsteNaechsteMatchNrMatchId = naechsteNaechsteMatchNrMatchId;
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


    public Long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }


    public Integer getMatchNr() {return this.matchNr; }


    public void setMatchNr(Integer matchNr) {
        this.matchNr = matchNr;
    }


    public Integer getMatchScheibennummer() {
        return matchScheibennummer;
    }


    public void setMatchScheibennummer(Integer matchScheibennummer) {
        this.matchScheibennummer = matchScheibennummer;
    }

    public Long getMatchpunkte() {return matchpunkte; }


    public void setMatchpunkte(Long matchpunkte) {this.matchpunkte = matchpunkte;}


    public Long getSatzpunkte() {return satzpunkte; }


    public void setSatzpunkte(Long satzpunkte) {this.satzpunkte = satzpunkte; }


    public Long getMannschaftId() {
        return mannschaftId;
    }


    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }


    public String getMannschaftName() {
        return mannschaftName;
    }


    public String getNameGegner() {
        return nameGegner;
    }


    public Integer getScheibennummerGegner() {
        return scheibennummerGegner;
    }


    public Long getMatchIdGegner() {
        return matchIdGegner;
    }


    public Long getNaechsteMatchId() {
        return naechsteMatchId;
    }


    public Long getNaechsteNaechsteMatchNrMatchId() {
        return naechsteNaechsteMatchNrMatchId;
    }


    public Integer getStrafpunkteSatz1() {
        return strafpunkteSatz1;
    }


    public void setStrafpunkteSatz1(Integer strafpunkteSatz1) {
        this.strafpunkteSatz1 = strafpunkteSatz1;
    }


    public Integer getStrafpunkteSatz2() {
        return strafpunkteSatz2;
    }


    public void setStrafpunkteSatz2(Integer strafpunkteSatz2) {
        this.strafpunkteSatz2 = strafpunkteSatz2;
    }


    public Integer getStrafpunkteSatz3() {
        return strafpunkteSatz3;
    }


    public void setStrafpunkteSatz3(Integer strafpunkteSatz3) {
        this.strafpunkteSatz3 = strafpunkteSatz3;
    }


    public Integer getStrafpunkteSatz4() {return strafpunkteSatz4; }


    public void setStrafpunkteSatz4(Integer strafpunkteSatz4) {
        this.strafpunkteSatz4 = strafpunkteSatz4;
    }


    public Integer getStrafpunkteSatz5() {
        return strafpunkteSatz5;
    }


    public void setStrafpunkteSatz5(Integer strafpunkteSatz5) {
        this.strafpunkteSatz5 = strafpunkteSatz5;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigaSyncMatchDTO)) {
            return false;
        }
        LigaSyncMatchDTO that = (LigaSyncMatchDTO) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVersion(),
                that.getVersion()) && Objects.equals(getWettkampfId(),
                that.getWettkampfId()) && Objects.equals(getMatchNr(),
                that.getMatchNr()) && Objects.equals(getMatchScheibennummer(),
                that.getMatchScheibennummer()) && Objects.equals(getMatchpunkte(),
                that.getMatchpunkte()) && Objects.equals(getSatzpunkte(),
                that.getSatzpunkte()) && Objects.equals(getMannschaftId(),
                that.getMannschaftId()) && Objects.equals(getMannschaftName(),
                that.getMannschaftName()) && Objects.equals(getNameGegner(),
                that.getNameGegner()) && Objects.equals(getScheibennummerGegner(),
                that.getScheibennummerGegner()) && Objects.equals(getMatchIdGegner(),
                that.getMatchIdGegner()) && Objects.equals(getNaechsteMatchId(),
                that.getNaechsteMatchId()) && Objects.equals(getNaechsteNaechsteMatchNrMatchId(),
                that.getNaechsteNaechsteMatchNrMatchId()) && Objects.equals(getStrafpunkteSatz1(),
                that.getStrafpunkteSatz1()) && Objects.equals(getStrafpunkteSatz2(),
                that.getStrafpunkteSatz2()) && Objects.equals(getStrafpunkteSatz3(),
                that.getStrafpunkteSatz3()) && Objects.equals(getStrafpunkteSatz4(),
                that.getStrafpunkteSatz4()) && Objects.equals(getStrafpunkteSatz5(), that.getStrafpunkteSatz5());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVersion(), getWettkampfId(), getMatchNr(), getMatchScheibennummer(),
                getMatchpunkte(), getSatzpunkte(), getMannschaftId(), getMannschaftName(), getNameGegner(),
                getScheibennummerGegner(), getMatchIdGegner(), getNaechsteMatchId(),
                getNaechsteNaechsteMatchNrMatchId(),
                getStrafpunkteSatz1(), getStrafpunkteSatz2(), getStrafpunkteSatz3(), getStrafpunkteSatz4(),
                getStrafpunkteSatz5());
    }


    @Override
    public String toString() {
        return "LigaSyncMatchDTO{" +
                "id=" + id +
                ", version=" + version +
                ", wettkampfId=" + wettkampfId +
                ", matchNr=" + matchNr +
                ", matchScheibennummer=" + matchScheibennummer +
                ", matchpunkte=" + matchpunkte +
                ", satzpunkte=" + satzpunkte +
                ", mannschaftId=" + mannschaftId +
                ", mannschaftName='" + mannschaftName + '\'' +
                ", nameGegner='" + nameGegner + '\'' +
                ", scheibennummerGegner=" + scheibennummerGegner +
                ", matchIdGegner=" + matchIdGegner +
                ", naechsteMatchId=" + naechsteMatchId +
                ", naechsteNaechsteMatchNrMatchId=" + naechsteNaechsteMatchNrMatchId +
                ", strafpunkteSatz1=" + strafpunkteSatz1 +
                ", strafpunkteSatz2=" + strafpunkteSatz2 +
                ", strafpunkteSatz3=" + strafpunkteSatz3 +
                ", strafpunkteSatz4=" + strafpunkteSatz4 +
                ", strafpunkteSatz5=" + strafpunkteSatz5 +
                '}';
    }
}
