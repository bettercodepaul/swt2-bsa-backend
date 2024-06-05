package de.bogenliga.application.business.ligatabelle.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I´m a composed business entity of the user and the permission business entity.
 *
 * The user permissions are resolved with a JOIN via the user roles, roles, role permissions and permissions.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
public class LigatabelleBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -7930719922483666804L;

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
     * Constructor
     */
    public LigatabelleBE() {
        // empty
    }

    /** hier der select um alle übergeordneten Ligen zu einer Liga zu finden:
     * with recursive cte AS (
     * 	select liga_id, liga_uebergeordnet, 1 as level
     * 	from liga
     *
     * 	union all
     * 	select t.liga_id, c.liga_uebergeordnet, c.level +1
     * 	from cte  c
     * 	join liga t on t.liga_uebergeordnet = c.liga_id
     * 	)select * from cte where liga_uebergeordnet notnull;
     *
     * 	beim select am Ende noch einschränken auf die gesuchte liga_id
     *
     *
     */

    /*
     *
     *
     * @return
     */


    @Override
    public String toString() {
        return "LigatabelleBE{" +
                "veranstaltungId=" + veranstaltungId +
                ", veranstaltungName=" + veranstaltungId +
                ", wettkampfId='" + wettkampfId +
                ", wettkampfTag='" + wettkampfTag +
                ", mannschaftId='" + mannschaftId +
                ", mannschaftNummer='" + mannschaftNummer +
                ", vereinId='" + vereinId +
                ", vereinName='" + vereinName +
                ", matchpkt='" + matchpkt + '\'' +
                ", matchpktGegen='" + matchpktGegen +
                ", satzpkt='" + satzpkt +
                ", satzpktGegen='" + satzpktGegen +
                ", satzpktDifferenz='" + satzpktDifferenz +
                ", sortierung='" + sortierung +
                ", tabellenplatz='" + tabellenplatz +
                ", matchCount='" + matchCount +
                '}';
    }


    public long getVeranstaltungId() {
        return veranstaltungId;
    }
    public void setVeranstaltungId(final long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public String getVeranstaltungName() {
        return veranstaltungName;
    }
    public void setVeranstaltungName(final String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
    }

    public long getWettkampfId() {
        return wettkampfId;
    }
    public void setWettkampfId(final long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public int getWettkampfTag() {
        return wettkampfTag;
    }
    public void setWettkampfTag(final int wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }

    public long getMannschaftId() {
        return mannschaftId;
    }
    public void setMannschaftId(final long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }

    public int getMannschaftNummer() {
        return mannschaftNummer;
    }
    public void setMannschaftNummer(final int mannschaftNummer) {
        this.mannschaftNummer = mannschaftNummer;
    }

    public long getVereinId() {
        return vereinId;
    }
    public void setVereinId(final long vereinId) {
        this.vereinId = vereinId;
    }

    public String getVereinName() {
        return vereinName;
    }
    public void setVereinName(final String vereinName) {
        this.vereinName = vereinName;
    }

    public int getMatchpkt() {
        return matchpkt;
    }
    public void setMatchpkt(final int matchpkt) {
        this.matchpkt = matchpkt;
    }

    public int getMatchpktGegen() {
        return matchpktGegen;
    }
    public void setMatchpktGegen(final int matchpktGegen) {
        this.matchpktGegen = matchpktGegen;
    }

    public int getSatzpkt() {
        return satzpkt;
    }
    public void setSatzpkt(final int satzpkt) {
        this.satzpkt = satzpkt;
    }

    public int getSatzpktGegen() {
        return satzpktGegen;
    }
    public void setSatzpktGegen(final int satzpktGegen) {
        this.satzpktGegen = satzpktGegen;
    }

    public int getSatzpktDifferenz() {
        return satzpktDifferenz;
    }
    public void setSatzpktDifferenz(final int satzpktDifferenz) {
        this.satzpktDifferenz = satzpktDifferenz;
    }

    public int getSortierung() {
        return sortierung;
    }
    public void setSortierung(final int sortierung) {
        this.sortierung = sortierung;
    }

    public int getTabellenplatz() {
        return tabellenplatz;
    }
    public void setTabellenplatz(final int tabellenplatz) {
        this.tabellenplatz = tabellenplatz;
    }

    public int getMatchCount() { return matchCount; }
    public void setMatchCount(int matchCount) { this.matchCount = matchCount; }
}
