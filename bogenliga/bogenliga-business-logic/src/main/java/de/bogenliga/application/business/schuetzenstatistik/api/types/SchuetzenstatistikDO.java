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
    private String schuetzeSatz1;
    private String schuetzeSatz2;
    private String schuetzeSatz3;
    private String schuetzeSatz4;
    private String schuetzeSatz5;


    public SchuetzenstatistikDO() {
        // no parameter-constructor
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
     * @param schuetzeSatz1;
     * @param schuetzeSatz2;
     * @param schuetzeSatz3;
     * @param schuetzeSatz4;
     * @param schuetzeSatz5;
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
            String schuetzeSatz1,
            String schuetzeSatz2,
            String schuetzeSatz3,
            String schuetzeSatz4,
            String schuetzeSatz5
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
        this.schuetzeSatz1 = schuetzeSatz1;
        this.schuetzeSatz2 = schuetzeSatz2;
        this.schuetzeSatz3 = schuetzeSatz3;
        this.schuetzeSatz4 = schuetzeSatz4;
        this.schuetzeSatz5 = schuetzeSatz5;
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

    public String getschuetzeSatz1() {
        return schuetzeSatz1;
    }
    public void setschuetzeSatz1(String schuetzeSatz1) {
        this.schuetzeSatz1 = schuetzeSatz1;
    }

    public String getschuetzeSatz2() {
        return schuetzeSatz2;
    }
    public void setschuetzeSatz2(String schuetzeSatz2) {
        this.schuetzeSatz2 = schuetzeSatz2;
    }

    public String getschuetzeSatz3() {
        return schuetzeSatz3;
    }
    public void setschuetzeSatz3(String schuetzeSatz3) {
        this.schuetzeSatz3 = schuetzeSatz3;
    }

    public String getschuetzeSatz4() {
        return schuetzeSatz4;
    }
    public void setschuetzeSatz4(String schuetzeSatz4) {
        this.schuetzeSatz4 = schuetzeSatz4;
    }

    public String getschuetzeSatz5() {
        return schuetzeSatz5;
    }
    public void setschuetzeSatz5(String schuetzeSatz5) {
        this.schuetzeSatz5 = schuetzeSatz5;
    }

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
                Objects.equals(schuetzeSatz1, that.schuetzeSatz1) &&
                Objects.equals(schuetzeSatz2, that.schuetzeSatz2) &&
                Objects.equals(schuetzeSatz3, that.schuetzeSatz3) &&
                Objects.equals(schuetzeSatz4, that.schuetzeSatz4) &&
                Objects.equals(schuetzeSatz5, that.schuetzeSatz5);
    }

    @Override
    public int hashCode() {
        return Objects.hash(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag,
                mannschaftId, mannschaftNummer, vereinId, vereinName, matchId, matchNr,
                dsbMitgliedId, dsbMitgliedName, rueckenNummer, pfeilpunkteSchnitt,
                schuetzeSatz1, schuetzeSatz2, schuetzeSatz3, schuetzeSatz4, schuetzeSatz5);
    }

}