package de.bogenliga.application.business.schuetzenstatistik.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.util.Objects;

/**
 * Contains the values of the schuetzenstatistik business entity.
 */
public class SchuetzenstatistikDO extends CommonDataObject implements DataObject {

    /**
     * business parameter
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
    private Long dsbMitgliedId;
    private String dsbMitgliedName;
    private float pfeilpunkteSchnitt;

    /**
     * Constructor with optional parameters
     *
     * @param veranstaltungId;
     * @param veranstaltungName;
     * @param wettkampfId;
     * @param wettkampfTag;
     * @param mannschaftId;
     * @param mannschaftNummer;
     * @param vereinId;
     * @param vereinName;
     * @param dsbMitgliedId;
     * @param dsbMitgliedName;
     * @param matchId;
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
            Long dsbMitgliedId,
            String dsbMitgliedName,
            float pfeilpunkteSchnitt
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
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedName = dsbMitgliedName;
        this.pfeilpunkteSchnitt = pfeilpunkteSchnitt;
    }


    public SchuetzenstatistikDO() {
        // empty constructor
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

    public float getPfeilpunkteSchnitt() {
        return pfeilpunkteSchnitt;
    }
    public void setPfeilpunkteSchnitt(float pfeilpunkteSchnitt) {
        this.pfeilpunkteSchnitt = pfeilpunkteSchnitt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag, mannschaftId,
                mannschaftNummer, vereinId, vereinName, matchId, dsbMitgliedId, dsbMitgliedName, pfeilpunkteSchnitt);
    }

    @Override
    public boolean equals(final Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SchuetzenstatistikDO that = (SchuetzenstatistikDO) o;
        return veranstaltungId.equals(that.veranstaltungId) &&
                Objects.equals(veranstaltungName, that.veranstaltungName)&&
                wettkampfId.equals(that.wettkampfId) &&
                wettkampfTag == that.wettkampfTag &&
                mannschaftId.equals(that.mannschaftId) &&
                mannschaftNummer == that.mannschaftNummer &&
                vereinId.equals(that.vereinId) &&
                Objects.equals(vereinName, that.vereinName) &&
                matchId.equals(that.matchId) &&
                dsbMitgliedId.equals(that.dsbMitgliedId) &&
                Objects.equals(dsbMitgliedName, that.dsbMitgliedName) &&
                pfeilpunkteSchnitt == that.pfeilpunkteSchnitt;
    }

}