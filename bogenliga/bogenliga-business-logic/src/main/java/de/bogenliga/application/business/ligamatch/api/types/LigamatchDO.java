package de.bogenliga.application.business.ligamatch.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * TODO [AL] class documentation
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
public class LigamatchDO extends CommonDataObject implements DataObject {
    private Long wettkampfId;
    private Long matchId;
    private int matchNr;
    private int scheibennummer;
    private Long mannschaftId;
    private String mannschaftName;
    private String mannschaftNameGegner;
    private int scheibennummerGegner;
    private Long idGegner;
    private Long naechsteMatchId;
    private Long naechsteNaechsteMatchNrMatchId;
    private int strafpunkteSatz1;
    private int strafpunkteSatz2;
    private int strafpunkteSatz3;
    private int strafpunkteSatz4;
    private int strafpunkteSatz5;


    public LigamatchDO(Long wettkampfId, Long matchId, int matchNr, int scheibennummer, Long mannschaftId,
                       String mannschaftName, String mannschaftNameGegner, int scheibennummerGegner,
                       Long idGegner, Long naechsteMatchId, Long naechsteNaechsteMatchNrMatchId, int strafpunkteSatz1,
                       int strafpunkteSatz2, int strafpunkteSatz3, int strafpunkteSatz4, int strafpunkteSatz5) {
        this.wettkampfId = wettkampfId;
        this.matchId = matchId;
        this.matchNr = matchNr;
        this.scheibennummer = scheibennummer;
        this.mannschaftId = mannschaftId;
        this.mannschaftName = mannschaftName;
        this.mannschaftNameGegner = mannschaftNameGegner;
        this.scheibennummerGegner = scheibennummerGegner;
        this.idGegner = idGegner;
        this.naechsteMatchId = naechsteMatchId;
        this.naechsteNaechsteMatchNrMatchId = naechsteNaechsteMatchNrMatchId;
        this.strafpunkteSatz1 = strafpunkteSatz1;
        this.strafpunkteSatz2 = strafpunkteSatz2;
        this.strafpunkteSatz3 = strafpunkteSatz3;
        this.strafpunkteSatz4 = strafpunkteSatz4;
        this.strafpunkteSatz5 = strafpunkteSatz5;
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


    public int getMatchNr() {
        return matchNr;
    }


    public void setMatchNr(int matchNr) {
        this.matchNr = matchNr;
    }


    public int getScheibennummer() {
        return scheibennummer;
    }


    public void setScheibennummer(int scheibennummer) {
        this.scheibennummer = scheibennummer;
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


    public String getMannschaftNameGegner() {
        return mannschaftNameGegner;
    }


    public void setMannschaftNameGegner(String mannschaftNameGegner) {
        this.mannschaftNameGegner = mannschaftNameGegner;
    }


    public int getScheibennummerGegner() {
        return scheibennummerGegner;
    }


    public void setScheibennummerGegner(int scheibennummerGegner) {
        this.scheibennummerGegner = scheibennummerGegner;
    }


    public Long getIdGegner() {
        return idGegner;
    }


    public void setIdGegner(Long idGegner) {
        this.idGegner = idGegner;
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


    public int getStrafpunkteSatz1() {
        return strafpunkteSatz1;
    }


    public void setStrafpunkteSatz1(int strafpunkteSatz1) {
        this.strafpunkteSatz1 = strafpunkteSatz1;
    }


    public int getStrafpunkteSatz2() {
        return strafpunkteSatz2;
    }


    public void setStrafpunkteSatz2(int strafpunkteSatz2) {
        this.strafpunkteSatz2 = strafpunkteSatz2;
    }


    public int getStrafpunkteSatz3() {
        return strafpunkteSatz3;
    }


    public void setStrafpunkteSatz3(int strafpunkteSatz3) {
        this.strafpunkteSatz3 = strafpunkteSatz3;
    }


    public int getStrafpunkteSatz4() {
        return strafpunkteSatz4;
    }


    public void setStrafpunkteSatz4(int strafpunkteSatz4) {
        this.strafpunkteSatz4 = strafpunkteSatz4;
    }


    public int getStrafpunkteSatz5() {
        return strafpunkteSatz5;
    }


    public void setStrafpunkteSatz5(int strafpunkteSatz5) {
        this.strafpunkteSatz5 = strafpunkteSatz5;
    }
}
