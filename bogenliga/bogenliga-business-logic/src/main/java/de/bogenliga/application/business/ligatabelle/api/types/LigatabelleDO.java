package de.bogenliga.application.business.ligatabelle.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.time.OffsetDateTime;
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
    private int matchpkt_gegen;
    private int satzpkt;
    private int satzpkt_gegen;
    private int satzpkt_differenz;
    private int sortierung;
    private int tabellenplatz;



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
     * @param matchpkt_gegen;
     * @param satzpkt;
     * @param satzpkt_gegen;
     * @param satzpkt_differenz;
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
            final int matchpkt_gegen,
            final int satzpkt,
            final int satzpkt_gegen,
            final int satzpkt_differenz,
            final int sortierung,
            final int tabellenplatz
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
        this.matchpkt_gegen = matchpkt_gegen;
        this.satzpkt = satzpkt;
        this.satzpkt_gegen = satzpkt_gegen;
        this.satzpkt_differenz = satzpkt_differenz;
        this.sortierung = sortierung;
        this.tabellenplatz = tabellenplatz;
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

    public int getmatchpkt_gegen() { return matchpkt_gegen; }
    public void setmatchpkt_gegen(int matchpkt_gegen) { this.matchpkt_gegen = matchpkt_gegen; }

    public int getsatzpkt() { return satzpkt; }
    public void setsatzpkt(int satzpkt) { this.satzpkt = satzpkt; }

    public int getsatzpkt_gegen() { return satzpkt_gegen; }
    public void setsatzpkt_gegen(int satzpkt_gegen) { this.satzpkt_gegen = satzpkt_gegen; }

    public int getsatzpkt_differenz() { return satzpkt_differenz; }
    public void setsatzpkt_differenz(int satzpkt_differenz) { this.satzpkt_differenz = satzpkt_differenz; }

    public int getsortierung() { return sortierung; }
    public void setsortierung(int sortierung) { this.sortierung = sortierung; }

    public int gettabellenplatz() { return tabellenplatz; }
    public void settabellenplatz(int tabellenplatz) { this.tabellenplatz = tabellenplatz; }



    @Override
    public int hashCode() {
        return Objects.hash(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag, mannschaftId,
                mannschaftNummer, vereinId, vereinName, matchpkt, matchpkt_gegen, satzpkt,
                satzpkt_gegen, satzpkt_differenz, sortierung, tabellenplatz);
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
        return veranstaltungId == that.veranstaltungId &&
                Objects.equals(veranstaltungName, that.veranstaltungName)&&
                wettkampfId ==that.wettkampfId &&
                wettkampfTag == that.wettkampfTag &&
                mannschaftId == that.mannschaftId &&
                mannschaftNummer == that.mannschaftNummer &&
                vereinId == that.vereinId &&
                Objects.equals(vereinName, that.vereinName)&&
                matchpkt == that.matchpkt &&
                matchpkt_gegen == that.matchpkt_gegen &&
                satzpkt == that.satzpkt &&
                satzpkt_gegen == that.satzpkt_gegen &&
                satzpkt_differenz == that.satzpkt_differenz &&
                sortierung == that.sortierung &&
                tabellenplatz == that.tabellenplatz;

    }

}