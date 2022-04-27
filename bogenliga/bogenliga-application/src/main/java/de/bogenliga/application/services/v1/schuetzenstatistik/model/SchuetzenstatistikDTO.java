package de.bogenliga.application.services.v1.schuetzenstatistik.model;

import de.bogenliga.application.common.service.types.DataTransferObject;
import java.util.Objects;

/**
 * I'm the data transfer object of the schuetzenstatistik.
 */
public class SchuetzenstatistikDTO implements DataTransferObject {

    /**
     * attributes of DTO
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

    /**
     * The Constructor with optional parameters
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
    public SchuetzenstatistikDTO(
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
        this.matchNr = matchNr;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedName = dsbMitgliedName;
        this.rueckenNummer = rueckenNummer;
        this.pfeilpunkteSchnitt = pfeilpunkteSchnitt;
    }

    public SchuetzenstatistikDTO() {
    }

    // Getters and Setters
    public Long getVeranstaltungId() {
        return veranstaltungId;
    }
    public void setVeranstaltungId(Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public String getVeranstaltungName() {
        return veranstaltungName;
    }
    public void setVeranstaltungName(String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
    }

    public Long getWettkampfId() {
        return wettkampfId;
    }
    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public int getWettkampfTag() {
        return wettkampfTag;
    }
    public void setWettkampfTag(int wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }

    public Long getMannschaftId() {
        return mannschaftId;
    }
    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }

    public int getMannschaftNummer() {
        return mannschaftNummer;
    }
    public void setMannschaftNummer(int mannschaftNummer) {
        this.mannschaftNummer = mannschaftNummer;
    }

    public Long getVereinId() {
        return vereinId;
    }
    public void setVereinId(Long vereinId) {
        this.vereinId = vereinId;
    }

    public String getVereinName() {
        return vereinName;
    }
    public void setVereinName(String vereinName) {
        this.vereinName = vereinName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchuetzenstatistikDTO that = (SchuetzenstatistikDTO) o;
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
                pfeilpunkteSchnitt == that.pfeilpunkteSchnitt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag,
                mannschaftId, mannschaftNummer, vereinId, vereinName, matchId, matchNr,
                dsbMitgliedId, dsbMitgliedName, rueckenNummer, pfeilpunkteSchnitt);
    }
}
