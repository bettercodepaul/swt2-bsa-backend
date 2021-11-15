package de.bogenliga.application.business.schusszettel.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 *
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
public class MatchPasseViewDO extends CommonDataObject implements DataObject {
    private Long wettkampfId;
    private Long matchId;
    private Long passeId;
    private Integer passeLfdnr;
    private Long mannschaftId;
    private Long dsbMitgliedId;
    private String dsbMitgliedName;
    private Integer mannschaftsmitgliedRueckennummer;
    private Integer passeRingzahlPfeil1;
    private Integer passeRingzahlPfeil2;
    private Integer matchNr;
    private Integer scheibennummer;

    private String mannschaftName;
    private String nameGegner;
    private Integer scheibennummerGegner;
    private Long idGegner;
    private Long naechsteMatchId;
    private Long naechsteNaechsteMatchNrMatchId;
    private Integer strafpunkteSatz1;
    private Integer strafpunkteSatz2;
    private Integer strafpunkteSatz3;
    private Integer strafpunkteSatz4;
    private Integer strafpunkteSatz5;


    public MatchPasseViewDO(Long wettkampfId, Long matchId, Long passeId, Integer passeLfdnr, Long mannschaftId,
                            Long dsbMitgliedId, String dsbMitgliedName, Integer mannschaftsmitgliedRueckennummer,
                            Integer passeRingzahlPfeil1, Integer passeRingzahlPfeil2, Integer matchNr,
                            Integer scheibennummer, String mannschaftName, String nameGegner,
                            Integer scheibennummerGegner, Long idGegner, Long naechsteMatchId,
                            Long naechsteNaechsteMatchNrMatchId, Integer strafpunkteSatz1,
                            Integer strafpunkteSatz2, Integer strafpunkteSatz3, Integer strafpunkteSatz4,
                            Integer strafpunkteSatz5) {
        this.wettkampfId = wettkampfId;
        this.matchId = matchId;
        this.passeId = passeId;
        this.passeLfdnr = passeLfdnr;
        this.mannschaftId = mannschaftId;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedName = dsbMitgliedName;
        this.mannschaftsmitgliedRueckennummer = mannschaftsmitgliedRueckennummer;
        this.passeRingzahlPfeil1 = passeRingzahlPfeil1;
        this.passeRingzahlPfeil2 = passeRingzahlPfeil2;
        this.matchNr = matchNr;
        this.scheibennummer = scheibennummer;
        this.mannschaftName = mannschaftName;
        this.nameGegner = nameGegner;
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


    public Long getPasseId() {
        return passeId;
    }


    public void setPasseId(Long passeId) {
        this.passeId = passeId;
    }


    public Integer getPasseLfdnr() {
        return passeLfdnr;
    }


    public void setPasseLfdnr(Integer passeLfdnr) {
        this.passeLfdnr = passeLfdnr;
    }


    public Long getMannschaftId() {
        return mannschaftId;
    }


    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
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


    public Integer getMannschaftsmitgliedRueckennummer() {
        return mannschaftsmitgliedRueckennummer;
    }


    public void setMannschaftsmitgliedRueckennummer(Integer mannschaftsmitgliedRueckennummer) {
        this.mannschaftsmitgliedRueckennummer = mannschaftsmitgliedRueckennummer;
    }


    public Integer getPasseRingzahlPfeil1() {
        return passeRingzahlPfeil1;
    }


    public void setPasseRingzahlPfeil1(Integer passeRingzahlPfeil1) {
        this.passeRingzahlPfeil1 = passeRingzahlPfeil1;
    }


    public Integer getPasseRingzahlPfeil2() {
        return passeRingzahlPfeil2;
    }


    public void setPasseRingzahlPfeil2(Integer passeRingzahlPfeil2) {
        this.passeRingzahlPfeil2 = passeRingzahlPfeil2;
    }


    public Integer getMatchNr() {
        return matchNr;
    }


    public void setMatchNr(Integer matchNr) {
        this.matchNr = matchNr;
    }


    public Integer getScheibennummer() {
        return scheibennummer;
    }


    public void setScheibennummer(Integer scheibennummer) {
        this.scheibennummer = scheibennummer;
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


    public Integer getScheibennummerGegner() {
        return scheibennummerGegner;
    }


    public void setScheibennummerGegner(Integer scheibennummerGegner) {
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


    public Integer getStrafpunkteSatz4() {
        return strafpunkteSatz4;
    }


    public void setStrafpunkteSatz4(Integer strafpunkteSatz4) {
        this.strafpunkteSatz4 = strafpunkteSatz4;
    }


    public Integer getStrafpunkteSatz5() {
        return strafpunkteSatz5;
    }


    public void setStrafpunkteSatz5(Integer strafpunkteSatz5) {
        this.strafpunkteSatz5 = strafpunkteSatz5;
    }
}
