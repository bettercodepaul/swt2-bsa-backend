package de.bogenliga.application.business.ligapasse.api.types;

/**
 * TODO [AL] class documentation
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
public class LigapasseDO {

    private Long wettkampfId;
    private Long matchId;
    private Long passeId;
    private int passeLfdnr;
    private Long passeManschaftId;
    private Long dsbMitgliedId;
    private String dsbMitgliedName;
    private int mannschaftsmitgliedRueckennummer;
    private int passeRingzahlPfeil1;
    private int passeRingzahlPfeil2;


    public LigapasseDO(Long wettkampfId, Long matchId, Long passeId, int passeLfdnr, Long passeManschaftId,
                       Long dsbMitgliedId, String dsbMitgliedName, int mannschaftsmitgliedRueckennummer,
                       int passeRingzahlPfeil1, int passeRingzahlPfeil2) {
        this.wettkampfId = wettkampfId;
        this.matchId = matchId;
        this.passeId = passeId;
        this.passeLfdnr = passeLfdnr;
        this.passeManschaftId = passeManschaftId;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedName = dsbMitgliedName;
        this.mannschaftsmitgliedRueckennummer = mannschaftsmitgliedRueckennummer;
        this.passeRingzahlPfeil1 = passeRingzahlPfeil1;
        this.passeRingzahlPfeil2 = passeRingzahlPfeil2;
    }


    public Long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }


    public Long getMatchId() {
        return matchId;
    }


    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }


    public Long getPasseId() {
        return passeId;
    }


    public void setPasseId(Long passeId) {
        this.passeId = passeId;
    }


    public int getPasseLfdnr() {
        return passeLfdnr;
    }


    public void setPasseLfdnr(int passeLfdnr) {
        this.passeLfdnr = passeLfdnr;
    }


    public Long getPasseManschaftId() {
        return passeManschaftId;
    }


    public void setPasseManschaftId(Long passeManschaftId) {
        this.passeManschaftId = passeManschaftId;
    }


    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }


    public void setDsbMitgliedId(Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }


    public String getDsbMitgliedName() {
        return dsbMitgliedName;
    }


    public void setDsbMitgliedName(String dsbMitgliedName) {
        this.dsbMitgliedName = dsbMitgliedName;
    }


    public int getMannschaftsmitgliedRueckennummer() {
        return mannschaftsmitgliedRueckennummer;
    }


    public void setMannschaftsmitgliedRueckennummer(int mannschaftsmitgliedRueckennummer) {
        this.mannschaftsmitgliedRueckennummer = mannschaftsmitgliedRueckennummer;
    }


    public int getPasseRingzahlPfeil1() {
        return passeRingzahlPfeil1;
    }


    public void setPasseRingzahlPfeil1(int passeRingzahlPfeil1) {
        this.passeRingzahlPfeil1 = passeRingzahlPfeil1;
    }


    public int getPasseRingzahlPfeil2() {
        return passeRingzahlPfeil2;
    }


    public void setPasseRingzahlPfeil2(int passeRingzahlPfeil2) {
        this.passeRingzahlPfeil2 = passeRingzahlPfeil2;
    }
}
