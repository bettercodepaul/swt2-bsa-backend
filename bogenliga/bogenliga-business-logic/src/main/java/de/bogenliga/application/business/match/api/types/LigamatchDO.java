package de.bogenliga.application.business.match.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;

/**
 * This class was introduced because the the DO object for the LigaMatchBE-class was missing
 * @author Adrian Kempf, HSRT MKI SS22 - SWT2
 */
public class LigamatchDO extends CommonDataObject {
    private static final long serialVersionUID = -3666564080201059328L;

    private Long id;
    private Long wettkampfId;
    private Long matchNr;
    private Long matchScheibennummer;
    private Long matchpunkte;
    private Long satzpunkte;
    private Long mannschaftId;
    private String mannschaftName;
    private String nameGegner;
    private Long scheibennummerGegner;
    private Long matchIdGegner;
    private Long naechsteMatchId;
    
    private Long naechsteNaechsteMatchNrMatchId;
    private Long strafpunkteSatz1;
    private Long strafpunkteSatz2;
    private Long strafpunkteSatz3;
    private Long strafpunkteSatz4;
    private Long strafpunkteSatz5;

    public LigamatchDO(Long id, Long wettkampfId, Long matchNr, Long matchScheibennummer, Long matchpunkte,
                       Long satzpunkte, Long mannschaftId, String mannschaftName, String nameGegner,
                       Long scheibennummerGegner, Long matchIdGegner, Long naechsteMatchId,
                       Long naechsteNaechsteMatchNrMatchId, Long strafpunkteSatz1, Long strafpunkteSatz2,
                       Long strafpunkteSatz3, Long strafpunkteSatz4, Long strafpunkteSatz5) {
        this.id = id;
        this.wettkampfId = wettkampfId;
        this.matchNr = matchNr;
        this.matchScheibennummer = matchScheibennummer;
        this.matchpunkte = matchpunkte;
        this.satzpunkte = satzpunkte;
        this.mannschaftId = mannschaftId;
        this.mannschaftName = mannschaftName;
        this.nameGegner = nameGegner;
        this.scheibennummerGegner = scheibennummerGegner;
        this.matchIdGegner = matchIdGegner;
        this.naechsteMatchId = naechsteMatchId;
        this.naechsteNaechsteMatchNrMatchId = naechsteNaechsteMatchNrMatchId;
        this.strafpunkteSatz1 = strafpunkteSatz1;
        this.strafpunkteSatz2 = strafpunkteSatz2;
        this.strafpunkteSatz3 = strafpunkteSatz3;
        this.strafpunkteSatz4 = strafpunkteSatz4;
        this.strafpunkteSatz5 = strafpunkteSatz5;
    }
    
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }


    public Long getMatchNr() {
        return matchNr;
    }


    public void setMatchNr(Long matchNr) {
        this.matchNr = matchNr;
    }


    public Long getMatchScheibennummer() {
        return this.matchScheibennummer;
    }


    public void setMatchScheibennummer(Long matchScheibennummer) {
        this.matchScheibennummer = matchScheibennummer;
    }


    public Long getMatchpunkte() {
        return matchpunkte;
    }


    public void setMatchpunkte(Long matchpunkte) {
        this.matchpunkte = matchpunkte;
    }


    public Long getSatzpunkte() {
        return satzpunkte;
    }


    public void setSatzpunkte(Long satzpunkte) {
        this.satzpunkte = satzpunkte;
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


    public String getNameGegner() {
        return nameGegner;
    }


    public void setNameGegner(String nameGegner) {
        this.nameGegner = nameGegner;
    }


    public Long getScheibennummerGegner() {
        return scheibennummerGegner;
    }


    public void setScheibennummerGegner(Long scheibennummerGegner) {
        this.scheibennummerGegner = scheibennummerGegner;
    }


    public Long getMatchIdGegner() {
        return matchIdGegner;
    }


    public void setMatchIdGegner(Long matchIdGegner) {
        this.matchIdGegner = matchIdGegner;
    }


    public Long getNaechsteMatchId() {
        return naechsteMatchId;
    }


    public void setNaechsteMatchId(Long naechsteMatchId) {
        this.naechsteMatchId = naechsteMatchId;
    }


    public Long getNaechsteNaechsteMatchNrMatchId() {
        return naechsteNaechsteMatchNrMatchId;
    }


    public void setNaechsteNaechsteMatchNrMatchId(Long naechsteNaechsteMatchNrMatchId) {
        this.naechsteNaechsteMatchNrMatchId = naechsteNaechsteMatchNrMatchId;
    }


    public Long getStrafpunkteSatz1() {
        return strafpunkteSatz1;
    }


    public void setStrafpunkteSatz1(Long strafpunkteSatz1) {
        this.strafpunkteSatz1 = strafpunkteSatz1;
    }


    public Long getStrafpunkteSatz2() {
        return strafpunkteSatz2;
    }


    public void setStrafpunkteSatz2(Long strafpunkteSatz2) {
        this.strafpunkteSatz2 = strafpunkteSatz2;
    }


    public Long getStrafpunkteSatz3() {
        return strafpunkteSatz3;
    }


    public void setStrafpunkteSatz3(Long strafpunkteSatz3) {
        this.strafpunkteSatz3 = strafpunkteSatz3;
    }


    public Long getStrafpunkteSatz4() {
        return strafpunkteSatz4;
    }


    public void setStrafpunkteSatz4(Long strafpunkteSatz4) {
        this.strafpunkteSatz4 = strafpunkteSatz4;
    }


    public Long getStrafpunkteSatz5() {
        return strafpunkteSatz5;
    }


    public void setStrafpunkteSatz5(Long strafpunkteSatz5) {
        this.strafpunkteSatz5 = strafpunkteSatz5;
    }
}
