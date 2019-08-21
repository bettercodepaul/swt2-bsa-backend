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

    private Long id;
    private Long version;
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
            int matchpkt_gegen,
            int satzpkt,
            int satzpkt_gegen,
            int satzpkt_differenz,
            int sortierung,
            int tabellenplatz
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
        this.id= null;
        this.version = 1L;
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

    public int getMatchpkt() {
        return matchpkt;
    }

    public void setMatchpkt(int matchpkt) {
        this.matchpkt = matchpkt;
    }

    public int getMatchpkt_gegen() {
        return matchpkt_gegen;
    }

    public void setMatchpkt_gegen(int matchpkt_gegen) {
        this.matchpkt_gegen = matchpkt_gegen;
    }

    public int getSatzpkt() {
        return satzpkt;
    }

    public void setSatzpkt(int satzpkt) {
        this.satzpkt = satzpkt;
    }

    public int getSatzpkt_gegen() {
        return satzpkt_gegen;
    }

    public void setSatzpkt_gegen(int satzpkt_gegen) {
        this.satzpkt_gegen = satzpkt_gegen;
    }

    public int getSatzpkt_differenz() {
        return satzpkt_differenz;
    }

    public void setSatzpkt_differenz(int satzpkt_differenz) {
        this.satzpkt_differenz = satzpkt_differenz;
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

    public void setTabellenplatz(int tabellenplatz) {
        this.tabellenplatz = tabellenplatz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LigatabelleDTO that = (LigatabelleDTO) o;
        return wettkampfTag == that.wettkampfTag &&
                mannschaftNummer == that.mannschaftNummer &&
                matchpkt == that.matchpkt &&
                matchpkt_gegen == that.matchpkt_gegen &&
                satzpkt == that.satzpkt &&
                satzpkt_gegen == that.satzpkt_gegen &&
                satzpkt_differenz == that.satzpkt_differenz &&
                sortierung == that.sortierung &&
                tabellenplatz == that.tabellenplatz &&
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
                mannschaftId, mannschaftNummer, vereinId, vereinName, matchpkt, matchpkt_gegen,
                satzpkt, satzpkt_gegen, satzpkt_differenz, sortierung, tabellenplatz);
    }
}
