package de.bogenliga.application.business.ligamatch.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * Im the Business Entity for a ligamatch. The Schusszettel gets this data from the View ligamatch
 * to increase perfomance.
 * This Entity will later be matched into a matchDO to send it to the frontend.
 *
 */
public class LigamatchBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 6766917935149997789L;
    
    private Long wettkampfId;
    private Long matchId;
    private Long matchNr;
    private Long scheibennummer;
    private Long mannschaftId;
    private Long begegnung;
    private Long naechsteMatchId;
    private Long naechsteNaechsteMatchNrMatchId;
    private Long strafpunkteSatz1;
    private Long strafpunkteSatz2;
    private Long strafpunkteSatz3;
    private Long strafpunkteSatz4;
    private Long strafpunkteSatz5;
    private String wettkampftypId;
    private Long wettkampfTag;
    private String mannschaftName;
    private Integer rueckennummer;




    public LigamatchBE() {
        //empty constructor
    }


    @Override
    public String toString() {
        return "LigamatchBE{" +
                "wettkampfId=" + wettkampfId +
                ", matchId=" + matchId +
                ", matchNr=" + matchNr +
                ", scheibennummer=" + scheibennummer +
                ", mannschaftId=" + mannschaftId +
                ",begegnung=" + begegnung +
                ", naechsteMatchId=" + naechsteMatchId +
                ", naechsteNaechsteMatchNrMatchId=" + naechsteNaechsteMatchNrMatchId +
                ", strafpunkteSatz1=" + strafpunkteSatz1 +
                ", strafpunkteSatz2=" + strafpunkteSatz2 +
                ", strafpunkteSatz3=" + strafpunkteSatz3 +
                ", strafpunkteSatz4=" + strafpunkteSatz4 +
                ", strafpunkteSatz5=" + strafpunkteSatz5 +
                '}';
    }


    public Long getWettkampfId() {return wettkampfId;}
    public void setWettkampfId(Long wettkampfId) {this.wettkampfId = wettkampfId;}

    public Long getMatchId() {return matchId;}
    public void setMatchId(Long matchId) {this.matchId = matchId;}

    public Long getMatchNr() {return matchNr;}
    public void setMatchNr(Long matchNr) {this.matchNr = matchNr;}

    public Long getScheibennummer() {return scheibennummer;}
    public void setScheibennummer(Long scheibennummer) {this.scheibennummer = scheibennummer;}

    public Long getMannschaftId() {return mannschaftId;}
    public void setMannschaftId(Long mannschaftId) {this.mannschaftId = mannschaftId;}

    public Long getBegegnung() {return begegnung;}
    public void setBegegnung(Long begegnung) {this.begegnung = begegnung;}

    public Long getNaechsteMatchId() {return naechsteMatchId;}
    public void setNaechsteMatchId(Long naechsteMatchId) {this.naechsteMatchId = naechsteMatchId;}

    public Long getNaechsteNaechsteMatchNrMatchId() {return naechsteNaechsteMatchNrMatchId;}
    public void setNaechsteNaechsteMatchNrMatchId(Long naechsteNaechsteMatchNrMatchId) {this.naechsteNaechsteMatchNrMatchId = naechsteNaechsteMatchNrMatchId;}

    public Long getStrafpunkteSatz1() {return strafpunkteSatz1;}
    public void setStrafpunkteSatz1(Long strafpunkteSatz1) {this.strafpunkteSatz1 = strafpunkteSatz1;}

    public Long getStrafpunkteSatz2() {return strafpunkteSatz2;}
    public void setStrafpunkteSatz2(Long strafpunkteSatz2) {this.strafpunkteSatz2 = strafpunkteSatz2;}

    public Long getStrafpunkteSatz3() {return strafpunkteSatz3;}
    public void setStrafpunkteSatz3(Long strafpunkteSatz3) {this.strafpunkteSatz3 = strafpunkteSatz3;}

    public Long getStrafpunkteSatz4() {return strafpunkteSatz4;}
    public void setStrafpunkteSatz4(Long strafpunkteSatz4) {this.strafpunkteSatz4 = strafpunkteSatz4;}

    public Long getStrafpunkteSatz5() {return strafpunkteSatz5;}
    public void setStrafpunkteSatz5(Long strafpunkteSatz5) {this.strafpunkteSatz5 = strafpunkteSatz5;}

    public String getWettkampftypId() {return wettkampftypId;}
    public void setWettkampftypId(String wettkampftypId) {this.wettkampftypId = wettkampftypId;}

    public Long getWettkampfTag() {return wettkampfTag;}
    public void setWettkampfTag(Long wettkampfTag) {this.wettkampfTag = wettkampfTag;}

    public String getMannschaftName() {return mannschaftName;}
    public void setMannschaftName(String mannschaftName) {this.mannschaftName = mannschaftName;}

    public Integer getRueckennummer() {return rueckennummer;}
    public void setRueckennummer(Integer rueckennummer) {this.rueckennummer = rueckennummer;}

}
