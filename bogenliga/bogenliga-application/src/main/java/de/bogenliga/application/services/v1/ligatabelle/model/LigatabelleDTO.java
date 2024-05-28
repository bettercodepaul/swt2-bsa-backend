package de.bogenliga.application.services.v1.ligatabelle.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

import java.util.Objects;

/**
 * I'm the data transfer object of the liga.
 * <p>
 * I define the payload for the external REST interface of the liga business entity.
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class LigatabelleDTO implements DataTransferObject {

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
    private int matchpkt;
    private int matchpktGegen;
    private int satzpkt;
    private int satzpktGegen;
    private int satzpktDifferenz;
    private int sortierung;
    private int tabellenplatz;
    private int matchCount;

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
     * @param matchpkt;
     * @param matchpktGegen;
     * @param satzpkt;
     * @param satzpktGegen;
     * @param satzpktDifferenz;
     * @param sortierung;
     * @param tabellenplatz;

     */
    public LigatabelleDTO(
            Long veranstaltungId,
            String veranstaltungName,
            Long wettkampfId,
            int wettkampfTag,
            Long mannschaftId,
            int mannschaftNummer,
            Long vereinId,
            String vereinName,
            int matchpkt,
            int matchpktGegen,
            int satzpkt,
            int satzpktGegen,
            int satzpktDifferenz,
            int sortierung,
            int tabellenplatz,
            int matchCount
    ) {
        this.veranstaltungId=veranstaltungId;
        this.veranstaltungName = veranstaltungName;
        this.wettkampfId = wettkampfId;
        this.wettkampfTag = wettkampfTag;
        this.mannschaftId = mannschaftId;
        this.mannschaftNummer = mannschaftNummer;
        this.vereinId = vereinId;
        this.vereinName = vereinName;
        this.matchpkt = matchpkt;
        this.matchpktGegen = matchpktGegen;
        this.satzpkt = satzpkt;
        this.satzpktGegen = satzpktGegen;
        this.satzpktDifferenz = satzpktDifferenz;
        this.sortierung = sortierung;
        this.tabellenplatz = tabellenplatz;
        this.matchCount = matchCount;
    }


    public LigatabelleDTO() {
        // empty constructor
    }

    public Long getVeranstaltungId() {
        return veranstaltungId;
    }

    public void setVeranstaltungId(Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public String getVeranstaltungName() {
        return veranstaltungName;
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

    public Long getVereinId() {
        return vereinId;
    }

    public void setVereinId(Long vereinId) {
        this.vereinId = vereinId;
    }

    public String getVereinName() {
        return vereinName;
    }

    public int getMatchpkt() {
        return matchpkt;
    }

    public int getMatchpktGegen() {
        return matchpktGegen;
    }

    public int getSatzpkt() {
        return satzpkt;
    }

    public int getSatzpktGegen() {
        return satzpktGegen;
    }

    public int getSatzpktDifferenz() {
        return satzpktDifferenz;
    }

    public int getSortierung() {
        return sortierung;
    }

    public void setSortierung(int sortierung) {
        this.sortierung = sortierung;
    }

    public int getTabellenplatz() {
        return tabellenplatz;
    }

    public int getMatchCount() { return matchCount; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LigatabelleDTO that = (LigatabelleDTO) o;
        return wettkampfTag == that.wettkampfTag &&
                mannschaftNummer == that.mannschaftNummer &&
                matchpkt == that.matchpkt &&
                matchpktGegen == that.matchpktGegen &&
                satzpkt == that.satzpkt &&
                satzpktGegen == that.satzpktGegen &&
                satzpktDifferenz == that.satzpktDifferenz &&
                sortierung == that.sortierung &&
                tabellenplatz == that.tabellenplatz &&
                matchCount == that.matchCount &&
                veranstaltungId.equals(that.veranstaltungId) &&
                Objects.equals(veranstaltungName, that.veranstaltungName) &&
                wettkampfId.equals(that.wettkampfId) &&
                mannschaftId.equals(that.mannschaftId) &&
                vereinId.equals(that.vereinId) &&
                Objects.equals(vereinName, that.vereinName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag,
                mannschaftId, mannschaftNummer, vereinId, vereinName, matchpkt, matchpktGegen,
                satzpkt, satzpktGegen, satzpktDifferenz, sortierung, tabellenplatz, matchCount);
    }
}
