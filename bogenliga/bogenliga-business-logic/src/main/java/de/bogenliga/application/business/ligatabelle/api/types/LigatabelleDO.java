package de.bogenliga.application.business.ligatabelle.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;


import java.util.Objects;

/**
 * Contains the values of the liga business entity.
 *
 * @author Bruno Michael Cunha Teixeira, Bruno_Michael.Cunha_teixeira@Student.Reutlingen-University.de
 */
public class LigatabelleDO extends CommonDataObject implements DataObject {

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



    public LigatabelleDO() {
        // empty constructor
    }



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
    public LigatabelleDO(
            final Long veranstaltungId,
            final String veranstaltungName,
            final Long wettkampfId,
            final int wettkampfTag,
            final Long mannschaftId,
            final int mannschaftNummer,
            final Long vereinId,
            final String vereinName,
            final int matchpkt,
            final int matchpktGegen,
            final int satzpkt,
            final int satzpktGegen,
            final int satzpktDifferenz,
            final int sortierung,
            final int tabellenplatz,
            final int matchCount
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

    public int getmatchpkt() { return matchpkt; }
    public void setmatchpkt(int matchpkt) { this.matchpkt = matchpkt; }

    public int getMatchpktGegen() { return matchpktGegen; }
    public void setMatchpktGegen(int matchpktGegen) { this.matchpktGegen = matchpktGegen; }

    public int getsatzpkt() { return satzpkt; }
    public void setsatzpkt(int satzpkt) { this.satzpkt = satzpkt; }

    public int getSatzpktGegen() { return satzpktGegen; }
    public void setSatzpktGegen(int satzpktGegen) { this.satzpktGegen = satzpktGegen; }

    public int getSatzpktDifferenz() { return satzpktDifferenz; }
    public void setSatzpktDifferenz(int satzpktDifferenz) { this.satzpktDifferenz = satzpktDifferenz; }

    public int getsortierung() { return sortierung; }
    public void setsortierung(int sortierung) { this.sortierung = sortierung; }

    public int gettabellenplatz() { return tabellenplatz; }
    public void settabellenplatz(int tabellenplatz) { this.tabellenplatz = tabellenplatz; }

    public int getMatchCount() { return matchCount; }
    public void setMatchCount(int matchCount) { this.matchCount = matchCount; }


    @Override
    public int hashCode() {
        return Objects.hash(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag, mannschaftId,
                mannschaftNummer, vereinId, vereinName, matchpkt, matchpktGegen, satzpkt,
                satzpktGegen, satzpktDifferenz, sortierung, tabellenplatz, matchCount);
    }

    @Override
    public boolean equals(final Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LigatabelleDO that = (LigatabelleDO) o;
        return veranstaltungId.equals(that.veranstaltungId) &&
                Objects.equals(veranstaltungName, that.veranstaltungName)&&
                wettkampfId.equals(that.wettkampfId) &&
                wettkampfTag == that.wettkampfTag &&
                mannschaftId.equals(that.mannschaftId) &&
                mannschaftNummer == that.mannschaftNummer &&
                vereinId.equals(that.vereinId) &&
                Objects.equals(vereinName, that.vereinName)&&
                matchpkt == that.matchpkt &&
                matchpktGegen == that.matchpktGegen &&
                satzpkt == that.satzpkt &&
                satzpktGegen == that.satzpktGegen &&
                satzpktDifferenz == that.satzpktDifferenz &&
                sortierung == that.sortierung &&
                tabellenplatz == that.tabellenplatz &&
                matchCount == that.matchCount;

    }

}