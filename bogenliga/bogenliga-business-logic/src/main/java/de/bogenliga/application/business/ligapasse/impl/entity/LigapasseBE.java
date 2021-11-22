package de.bogenliga.application.business.ligapasse.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class LigapasseBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -4448642082943788879L;

    private Long wettkampfId;
    private Long matchId;
    private Long passeId;
    private int passeLfdnr;
    private Long passeMannschaftId;
    private Long dsbMitgliedId;
    private String dsbMitgliedName;
    private int mannschaftsmitgliedRueckennummer;
    private int passeRingzahlPfeil1;
    private int passeRingzahlPfeil2;


    public LigapasseBE() {
    }


    @Override
    public String toString() {
        return "LigapasseBE{" +
                "wettkampfId=" + wettkampfId +
                ", matchId=" + matchId +
                ", passeId=" + passeId +
                ", passeLfdnr=" + passeLfdnr +
                ", passeMannschaftId=" + passeMannschaftId +
                ", dsbMitgliedId=" + dsbMitgliedId +
                ", dsbMitgliedName='" + dsbMitgliedName + '\'' +
                ", mannschaftsmitgliedRueckennummer=" + mannschaftsmitgliedRueckennummer +
                ", passeRingzahlPfeil1=" + passeRingzahlPfeil1 +
                ", passeRingzahlPfeil2=" + passeRingzahlPfeil2 +
                '}';
    }


    public Long getWettkampfId() {return wettkampfId;}
    public void setWettkampfId(Long wettkampfId) {this.wettkampfId = wettkampfId;}

    public Long getMatchId() {return matchId;}
    public void setMatch_id(Long matchId) {this.matchId = matchId;}

    public Long getPasseId() {return passeId;}
    public void setPasseId(Long passeId) {this.passeId = passeId;}

    public int getPasseLfdnr() {return passeLfdnr;}
    public void setPasseLfdnr(int passeLfdnr) {this.passeLfdnr = passeLfdnr;}

    public Long getPasseMannschaftId() {return passeMannschaftId;}
    public void setPasseMannschaftId(Long passeMannschaftId) {this.passeMannschaftId = passeMannschaftId;}

    public Long getDsbMitgliedId() {return dsbMitgliedId;}
    public void setDsbMitgliedId(Long dsbMitgliedId) {this.dsbMitgliedId = dsbMitgliedId;}

    public String getDsbMitgliedName() {return dsbMitgliedName;}
    public void setDsbMitgliedName(String dsbMitgliedName) {this.dsbMitgliedName = dsbMitgliedName;}

    public int getMannschaftsmitgliedRueckennummer() {return mannschaftsmitgliedRueckennummer;}
    public void setMannschaftsmitgliedRueckennummer(int mannschaftsmitgliedRueckennummer) {this.mannschaftsmitgliedRueckennummer = mannschaftsmitgliedRueckennummer;}

    public int getPasseRingzahlPfeil1() {return passeRingzahlPfeil1;}
    public void setPasseRingzahlPfeil1(int passeRingzahlPfeil1) {this.passeRingzahlPfeil1 = passeRingzahlPfeil1;}

    public int getPasseRingzahlPfeil2() {return passeRingzahlPfeil2;}
    public void setPasseRingzahlPfeil2(int passeRingzahlPfeil2) {this.passeRingzahlPfeil2 = passeRingzahlPfeil2;}
}
