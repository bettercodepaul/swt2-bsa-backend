package de.bogenliga.application.business.schuetzenstatistik.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.util.Objects;

/**
 * Contains the values of the schuetzenstatistik business entity.
 */
public class SchuetzenstatistikDO extends CommonDataObject implements DataObject {

    /**
     * DO attributes
     */

    private Long veranstaltungId;
    private String veranstaltungName;
    private Long wettkampfId;
    private int wettkampfTag;
    private Long mannschaftId;
    private int mannschaftNummer;
    private Long vereinId;
    private String vereinName;
    private Long matchId;
    private int matchNr;
    private Long dsbMitgliedId;
    private String dsbMitgliedName;
    private int rueckenNummer;
    private float pfeilpunkteSchnitt;
    private String[] schuetzeSaetze;

    public SchuetzenstatistikDO() {
        // no parameter-constructor
        schuetzeSaetze = new String[5];
    }

    /**
     * A Constructor with optional parameters
     *
     * @param veranstaltungId;
     * @param veranstaltungName;
     * @param wettkampfId;
     * @param wettkampfTag;
     * @param mannschaftId;
     * @param mannschaftNummer;
     * @param vereinId;
     * @param vereinName;
     * @param matchId;
     * @param matchNr;
     * @param dsbMitgliedId;
     * @param dsbMitgliedName;
     * @param rueckenNummer;
     * @param pfeilpunkteSchnitt;
     */
    public SchuetzenstatistikDO(
            Long veranstaltungId,
            String veranstaltungName,
            Long wettkampfId,
            int wettkampfTag,
            Long mannschaftId,
            int mannschaftNummer,
            Long vereinId,
            String vereinName,
            Long matchId,
            int matchNr,
            Long dsbMitgliedId,
            String dsbMitgliedName,
            int rueckenNummer,
            float pfeilpunkteSchnitt,
            String[] schuetzeSaetze
    ) {
        this.veranstaltungId=veranstaltungId;
        this.veranstaltungName = veranstaltungName;
        this.wettkampfId = wettkampfId;
        this.wettkampfTag = wettkampfTag;
        this.mannschaftId = mannschaftId;
        this.mannschaftNummer = mannschaftNummer;
        this.vereinId = vereinId;
        this.vereinName = vereinName;
        this.matchId = matchId;
        this.matchNr = matchNr;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedName = dsbMitgliedName;
        this.rueckenNummer = rueckenNummer;
        this.pfeilpunkteSchnitt = pfeilpunkteSchnitt;
        this.schuetzeSaetze = schuetzeSaetze;
    }


    public Long getveranstaltungId() { return veranstaltungId; }
    public void setveranstaltungId(Long veranstaltungId) { this.veranstaltungId = veranstaltungId; }

    public String getveranstaltungName() { return veranstaltungName; }
    public void setveranstaltungName(String veranstaltungName) { this.veranstaltungName = veranstaltungName; }

    public Long getwettkampfId() { return wettkampfId; }
    public void setwettkampfId(Long wettkampfId) { this.wettkampfId = wettkampfId; }

    public int getwettkampfTag() { return wettkampfTag; }
    public void setwettkampfTag(int wettkampfTag) { this.wettkampfTag = wettkampfTag; }

    public Long getmannschaftId() { return mannschaftId; }
    public void setmannschaftId(Long mannschaftId) { this.mannschaftId = mannschaftId; }

    public int getmannschaftNummer() { return mannschaftNummer; }
    public void setmannschaftNummer(int mannschaftNummer) { this.mannschaftNummer = mannschaftNummer; }

    public Long getvereinId() { return vereinId; }
    public void setvereinId(Long vereinId) { this.vereinId = vereinId; }

    public String getvereinName() { return vereinName; }
    public void setvereinName(String vereinName) { this.vereinName = vereinName; }

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

    public int getRueckenNummer() {
        return rueckenNummer;
    }
    public void setRueckenNummer(int rueckenNummer) {
        this.rueckenNummer = rueckenNummer;
    }

    public float getPfeilpunkteSchnitt() {
        return pfeilpunkteSchnitt;
    }
    public void setPfeilpunkteSchnitt(float pfeilpunkteSchnitt) {
        this.pfeilpunkteSchnitt = pfeilpunkteSchnitt;
    }

    public String getschuetzeSatz1() { return schuetzeSaetze[0]; }

    public String getschuetzeSatz2() {
        return schuetzeSaetze[1];
    }

    public String getschuetzeSatz3() {
        return schuetzeSaetze[2];
    }

    public String getschuetzeSatz4() {
        return schuetzeSaetze[3];
    }

    public String getschuetzeSatz5() {
        return schuetzeSaetze[4];
    }
    public String[] getSchuetzeSaetze() { return schuetzeSaetze; }
    public void setSchuetzeSaetze(String[] schuetzeSaetze) { this.schuetzeSaetze = schuetzeSaetze;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchuetzenstatistikDO that = (SchuetzenstatistikDO) o;
        return wettkampfTag == that.wettkampfTag &&
                mannschaftNummer == that.mannschaftNummer &&
                veranstaltungId.equals(that.veranstaltungId) &&
                Objects.equals(veranstaltungName, that.veranstaltungName) &&
                wettkampfId.equals(that.wettkampfId) &&
                mannschaftId.equals(that.mannschaftId) &&
                vereinId.equals(that.vereinId) &&
                Objects.equals(vereinName, that.vereinName) &&
                matchId.equals(that.matchId) &&
                matchNr == that.matchNr &&
                dsbMitgliedId.equals(that.dsbMitgliedId) &&
                Objects.equals(dsbMitgliedName, that.dsbMitgliedName) &&
                rueckenNummer == that.rueckenNummer &&
                pfeilpunkteSchnitt == that.pfeilpunkteSchnitt &&
                Objects.equals(schuetzeSaetze, that.schuetzeSaetze);
    }

    @Override
    public int hashCode() {
        return Objects.hash(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag,
                mannschaftId, mannschaftNummer, vereinId, vereinName, matchId, matchNr,
                dsbMitgliedId, dsbMitgliedName, rueckenNummer, pfeilpunkteSchnitt,
                schuetzeSaetze);
    }

}